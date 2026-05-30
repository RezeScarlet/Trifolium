package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.TransactionMapper;
import com.alorisse.trifolium.model.dto.CategorySummaryDTO;
import com.alorisse.trifolium.model.dto.GeneralSummaryDTO;
import com.alorisse.trifolium.model.dto.TransactionRequestDTO;
import com.alorisse.trifolium.model.dto.TransactionResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.Transaction;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.model.enums.Type;
import com.alorisse.trifolium.repository.CategoryRepository;
import com.alorisse.trifolium.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.categoryRepository = categoryRepository;
    }

    public TransactionResponseDTO create(TransactionRequestDTO dto, User user) {
        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new RuntimeException("Category not found."));
        Transaction transaction = transactionMapper.toEntity(dto, user, category);

        if (transaction.getTitle() != null) {
            transaction.setTitle(HtmlUtils.htmlEscape(transaction.getTitle()));
        }
        if (transaction.getDescription() != null) {
            transaction.setDescription(HtmlUtils.htmlEscape(transaction.getDescription()));
        }

        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toDTO(saved);
    }

    public List<TransactionResponseDTO> listAll(User user) {
        return transactionRepository.findByUserIdOrderByDateTimeDesc(user.getId())
                .stream().map(transactionMapper::toDTO).toList();
    }

    public TransactionResponseDTO update(Long id, TransactionRequestDTO dto, User user) {
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transaction not found or user doesn't own this transaction."));

        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new RuntimeException("Category not found."));

        transaction.setAmount(dto.amount());
        transaction.setDateTime(dto.dateTime());

        if (dto.title() != null) {
            transaction.setTitle(HtmlUtils.htmlEscape(dto.title()));
        }
        if (dto.description() != null) {
            transaction.setDescription(HtmlUtils.htmlEscape(dto.description()));
        }

        transaction.setType(Type.valueOf(dto.type()));
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);
        return transactionMapper.toDTO(updated);
    }

    public void delete(Long id, User user) {
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transaction not found."));

        transactionRepository.delete(transaction);
    }

    @Transactional
    public List<TransactionResponseDTO> sync(List<TransactionRequestDTO> dtos, User user) {
        return dtos.stream()
                .map(dto -> create(dto, user))
                .toList();
    }

    public List<CategorySummaryDTO> getExpensesSummary(User user) {
        return transactionRepository.getExpensesByCategory(user.getId());
    }

    public List<CategorySummaryDTO> getIncomeSummary(User user) {
        return transactionRepository.getIncomeSummaryByUser(user);
    }

    public GeneralSummaryDTO getGeneralSummary(User user) {
        BigDecimal totalIncome = transactionRepository.sumIncomeByUser(user);
        BigDecimal totalExpenses = transactionRepository.sumExpensesByUser(user);

        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return new GeneralSummaryDTO(totalIncome, totalExpenses, balance);
    }
}
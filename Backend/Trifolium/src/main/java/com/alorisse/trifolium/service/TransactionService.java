package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.TransactionMapper;
import com.alorisse.trifolium.model.dto.TransactionRequestDTO;
import com.alorisse.trifolium.model.dto.TransactionResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.Transaction;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.model.enums.Type;
import com.alorisse.trifolium.repository.CategoryRepository;
import com.alorisse.trifolium.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

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


    public TransactionResponseDTO create(TransactionResponseDTO dto, User user) {
        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new RuntimeException("Category not found."));
        Transaction transaction = transactionMapper.toEntity(dto, user, category);
        Transaction saved = transactionRepository.save(transaction);

        return transactionMapper.toDTO(saved);
    }

    public List<TransactionResponseDTO> listAll(User user) {
        return transactionRepository.findByUserIdOrderByDateTimeDesc(user.getId()).stream().map(transactionMapper::toDTO).toList();
    }

    public TransactionResponseDTO update(Long id, TransactionRequestDTO dto, User user) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found."));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User doesn't own this transaction.");
        }

        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new RuntimeException("Category not found."));

        transaction.setAmount(dto.amount());
        transaction.setDateTime(dto.dateTime());
        transaction.setTitle(dto.title());
        transaction.setDescription(dto.description());
        transaction.setType(Type.valueOf(dto.type()));
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);
        return transactionMapper.toDTO(updated);
    }

    public void delete(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found."));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User doesn't own this transaction.");
        }

        transactionRepository.delete(transaction);
    }
}

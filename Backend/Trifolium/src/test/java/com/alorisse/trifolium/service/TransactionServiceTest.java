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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Should create a transaction successfully")
    void shouldCreateTransactionSuccessfully() {
        User user = new User();
        user.setId(1L);

        Long id = 10L;
        BigDecimal amount = new BigDecimal(10);
        Instant dateTime = Instant.now();
        String title = "Dinner";
        String description = "Spaghetti";
        Type type = Type.OUTCOME;

        Category category = new Category();
        category.setId(1L);
        category.setTitle("Food");
        category.setColor("#FFBBCC");
        category.setIcon("food_icon");

        TransactionRequestDTO request = new TransactionRequestDTO(
                amount,
                dateTime,
                title,
                description,
                type.toString(),
                category.getId()
        );

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(amount);
        transaction.setDateTime(dateTime);
        transaction.setTitle(title);
        transaction.setDescription(description);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setUser(user);

        TransactionResponseDTO expectedResponse = new TransactionResponseDTO(
                id,
                amount,
                dateTime,
                title,
                description,
                type.toString(),
                category.getTitle(),
                category.getColor(),
                category.getIcon()
        );

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(transactionMapper.toEntity(request, user, category)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(expectedResponse);

        TransactionResponseDTO result = transactionService.create(request, user);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.amount()).isEqualTo(amount);
        assertThat(result.dateTime()).isEqualTo(dateTime);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.description()).isEqualTo(description);
        assertThat(result.type()).isEqualTo(type.toString());
        assertThat(result.categoryTitle()).isEqualTo(category.getTitle());
        assertThat(result.categoryColor()).isEqualTo(category.getColor());
        assertThat(result.categoryIcon()).isEqualTo(category.getIcon());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


}
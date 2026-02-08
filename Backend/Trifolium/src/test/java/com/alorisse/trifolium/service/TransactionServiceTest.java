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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    @DisplayName("Should create a transaction successfully")
    void shouldCreateTransaction() {
        User user = buildUser();
        Category category = buildCategory(user);
        Transaction transaction = buildTransaction("Dinner", category, user);

         Long id = transaction.getId();
         BigDecimal amount = transaction.getAmount();
         Instant dateTime = transaction.getDateTime();
         String title = transaction.getTitle();
         String description = transaction.getDescription();
         String type = transaction.getType().toString();

        TransactionRequestDTO request = new TransactionRequestDTO(
                amount,
                dateTime,
                title,
                description,
                type,
                category.getId()
        );

        TransactionResponseDTO expectedResponse = new TransactionResponseDTO(
                id,
                amount,
                dateTime,
                title,
                description,
                type,
                category.getTitle(),
                category.getColor(),
                category.getIcon()
        );

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(transactionMapper.toEntity(any(TransactionRequestDTO.class), eq(user), eq(category))).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(expectedResponse);

        when(transactionMapper.toDTO(transaction)).thenReturn(expectedResponse);

        TransactionResponseDTO result = transactionService.create(request, user);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.amount()).isEqualTo(amount);
        assertThat(result.dateTime()).isEqualTo(dateTime);
        assertThat(result.description()).isEqualTo(description);
        assertThat(result.type()).isEqualTo(type);
        assertThat(result.categoryTitle()).isEqualTo(category.getTitle());
        assertThat(result.categoryColor()).isEqualTo(category.getColor());
        assertThat(result.categoryIcon()).isEqualTo(category.getIcon());

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should list user transactions successfully")
    void shouldListUserTransactions() {
        User user = buildUser();
        Category category = buildCategory(user);
        Transaction transaction = buildTransaction("Dinner", category, user);

        List<Transaction> transactionList = List.of(transaction);

        TransactionResponseDTO responseDTO = new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDateTime(), transaction.getTitle(), transaction.getDescription(), transaction.getType().toString(), category.getTitle(), category.getColor(), category.getIcon());

        when(transactionRepository.findByUserIdOrderByDateTimeDesc(user.getId())).thenReturn(transactionList);
        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(responseDTO);

        List<TransactionResponseDTO> result = transactionService.listAll(user);

        assertThat(result)
                .isNotNull()
                .hasSize(1);

        assertThat(result.getFirst().title()).isEqualTo("Dinner");

        verify(transactionRepository).findByUserIdOrderByDateTimeDesc(user.getId());

    }

    @Test
    @DisplayName("Should update transaction successfully")
    void shouldUpdateTransaction() {
        User user = buildUser();
        Category category = buildCategory(user);
        Transaction oldTransaction = buildTransaction("Dinner", category, user);
        String newTitle = "Lunch";

        Long id = oldTransaction.getId();
        BigDecimal amount = oldTransaction.getAmount();
        Instant dateTime = oldTransaction.getDateTime();
        String description = oldTransaction.getDescription();
        String type = oldTransaction.getType().toString();
        Long categoryId = category.getId();

        TransactionRequestDTO request = new TransactionRequestDTO(amount, dateTime, newTitle, description, type, categoryId);
        TransactionResponseDTO response = new TransactionResponseDTO(id, amount, dateTime, newTitle, description, type, category.getTitle(), category.getColor(), category.getIcon());

        when(transactionRepository.findById(id)).thenReturn(Optional.of(oldTransaction));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(transactionMapper.toDTO(oldTransaction)).thenReturn(response);

        TransactionResponseDTO result = transactionService.update(id, request, user);

        assertThat(result).isNotNull();

        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction newTransaction = transactionCaptor.getValue();

        assertThat(newTransaction.getTitle()).isEqualTo("Lunch");
    }

    @Test
    @DisplayName("Should delete transaction successfully")
    void shouldDeleteTransaction() {
        User user = buildUser();
        Category category = buildCategory(user);
        Transaction transaction = buildTransaction("Dinner", category, user);
        Long id = transaction.getId();

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        transactionService.delete(id, user);

        verify(transactionRepository).delete(transaction);


    }

    @Test
    @DisplayName("Should throw exception when creating transaction with invalid category")
    void shouldThrowExceptionWhenCategoryNotFound() {
        User user = buildUser();
        Long invalidCategoryId = 99L;

        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal(10), Instant.now(), "Dinner", "Spaghetti", "OUTCOME", invalidCategoryId
        );

        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.create(request, user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Category not found.");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail to delete transaction belonging to another user")
    void shouldFailDeleteTransactionOfAnotherUser() {
        User owner = buildUser();

        User notOwner = buildUser();
        notOwner.setId(2L);

        Category category = buildCategory(owner);
        Transaction transaction = buildTransaction("Private", category, owner);

        when(transactionRepository.findById(transaction.getId()))
                .thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.delete(transaction.getId(), notOwner))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User doesn't own this transaction.");

        verify(transactionRepository, never()).delete(any());
    }

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        return user;
    }

    private Category buildCategory(User user) {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Food");
        category.setColor("#FFBBCC");
        category.setIcon("food_icon");
        category.setUser(user);

        return category;
    }

    private Transaction buildTransaction(String title, Category category, User user) {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal(10));
        transaction.setDateTime(Instant.now());
        transaction.setTitle(title);
        transaction.setDescription("Spaghetti");
        transaction.setType(Type.OUTCOME);
        transaction.setCategory(category);
        transaction.setUser(user);

        return transaction;
    }

}
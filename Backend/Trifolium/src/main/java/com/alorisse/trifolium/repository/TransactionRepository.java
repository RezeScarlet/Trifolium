package com.alorisse.trifolium.repository;

import com.alorisse.trifolium.model.dto.CategorySummaryDTO;
import com.alorisse.trifolium.model.entity.Transaction;
import com.alorisse.trifolium.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateTimeDesc(Long userId);

    @Query("SELECT new com.alorisse.trifolium.model.dto.CategorySummaryDTO(c.title, c.color, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "WHERE t.user.id = :userId AND t.type = com.alorisse.trifolium.model.enums.Type.OUTCOME " + // <-- ESPAÇO ADICIONADO AQUI NO FINAL
            "GROUP BY c.title, c.color ORDER BY SUM(t.amount) DESC")
    List<CategorySummaryDTO> getExpensesByCategory(@Param("userId") Long userId);

    @Query("SELECT new com.alorisse.trifolium.model.dto.CategorySummaryDTO(t.category.title, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.type = 'INCOME' AND t.user = :user " +
            "GROUP BY t.category.title")
    List<CategorySummaryDTO> getIncomeSummaryByUser(@Param("user") User user);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'INCOME' AND t.user = :user")
    BigDecimal sumIncomeByUser(@Param("user") User user);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'OUTCOME' AND t.user = :user")
    BigDecimal sumExpensesByUser(@Param("user") User user);
}
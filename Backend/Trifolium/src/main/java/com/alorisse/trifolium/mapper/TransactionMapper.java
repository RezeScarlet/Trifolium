package com.alorisse.trifolium.mapper;

import com.alorisse.trifolium.model.dto.TransactionRequestDTO;
import com.alorisse.trifolium.model.dto.TransactionResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.Transaction;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.model.enums.Type;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction toEntity(TransactionRequestDTO dto, User user, Category category) {
        Transaction entity = new Transaction();
        entity.setAmount(dto.amount());
        entity.setDateTime(dto.dateTime());
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setType(Type.valueOf((dto.type())));
        entity.setUser(user);
        entity.setCategory(category);
        return entity;

    }

    public TransactionResponseDTO toDTO(Transaction entity) {
        return new TransactionResponseDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getDateTime(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getType().toString(),
                entity.getCategory().getTitle(),
                entity.getCategory().getColor(),
                entity.getCategory().getIcon()
        );
    }
}

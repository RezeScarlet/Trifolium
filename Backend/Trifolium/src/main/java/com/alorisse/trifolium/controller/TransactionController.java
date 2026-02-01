package com.alorisse.trifolium.controller;

import com.alorisse.trifolium.model.dto.TransactionRequestDTO;
import com.alorisse.trifolium.model.dto.TransactionResponseDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !((authentication.getPrincipal()) instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return (User) authentication.getPrincipal();
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO dto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        TransactionResponseDTO response = transactionService.create(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> listAll(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(transactionService.listAll(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable Long id, @RequestBody @Valid TransactionRequestDTO dto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(transactionService.update(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        transactionService.delete(id, user);
        return ResponseEntity.noContent().build();

    }

}

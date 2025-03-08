package io.github.mark.libraryapi.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LivroResponseRequest(
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        String genero,
        BigDecimal preco
) {}
package io.github.mark.libraryapi.controller.dto;

import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.model.Livro;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AutorResponseRequest(
        UUID id,
        String nome,
        LocalDate dataNascimento,
        String nacionalidade
) {
}

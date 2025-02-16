package io.github.mark.libraryapi.dto;

import java.time.LocalDate;

public record AtualizarAutorRequest(String nome, LocalDate dataNascimento, String nacionalidade) {
}

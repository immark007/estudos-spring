package io.github.mark.libraryapi.controller.dto;

import io.github.mark.libraryapi.model.Autor;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record AutorRequest (
        @NotBlank(message = "campo obrigatório!!")
        String nome,
        @NotNull
        LocalDate dataNascimento,
        @NotBlank
        String nacionalidade) {

        public void atualizarAutor(Autor autor) {
                autor.setNome(this.nome);
                autor.setDataNascimento(this.dataNascimento);
                autor.setNacionalidade(this.nacionalidade);
        }
}

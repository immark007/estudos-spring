package io.github.mark.libraryapi.controller.dto;

import io.github.mark.libraryapi.model.Autor;

import java.time.LocalDate;

public record AtualizarAutorRequest(String nome, LocalDate dataNascimento, String nacionalidade) {



    public static void converter(AtualizarAutorRequest atualizarAutorRequest) {
        Autor autor = new Autor();
        autor.setNome(atualizarAutorRequest.nome);
        autor.setNacionalidade(atualizarAutorRequest.nacionalidade);
        autor.setDataNascimento(atualizarAutorRequest.dataNascimento);
    }
}

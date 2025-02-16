package io.github.mark.libraryapi.dto;

import io.github.mark.libraryapi.model.Autor;

import java.time.LocalDate;

public record AutorRequest (String nome, LocalDate dataNascimento, String nacionalidade) {


    public Autor mapearAutor(){
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setDataNascimento(dataNascimento);
        autor.setNacionalidade(nacionalidade);
        return autor;
    }
}

package io.github.mark.libraryapi.dto;

import io.github.mark.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorResponseRequest(
        UUID id,
        String nome,
        LocalDate dataNascimento,
        String nacionalidade
) {

    public Autor mapearAutor(){
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setDataNascimento(dataNascimento);
        autor.setNacionalidade(nacionalidade);
        return autor;
    }
}

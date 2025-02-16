package io.github.mark.libraryapi.repository;

import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
    //Query Method
    List<Livro> findByAutor(Autor autor);
    //Manda uma lista de livros pelo Titulo
    List<Livro> findByTitulo(String titulo);
    //Mnada uma lista de titulos pelo Autor e pelo titulo como parametros
    List<Livro> findByAutorAndTitulo(Autor autor, String titulo);
    //Posso passar um ou outro como parametro
    List<Livro> findByTituloOrAutor(String titulo, Autor autor);
}

package io.github.mark.libraryapi.services;

import io.github.mark.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.mark.libraryapi.model.GeneroLivro;
import io.github.mark.libraryapi.model.Livro;
import io.github.mark.libraryapi.repository.LivroRepository;
import io.github.mark.libraryapi.repository.specs.LivroSpecs;
import io.github.mark.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static io.github.mark.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;
    public Livro salvar(Livro livro){
        livroValidator.validar(livro);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterDetalhes(UUID id){
        return livroRepository.findById(id);
    }

    public void deletar(Livro livro){
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa(String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao, Integer pagina, Integer tamanhoPagina){
        //Specification<Livro> specs = Specification.where(LivroSpecs.isbnEqual(isbn)).and(LivroSpecs.tituloLike(titulo)).and(LivroSpecs.generoEqual(genero));

        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());
        if(isbn != null){
            specs = specs.and(isbnEqual(isbn));
        }

        if(titulo != null){
            specs = specs.and(tituloLike(titulo));
        }

        if(genero != null){
            specs = specs.and(generoEqual(genero));
        }

        if(anoPublicacao != null){
            specs = specs.and(anoPublicacao(anoPublicacao));
        }

        if(nomeAutor != null){
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageable = PageRequest.of(pagina, tamanhoPagina);

        //Specification<Livro> isbnEqual = (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
        return livroRepository.findAll(specs, pageable);
    }

    public void atualizar(Livro livro){
        if(livro.getId() == null){
            throw new OperacaoNaoPermitidaException("Não existe nenhum usuário com esse id");
        }
        livroValidator.validar(livro);
        livroRepository.save(livro);
    }
}

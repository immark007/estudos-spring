package io.github.mark.libraryapi.validator;

import io.github.mark.libraryapi.exceptions.CampoInvalidoException;
import io.github.mark.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mark.libraryapi.model.Livro;
import io.github.mark.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {
    private final LivroRepository livroRepository;
    private static final int ANO_EXIGENCIA_PRECO = 2020;

    public void validar(Livro livro) {
        if(existeLivroIsbn(livro)) {
            throw new RegistroDuplicadoException("Isbn já cadastrado");
        }

        if(isPrecoObrigatorioNullo(livro)){
            throw new CampoInvalidoException("preco", "Para livros com ano de publicação com ano de publicação de 2020, o preço é obrigatório");
        }
    }

    private boolean isPrecoObrigatorioNullo(Livro livro) {
        return livro.getPreco() == null && livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

    private boolean existeLivroIsbn(Livro livro) {
       Optional<Livro> contem =  livroRepository.findByIsbn(livro.getIsbn());

        if(livro.getId() == null){
            return contem.isPresent();
        }

        return contem.map(Livro::getId).stream().anyMatch(id -> !id.equals(livro.getId()));
    }
}

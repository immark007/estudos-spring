package io.github.mark.libraryapi.validator;

import io.github.mark.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {
    private final AutorRepository autorRepository;

    public AutorValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void validate(Autor autor) {
        if(existeAutor(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado");
        }
    }

    private boolean existeAutor(Autor autor) {
        Optional<Autor> autorEncontrado = autorRepository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());

        if(autor.getId() == null) {
            return autorEncontrado.isPresent();
        }


        return autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }
}

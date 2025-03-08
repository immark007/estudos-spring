package io.github.mark.libraryapi.validator;

import io.github.mark.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {
    //Injetando a dependencia
    private final AutorRepository autorRepository;

    public AutorValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    //Método para retornar uma exception caso a validação ocorra
    //Aqui toda vez que a validação for verdadeira, retornamos uma execption personalizada
    public void validate(Autor autor) {
        if(existeAutor(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado");
        }
    }


    //Aqui vemos dois cenarios, primeiro, verificamos se o AutorEcnontrado tem os mesmas informações que um autor já cadastrado
    //aqui passamos as informações do autor, e ele vai verificar no banco de dados se tem algum autor com essas informações
    //Se caso o id do autor for igual a nullo, significa que é um novo autor, ent ele vai verificar se o autor buscado existe
    //Logo em seguida, verificamos se o id do autor é igual ao id do autorEncontrado e verificamos se o autor encontrado é true
    private boolean existeAutor(Autor autor) {
        Optional<Autor> autorEncontrado = autorRepository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());

        if(autor.getId() == null) {
            return autorEncontrado.isPresent();
        }


        return autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }

}

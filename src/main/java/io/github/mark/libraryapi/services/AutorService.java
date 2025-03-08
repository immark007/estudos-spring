package io.github.mark.libraryapi.services;

import io.github.mark.libraryapi.controller.dto.AtualizarAutorRequest;
import io.github.mark.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.model.Livro;
import io.github.mark.libraryapi.model.Usuario;
import io.github.mark.libraryapi.repository.AutorRepository;
import io.github.mark.libraryapi.repository.LivroRepository;
import io.github.mark.libraryapi.security.SecurityService;
import io.github.mark.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final AutorValidator autorValidator;
    private final SecurityService securityService;

    //Salvar um autor
    @Transactional
    public Autor save(Autor autor){
        autorValidator.validate(autor);
        Usuario usuario = securityService.autenticar();
        autor.setUsuario(usuario);
        return autorRepository.save(autor);
    }

    //Update Autor
    @Transactional
    public void update(Autor autor){
        if(autor.getId() == null){
            throw new IllegalArgumentException("Para atualizar é necessário que o autor já esteja salvo na base");
        }
        autorValidator.validate(autor);
        Usuario usuario = securityService.autenticar();
        autor.setUsuario(usuario);
        autorRepository.save(autor);
    }

    //Buscar por ID
    public Optional<Autor> buscarPorId(UUID id){
        return autorRepository.findById(id);
    }

    //Listar todos
    public List<Autor> listarAutor(){
        if(autorRepository.findAll().isEmpty()){
            throw new IllegalArgumentException("Não existe nenhum autor no banco");
        }
        return autorRepository.findAll();
    }

    //Contagem
    public Long quantidadeAutores(){
        return autorRepository.count();
    }

    //Deletar autor
    //Aqui verificamos se o autor tem livro ou não, se não tiver, ele faz a exclusão normalmente
    public void removerAutor(Autor autor){
        if(possuiLivro(autor)){
            throw new OperacaoNaoPermitidaException("Não é permitido!! Autor já existe");
        }

        autorRepository.delete(autor);
    }

    //Atualizar autor
    @Transactional
    public void atualizarAutor(UUID id, AtualizarAutorRequest atualizarAutorRequest){
        var autor = autorRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Esse autor não existe no banco"));

        Optional.ofNullable(atualizarAutorRequest.nome()).ifPresent(autor::setNome);
        Optional.ofNullable(atualizarAutorRequest.nacionalidade()).ifPresent(autor::setNacionalidade);
        Optional.ofNullable(atualizarAutorRequest.nacionalidade()).ifPresent(autor::setNacionalidade);

        autorRepository.save(autor);
    }

    //Buscar livros por autor
    public List<Livro> buscarLivroDoAutor(Autor autor){
        return livroRepository.findByAutor(autor);
    }

    public List<Autor> pesquisa(String nome, String nacionalidade){
        if(nome != null && nacionalidade != null){
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if(nome != null){
            return autorRepository.findByNome(nome);
        }

        if(nacionalidade != null){
            return autorRepository.findByNacionalidade(nacionalidade);
        }

        return autorRepository.findAll();
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id","dataNascimento", "dataCadastro").withIgnoreNullValues().withIgnoreCase(true).withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> autorExample = Example.of(autor, matcher);
        return autorRepository.findAll(autorExample);
    }

    //Aqui eu verifico se o autor que eu quero deletar, possui um livro, se caso ele possuir algum livro, ele não pode ser deletado
    public boolean possuiLivro(Autor autor){
        return livroRepository.existsByAutor(autor);
    }
}

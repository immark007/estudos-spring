package io.github.mark.libraryapi.services;

import io.github.mark.libraryapi.dto.AtualizarAutorRequest;
import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.model.Livro;
import io.github.mark.libraryapi.repository.AutorRepository;
import io.github.mark.libraryapi.repository.LivroRepository;
import io.github.mark.libraryapi.validator.AutorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorValidator autorValidator;

    //Salvar um autor
    @Transactional
    public Autor save(Autor autor){
        autorValidator.validate(autor);
        return autorRepository.save(autor);
    }

    //Update Autor
    @Transactional
    public void update(Autor autor){
        if(autor.getId() == null){
            throw new IllegalArgumentException("Para atualizar é necessário que o autor já esteja salvo na base");
        }
        autorValidator.validate(autor);
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

    //Deletar por id
    public void removerAutor(UUID id){
        if(autorRepository.existsById(id)){
            autorRepository.deleteById(id);
        }
        throw new IllegalArgumentException("Não existe autor com esse id");
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
}

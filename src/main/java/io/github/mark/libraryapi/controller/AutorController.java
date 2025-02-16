package io.github.mark.libraryapi.controller;

import io.github.mark.libraryapi.dto.AutorRequest;
import io.github.mark.libraryapi.dto.AutorResponseRequest;
import io.github.mark.libraryapi.dto.ErroResposta;
import io.github.mark.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.services.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    //Criando um novo autor
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody AutorRequest autor) {
        try {
            Autor autorEntidade = autor.mapearAutor();
            autorService.save(autorEntidade); // Corrigindo aqui!

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntidade.getId()) // Agora o ID será preenchido corretamente.
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            var erroDto = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDto.status()).body(erroDto);
        }
    }

    //Buscando por id
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseRequest> details(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autor = autorService.buscarPorId(idAutor);
        if(autor.isPresent()){
            Autor entidade = autor.get();
            AutorResponseRequest dto = new AutorResponseRequest(entidade.getId(),entidade.getNome(),entidade.getDataNascimento(), entidade.getNacionalidade());
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    //Deletando por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autor = autorService.buscarPorId(idAutor);
        if(autor.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        autorService.removerAutor(idAutor);
        return ResponseEntity.noContent().build();
    }


    //Buscando por parametros
    @GetMapping
    public ResponseEntity<List<AutorResponseRequest>> listarPorParametros(
            @RequestParam(value = "nome", required = false ) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> resultado = autorService.pesquisa(nome, nacionalidade);

        List<AutorResponseRequest> lista = resultado.stream().map(autor -> new AutorResponseRequest(autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade())).toList();

        return ResponseEntity.ok(lista);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@RequestBody AutorRequest autorRequest, @PathVariable("id") String id) {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> busca = autorService.buscarPorId(idAutor);
            if (busca.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = busca.get();
            autor.setNome(autorRequest.nome());
            autor.setDataNascimento(autorRequest.dataNascimento());
            autor.setNacionalidade(autorRequest.nacionalidade());
            autorService.update(autor);

            return ResponseEntity.noContent().build();
        } catch (RegistroDuplicadoException e) {
            var erroDto = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDto.status()).body(erroDto);
        }
    }

}

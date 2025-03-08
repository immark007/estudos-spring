package io.github.mark.libraryapi.controller;

import io.github.mark.libraryapi.controller.dto.AutorRequest;
import io.github.mark.libraryapi.controller.dto.AutorResponseRequest;
import io.github.mark.libraryapi.controller.dto.ErroResposta;
import io.github.mark.libraryapi.controller.mappers.AutorMapper;
import io.github.mark.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.mark.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mark.libraryapi.model.Autor;
import io.github.mark.libraryapi.model.Usuario;
import io.github.mark.libraryapi.security.SecurityService;
import io.github.mark.libraryapi.services.AutorService;
import io.github.mark.libraryapi.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController implements GenericController {
    private final AutorService autorService;
    private final AutorMapper autorMapper;

    //Criando um novo autor
    //Usamos o try-catch para tratar o erro, ou seja, ele espera o erro RegistroDuplicadoException, que é alertado no nosso método de validação
    //Se caso esse erro acontecer, criar uma variável, que vai ser igual ao meu Record de ErroResposta
    //Nele passamos o tipo de erro que vai ser, nesse caso é o conflito, a mensagem passada vai ser a mensagem da exception
    //O response vai retornar o status de erro, que vai ser conflito, declarado na variável erroDto, e no corpo da requisição, vai ser passado as infomrações do dto
    //Que é o número de status de erro, a mensagem e por fim uma lista vazia
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AutorRequest autorRequest) {

            //UserDetails usuarioLogado = (UserDetails) authentication.getPrincipal();
            //Usuario usuario = usuarioService.obterPorLogin(usuarioLogado.getUsername());
            Autor autor = autorMapper.toEntity(autorRequest);
            //autor.setIdUsuario(usuario.getId());
            autorService.save(autor); // Corrigindo aqui!

            //URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    //.path("/{id}")
                    //.buildAndExpand(autor.getId()) // Agora o ID será preenchido corretamente.
                    //.toUri();

            URI location = gerarHeaderLocation(autor.getId());

            return ResponseEntity.created(location).build();

    }

    //Buscando por id
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseRequest> details(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        //Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);
        //if(autorOptional.isPresent()){
            //Autor autor = autorOptional.get();
            //AutorResponseRequest dto = autorMapper.toAutorResponseRequest(autor);
            //return ResponseEntity.ok(dto);
        //}

        //return ResponseEntity.notFound().build();
        //Refatorando código
        return autorService.buscarPorId(idAutor)
                .map(autor -> {
                    AutorResponseRequest autorResponseRequest = autorMapper.toAutorResponseRequest(autor);
                    return ResponseEntity.ok(autorResponseRequest);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());


    }

    //Deletando por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autor = autorService.buscarPorId(idAutor);
            if (autor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            autorService.removerAutor(autor.get());
            return ResponseEntity.noContent().build();
    }

    //Buscando por parametros
    @GetMapping
    public ResponseEntity<List<AutorResponseRequest>> listarPorParametros(
            @RequestParam(value = "nome", required = false ) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);

        //Método antigo
        //List<AutorResponseRequest> lista = resultado.stream().map(autor -> new AutorResponseRequest(autor.getId(),
               // autor.getNome(),
                //autor.getDataNascimento(),
                //autor.getNacionalidade())).toList();
        List<AutorResponseRequest> lista = resultado.stream().map(autorMapper::toAutorResponseRequest).collect(Collectors.toList());
        return ResponseEntity.ok(lista);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@RequestBody @Valid AutorRequest autorRequest, @PathVariable("id") String id) {
            var idAutor = UUID.fromString(id);
            Optional<Autor> busca = autorService.buscarPorId(idAutor);
            if (busca.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = busca.get();
            autorRequest.atualizarAutor(autor);
            autorService.update(autor);

            return ResponseEntity.noContent().build();
    }

}

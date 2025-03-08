package io.github.mark.libraryapi.security;

import io.github.mark.libraryapi.model.Usuario;
import io.github.mark.libraryapi.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//Falando pro spring que isso é um componente
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    //Injetando as dependências
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    //Método de autenticação dando pelo AuthenticationProvider
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //Aqui a gente fala que o login, em formato de String, vai ser equivalente ao login (getName) do authentication
        String login = authentication.getName();
        //Mesma coisa com a senha, porém o getCredentials espera um object, ent convertemos ele para toString
        String password = authentication.getCredentials().toString();

        //Aqui instânciamos um usuário, que vai receber o serviço que busca o usuário pelo login (identificador unico, no nosso caso o email)
        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);

        //Aqui verificamos se esse valor obtido pelo login é nullo
        if(usuarioEncontrado == null) {
            throw new UsernameNotFoundException("Usuário e/ou senha incorreto");
        }

        //Aqui passamos nossa senha senha criptografa, pq quando passamos que o usuário encontrado vai ser equivalente ao usuário buscado pelo login (Optional)
        String senhaCriptografada = usuarioEncontrado.getSenha();

        //passamos um booleano de um método que existe no enconder, que ele compara a senha digitada (123), com a senha criptografada (hash)
        //E retorna se é verdadeiro ou falsa
        boolean senhasBatem = passwordEncoder.matches(password, senhaCriptografada);
        //Aqui verificamos se a comparação das senhas é verdadeiro
        if(senhasBatem){
            //Aqui a gente retorna o usuário já autenticado
            return new CustomAuthentication(usuarioEncontrado);
        }
        //Se caso as senhas não batem, retornando uma exception
        throw new UsernameNotFoundException("Usuário e/ou senha incorreto");
    }

    //Nesse aqui passamos o tipo de login que iremos passar, no nosso caso é o usernamePasswordAuthenticationToken
    //Ou seja, ele vai fazer a autentication passando o nome(login) e a passowrd (senha)
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}

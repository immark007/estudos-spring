package io.github.mark.libraryapi.security;

import io.github.mark.libraryapi.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final Usuario usuario;


    //Retorna as roles
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        //Transformando uma lista de items (roles) em authority
        return this.usuario.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //Aqui fica null, pq o usuário já autenticado
    @Override
    public Object getCredentials() {
        return null;
    }


    //Aqui podemos retornar uma informação do user
    @Override
    public Object getDetails() {
        return usuario;
    }


    //No principal retornamos o usuário
    @Override
    public Object getPrincipal() {
        return usuario;
    }

    //Tem que ser true, pq se não não conseguimos autenticar o usuário
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    //Defini qual vai ser o identificador unico de autenticação, no nosso caso é o login
    @Override
    public String getName() {
        return usuario.getLogin();
    }
}

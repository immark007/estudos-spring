package io.github.mark.libraryapi.security;

import io.github.mark.libraryapi.model.Usuario;
import io.github.mark.libraryapi.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService usuarioService;

    public Usuario autenticar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Jeito padrão sem o uso de authentications personalizadas
        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //String login = userDetails.getUsername();
        //return usuarioService.obterPorLogin(login);
        if(authentication instanceof CustomAuthentication custoomAuth){
            return custoomAuth.getUsuario();
        }

        return null;
    }
}

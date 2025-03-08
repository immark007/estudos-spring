package io.github.mark.libraryapi.security;


import io.github.mark.libraryapi.model.Usuario;
import io.github.mark.libraryapi.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
    private final UsuarioService usuarioService;
    private static final String SENHA_PADRAO = "321";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken auth2Authentication = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = auth2Authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        Usuario usuario = usuarioService.obterPorEmail(email);

        authentication = new CustomAuthentication(usuario);

        if(usuario == null){
            methodUser(email);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void methodUser(String email) {
        new Usuario().setEmail(email);
        new Usuario().setLogin(email);
        new Usuario().setSenha(SENHA_PADRAO);
        new Usuario().setRoles(List.of("OPERADOR"));
        usuarioService.salvar(new Usuario());
    }
}

package io.github.mark.libraryapi.config;

import io.github.mark.libraryapi.security.CustomUserDetailsService;
import io.github.mark.libraryapi.security.LoginSocialSuccessHandler;
import io.github.mark.libraryapi.services.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler successHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.POST, "/autores").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();
                    authorize.requestMatchers(HttpMethod.POST,"/livros/**").hasAnyRole("ADMIN");
                    authorize.anyRequest().authenticated();

                })
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    //@Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService) {

        //UserDetails user1 = User.builder()
          //      .username("admin")
            //    .password(passwordEncoder.encode("123"))
              //  .roles("USER")
                //.build();


    //    UserDetails user2 = User.builder()
       //         .username("marquinhos")
         //       .password(passwordEncoder.encode("321"))
           //     .roles("ADMIN")
             //   .build();

        //return new InMemoryUserDetailsManager(user1, user2);

        return new CustomUserDetailsService(usuarioService);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}

package com.uniovi.sdi.sdi2526entrega121;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/script/**", "/", "/signup",
                                "/login/**").permitAll()
                        .requestMatchers("/reservations/export",
                                "/toggle/*",
                                "/spaces/new",
                                "/spaces/edit/*",
                                "/spaces/list/*",
                                "/spaces/new/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/reservations/add").hasAuthority("ROLE_EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout((logout) -> logout
                    .logoutUrl("/logout").permitAll())
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(true));

        return http.build();
    }
}



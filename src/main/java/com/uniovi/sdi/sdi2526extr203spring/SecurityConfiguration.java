package com.uniovi.sdi.sdi2526extr203spring;

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
                        .requestMatchers("/reservations/export").hasAnyRole("ADMIN")
                        .requestMatchers(
                                "/reservations/list",
                                "/reservations/update"
                        ).hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(
                                "/reservations/add",
                                "/reservations/cancel/**"
                        ).hasAnyRole("EMPLOYEE")
                        .requestMatchers(
                                "/spaces/new",
                                "/spaces/edit/**",
                                "/spaces/toggle/**",
                                "/spaces/update"
                        ).hasAnyRole("ADMIN")
                        .requestMatchers(
                                "/spaces/list",
                                "/spaces/detail/**",
                                "/space/availability/**",
                                "/spaces/availability/**"
                        ).hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/blocks/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/home",
                                "/profile/**"
                        ).authenticated()
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



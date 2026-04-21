package com.tienda;

import com.tienda.service.RutaService;
import com.tienda.domain.Ruta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SecurityConfig {

 
    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http,
        @Lazy RutaService rutaService) throws Exception {
var rutas = rutaService.getRutas();
    
   http.authorizeHttpRequests(request -> {
    for (Ruta ruta : rutas) {
        if (ruta.isRequiereRol()) {
            request.requestMatchers(ruta.getRuta())
                   .hasRole(ruta.getRol().getRol());
        } else {
            request.requestMatchers(ruta.getRuta()).permitAll();
        }
    }
    request.anyRequest().authenticated(); // ✅ siempre al final
})
.formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/", true)
        .failureUrl("/login?error=true")
        .permitAll()
)
.logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout=true")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID") 
        .permitAll()
)
.sessionManagement(session -> session
        .maximumSessions(1)
        .maxSessionsPreventsLogin(false)
)
.exceptionHandling(ex -> ex.accessDeniedPage("/acceso_denegado"));

    return http.build();
}
@Bean
        public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
    
}
@Autowired
public void configurerGlobal(AuthenticationManagerBuilder build,
        @Lazy PasswordEncoder passwordEncoder,
        @Lazy UserDetailsService userDetailsService
        ) throws Exception {
    build.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
}
}

  
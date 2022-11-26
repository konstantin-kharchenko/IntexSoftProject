package by.kharchenko.intexsoftproject.config;

import by.kharchenko.intexsoftproject.controllers.filter.JwtAuthorizationFilter;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static by.kharchenko.intexsoftproject.model.entity.RoleType.ROLE_ADMINISTRATOR;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user", "/product/**").authenticated()
                .antMatchers("/generate-card-number/**", "/create-card/**").hasRole(ROLE_ADMINISTRATOR.getName())
 /*               .antMatchers("/post/**").hasAuthority(AuthorityType.POST.toString())*/
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenProvider));
        http.logout()
                .logoutUrl("/perform-logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }
}
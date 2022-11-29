package by.kharchenko.intexsoftproject.config;

import by.kharchenko.intexsoftproject.filter.JwtAuthorizationFilter;
import by.kharchenko.intexsoftproject.filter.RestAuthenticationEntryPoint;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import static by.kharchenko.intexsoftproject.model.entity.RoleType.ROLE_ADMINISTRATOR;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RestAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, RestAuthenticationEntryPoint unauthorizedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().
                authorizeRequests()
                .antMatchers("/user/**", "/product/**").authenticated()
                .antMatchers("/generate-card-number/**", "/create-card/**").hasRole(ROLE_ADMINISTRATOR.getName())
 /*               .antMatchers("/post/**").hasAuthority(AuthorityType.POST.toString())*/
                .and()
                .addFilter(new JwtAuthorizationFilter( jwtTokenProvider, authenticationManager()));
        http.logout()
                .logoutUrl("/perform-logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }
}
package by.kharchenko.intexsoftproject.config;

import by.kharchenko.intexsoftproject.controller.filter.JwtAuthorizationFilter;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

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
                .antMatchers("/refresh").authenticated()
/*                .antMatchers("/client/**").hasRole(ROLE_USER.getName())
                .antMatchers("/post/**").hasAuthority(AuthorityType.POST.toString())*/
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenProvider));
        http.logout()
                .logoutUrl("/perform-logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }
}
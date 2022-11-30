package by.kharchenko.intexsoftproject.config;

import by.kharchenko.intexsoftproject.filter.JwtAuthorizationFilter;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import static by.kharchenko.intexsoftproject.model.entity.RoleType.ROLE_ADMINISTRATOR;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().
                authorizeRequests()
                .antMatchers("/user/**", "/product/**").authenticated()
                .antMatchers("/generate-card-number/**", "/create-card/**").hasRole(ROLE_ADMINISTRATOR.getName())
 /*               .antMatchers("/post/**").hasAuthority(AuthorityType.POST.toString())*/
                .and()
                .anonymous().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider, authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http.logout()
                .logoutUrl("/perform-logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }
}
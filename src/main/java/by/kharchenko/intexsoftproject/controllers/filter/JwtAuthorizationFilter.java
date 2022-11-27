package by.kharchenko.intexsoftproject.controllers.filter;

import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import by.kharchenko.intexsoftproject.security.JwtType;
import by.kharchenko.intexsoftproject.util.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String ACCESS_TOKEN = "Access-Token";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(ACCESS_TOKEN);
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken, JwtType.ACCESS)) {
            try {
                JwtAuthentication auth = JwtUtils.generate(jwtTokenProvider.getAccessClaims(accessToken));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        chain.doFilter(request, response);
    }
}
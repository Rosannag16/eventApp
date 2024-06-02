
package com.eventapp.security;

import com.eventapp.exception.ResourceNotFoundException;
import com.eventapp.model.User;
import com.eventapp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    @Override
    protected  void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            throw  new ResourceNotFoundException("La richiesta non ha un token. Riloggarsi!");
        }

        String token = authHeader.substring(7);

        jwtTool.verifyToken(token);

        int username = jwtTool.getUsername(token);

        Optional <User> userOptional = userService.getUser(username);

        if(userOptional.isPresent()){
            User user= userOptional.get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else{
            throw new ResourceNotFoundException("Dipendente non trovato nel token. Riloggati");
        }

        filterChain.doFilter(request,response);
    }

}
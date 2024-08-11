package com.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.service.JWTUtils;
import com.service.OurUserDetails;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	  @Autowired
	    private JWTUtils jwtUtils;

	    @Autowired
	    private OurUserDetails ourUserDetails;

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws ServletException, IOException {
			// TODO Auto-generated method stub

	        final String authHeader = request.getHeader("Authorization");
	        final String jwtToken;
	        final String userEmail;

	        if (authHeader == null || authHeader.isBlank()) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        jwtToken = authHeader.substring(7);
	        userEmail = jwtUtils.extractUsername(jwtToken);

	        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails userDetails = ourUserDetails.loadUserByUsername(userEmail);

	            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
	                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
	                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities()
	                );
	                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                securityContext.setAuthentication(token);
	                SecurityContextHolder.setContext(securityContext);
	            }
	        }
	        filterChain.doFilter(request, response);
			
		}
}

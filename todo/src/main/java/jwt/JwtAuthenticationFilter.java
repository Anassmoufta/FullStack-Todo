package jwt;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
   
    @Bean
    JwtServices jwtServices() {
    	return new JwtServices();
    }
    
    @Autowired
    UserDetailsService userDetailsService;

    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
		
			final String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Bearer ")) {
		
				final String token = authorization.substring(7);
				final Claims claims = jwtServices().getClaims(token);
				if (claims.getExpiration().after(new Date())) {

					final String username = claims.getSubject();
					final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					final UsernamePasswordAuthenticationToken authToken =
							new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
				
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}

		}
		
		filterChain.doFilter(request, response);
	}

}

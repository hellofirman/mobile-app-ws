package com.hellofirman.mobileappws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {
	
	public AuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, 
			HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		String header = req.getHeader(SecurityConstans.HEADER_STRING);
		
		if(header == null || !header.startsWith(SecurityConstans.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(req, res);
		
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String token = req.getHeader(SecurityConstans.HEADER_STRING);
		
		if(token != null) {
			token = token.replace(SecurityConstans.TOKEN_PREFIX, "");
			
			String user = Jwts.parser()
					.setSigningKey(SecurityConstans.getTokenSecret())
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			if(user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			
			return null;
			
		}
		
		return null;
	}

}

package com.programsji.authenticationprovider;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.programsji.bo.User;
import com.programsji.service.UserService;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	UserService service;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		User user = (User) service.loadUserByUsername(username);
		if (user == null) {
			throw new BadCredentialsException("User Name Not Valid");
		}

		Object obj = authentication.getDetails();

		if (!user.getPassword().equals(password)) {
			throw new BadCredentialsException("Password Not Valid");
		}

		Collection<? extends GrantedAuthority> authorities = user
				.getAuthorities();
		return new UsernamePasswordAuthenticationToken(user, password,
				authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}
}
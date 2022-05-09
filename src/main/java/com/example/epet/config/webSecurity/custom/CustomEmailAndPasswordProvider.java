package com.example.epet.config.webSecurity.custom;

import com.example.epet.service.PetOwnerService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomEmailAndPasswordProvider implements AuthenticationProvider {
    private final PetOwnerService petOwnerService;
    private final PasswordEncoder passwordEncoder;

    public CustomEmailAndPasswordProvider(PetOwnerService petOwnerService, PasswordEncoder passwordEncoder) {
        this.petOwnerService = petOwnerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if ("".equals(username) || "".equals(password)){
            throw new BadCredentialsException("Bad credentials!");
        }

        UserDetails userDetails =this.petOwnerService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Bad Credentials!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}

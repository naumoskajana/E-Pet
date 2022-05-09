package com.example.epet.config.webSecurity;

import com.example.epet.config.webSecurity.custom.CustomEmailAndPasswordProvider;
import com.example.epet.config.webSecurity.oauth2.CustomOAuth2UserService;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.service.PetOwnerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    public WebSecurityConfig(CustomOAuth2UserService oauthUserService, PetOwnerService petOwnerService, CustomEmailAndPasswordProvider provider) {
        this.oauthUserService = oauthUserService;
        this.petOwnerService = petOwnerService;
        this.provider = provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String username = userDetails.getUsername();
                    PetOwner petOwner = (PetOwner) petOwnerService.loadUserByUsername(username);
                    httpServletRequest.getSession().setAttribute("user",petOwner);
                    httpServletResponse.sendRedirect("/lostPets");
                })
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
                    String email = oauthUser.getAttribute("email");
                    String name = oauthUser.getAttribute("name");
                    PetOwner petOwner = petOwnerService.processOAuthPostLogin(email, name);
                    HttpSession session = request.getSession();
                    session.setAttribute("user", petOwner);
                    session.setAttribute("loggedIn",true);
                    response.sendRedirect("/lostPets");
                });
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
    }


    @Autowired
    private final CustomOAuth2UserService oauthUserService;
    private final PetOwnerService petOwnerService;
    private final CustomEmailAndPasswordProvider provider;


}

class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                e.getLocalizedMessage());
    }
}

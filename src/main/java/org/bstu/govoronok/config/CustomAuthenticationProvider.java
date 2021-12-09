package org.bstu.govoronok.config;

import org.bstu.govoronok.model.Role;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        String password = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getCredentials().toString();
        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Invalid login/password details");
        }
        User user = userService.findByUsername(username);
        List<Role> roleList = new ArrayList<>();
        roleList.add(user.getRole());
        if(user != null){
            if(username.equals(user.getEmail()) && passwordEncoder.matches(password, user.getPassword())
                    && user.getApproved().equals(Boolean.TRUE)){
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        new org.springframework.security.core.userdetails.User(username,password, mapRolesToAuthorities(roleList)),
                        password, mapRolesToAuthorities(roleList));
            }
        }
        else{
            throw new UsernameNotFoundException("User " + username + "not found");
        }
        return usernamePasswordAuthenticationToken;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
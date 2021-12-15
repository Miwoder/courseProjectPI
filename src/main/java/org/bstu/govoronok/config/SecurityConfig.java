package org.bstu.govoronok.config;

import org.bstu.govoronok.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public SecurityConfig (UserService userService, PasswordEncoder passwordEncoder, CustomAuthenticationProvider customAuthenticationProvider){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/administration/**").hasAuthority("ADMINISTRATOR")
                .antMatchers("/my/**", "/auctions/**","/items/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/signin").permitAll()
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
}

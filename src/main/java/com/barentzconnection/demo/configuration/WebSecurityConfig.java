package com.barentzconnection.demo.configuration;

import com.barentzconnection.demo.services.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
    public static String ANONYMOUS = "anonymousUser";

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] staticResources  =  {
                "/css/**",
                "/img/**",
        };

        http
                .authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/schedule").permitAll()
                .antMatchers("/schedule/**").permitAll()
                .antMatchers("/contacts").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/register/confirm/**").permitAll()
                .antMatchers("/leaders").permitAll()
                .antMatchers("/admin").hasAuthority(ADMIN)
                .antMatchers("/admin/**").hasAuthority(ADMIN)
                .antMatchers("/admin/event/**").hasAuthority(ADMIN)
                .antMatchers("/admin/links").hasAuthority(ADMIN)
                .antMatchers("/admin/links/**").hasAuthority(ADMIN)
                .antMatchers("/upload/**").hasAuthority(ADMIN)
                .antMatchers("/avatar/**").authenticated()
                .antMatchers("/cabinet/*").authenticated()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/",true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
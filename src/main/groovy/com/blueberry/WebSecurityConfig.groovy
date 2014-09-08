package com.blueberry

import com.blueberry.framework.security.*

import com.blueberry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableWebMvcSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

    @Autowired
    UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.userDetailsService(userService)
    }


    @Configuration
    @Order(1)
    public static class RestWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                        .antMatchers("/api/greeting/**")            .permitAll()
                        .antMatchers("/api/users")                 .permitAll()
                        .antMatchers("/api/login", "/api/logout")   .permitAll()
                        .anyRequest()                               .authenticated()

            http
                    .exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint())
                    .accessDeniedHandler(restAccessDeniedHandler())

            http
                    .formLogin()
                    .loginProcessingUrl("/api/login")
                    .successHandler(restAuthenticationSuccessHandler())
                    .failureHandler(restAuthenticationFailureHandler())
                    .permitAll()

            http
                    .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(restLogoutSuccessHandler())
                    .permitAll()

            http
                    .csrf().disable()                                       // Enabled by default in java config
        }

        public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
            return new RestAuthenticationEntryPoint();
        }

        public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
            RestAuthenticationSuccessHandler handler = new RestAuthenticationSuccessHandler();
            //        handler.setRequestCache(requestCache);
            return handler;
        }

        public RestAuthenticationFailureHandler restAuthenticationFailureHandler() {
            return new RestAuthenticationFailureHandler();
        }

        public RestLogoutSuccessHandler restLogoutSuccessHandler() {
            return new RestLogoutSuccessHandler();
        }

        public RestAccessDeniedHandler restAccessDeniedHandler() {
            return new RestAccessDeniedHandler();
        }
    }

    @Configuration
    @Order(2)
    public static class MvcWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/**")
                    .authorizeRequests()
                        .anyRequest().permitAll()
                    .and()
                        .formLogin();
        }
    }

}

package com.blueberry

import com.blueberry.framework.security.*

import com.blueberry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity

/**
 * Configure spring security for REST and WEB applications
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

    @Autowired
    UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.userDetailsService(userService)
    }

    /**
     * Use this for rest application
     */
    @Configuration
    @Order(1)
    public static class RestWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // Protect using a catch-all authenticated() strategy here. Use fine-grained authenticated in Controllers.
            // List out unprotected resources as well here
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                    .antMatchers("/api/sample").permitAll()
                    .antMatchers("/api/login", "/api/logout").permitAll()
                    .anyRequest().authenticated()

            // Configure REST friendly entry-point and access denied handler
            http
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .accessDeniedHandler(restAccessDeniedHandler())

            // Configure REST friendly authenticate success and failure handler
            http
                .formLogin()
                .loginProcessingUrl("/api/login")
                .successHandler(restAuthenticationSuccessHandler())
                .failureHandler(restAuthenticationFailureHandler())
                .permitAll()

            // Configure REST friendly logout success handler
            http
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(restLogoutSuccessHandler())
                .permitAll()

            // Java config enabled CSRF protection by default. Disable it here.
            http
                .csrf().disable()
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

    /**
     * Use this for web application
     */
    @Configuration
    @Order(2)
    public static class MvcWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // For anything other than /api/** consider the resource protected.
            http
                .antMatcher("/**")
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .formLogin();
        }
    }

}

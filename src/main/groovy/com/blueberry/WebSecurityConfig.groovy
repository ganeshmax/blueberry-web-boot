package com.blueberry

import com.blueberry.framework.security.*
import com.blueberry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint

import static com.blueberry.model.dto.ClientType.*
import static com.blueberry.model.dto.OauthFlowType.*
import static com.blueberry.model.dto.OauthScopeType.*
import static com.blueberry.util.Constants.APPLICATION_NAME

/**
 * Configure spring security for REST and WEB and API modules
 * Found by the app because of ComponentScan on ApplicationConfig.
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

    @Autowired
    UserService userService;

    /**
     * Configure a global authentication manager which will be used by web, rest and api modules.
     * The reason is that, this system's users are always the same with the same credentials regardless of whether
     * a web client, an api client or rest client accesses it. So, the same authentication manager can be used
     * across all the modules as long as it is for user authentication
     *
     * @param auth
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.userDetailsService(userService)
    }

    /**
     * In general, when we build the authenticationManager globally using the builder, it is set globally and used by
     * other HTTP endpoints. In our example, both the web and rest endpoints will use it.
     *
     * But, since Oauth config needs 2 authentication managers, one for client and one for users it won't know
     * which one to pick for what. Not only that, there is also a convention that the password flow is enabled only
     * when the user auth-manager is set explicitly. Otherwise, "password" flow remains disabled. This sounds logical
     * because, for authorization_code and implicit flow, the authorization server need not perform authentication at
     * all. It is performed by the regular web app. So, in those cases, the authorization server doesn't even need to
     * know about the authentication manager.
     *
     * So, we specify client auth-manager separately by specifying an in-memory client-details service.
     * But, then we will have to specify the user auth-manager. That cannot be done, unless we
     * expose it to the container as a bean. This method makes the global auth-manager available as a spring bean. We
     * can then Autowire this in the Oauth config class and set it.
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Use this for rest application
     */
    @Configuration
    @Order(1)
    public static class CookieBasedRestSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // Protect using a catch-all authenticated() strategy here. Use fine-grained authenticated in Controllers.
            // List out unprotected resources as well here
            http
                .antMatcher("/rest/**")
                .authorizeRequests()
                    .antMatchers("/rest/sample/**").permitAll()
                    .antMatchers("/rest/login", "/rest/logout").permitAll()
                    .anyRequest().authenticated()

            // Configure REST friendly entry-point and access denied handler
            http
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .accessDeniedHandler(restAccessDeniedHandler())

            // Configure REST friendly authenticate success and failure handler
            http
                .formLogin()
                .loginProcessingUrl("/rest/login")
                .successHandler(restAuthenticationSuccessHandler())
                .failureHandler(restAuthenticationFailureHandler())
                .permitAll()

            // Configure REST friendly logout success handler
            http
                .logout()
                .logoutUrl("/rest/logout")
                .logoutSuccessHandler(restLogoutSuccessHandler())
                .permitAll()

            // Java config enabled CSRF protection by default. Disable it here.
            http
                .csrf().disable()
        }

        @Bean
        public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
            return new RestAuthenticationEntryPoint();
        }

        @Bean
        public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
            RestAuthenticationSuccessHandler handler = new RestAuthenticationSuccessHandler();
            //        handler.setRequestCache(requestCache);
            return handler;
        }

        @Bean
        public RestAuthenticationFailureHandler restAuthenticationFailureHandler() {
            return new RestAuthenticationFailureHandler();
        }

        @Bean
        public RestLogoutSuccessHandler restLogoutSuccessHandler() {
            return new RestLogoutSuccessHandler();
        }

        @Bean
        public RestAccessDeniedHandler restAccessDeniedHandler() {
            return new RestAccessDeniedHandler();
        }
    }

    /**
     * Configures the resource server. Read authorization server first.
     *
     * Here, we try to protect the resource server using oauth. So, the first thing we do is configure it to be
     * protected for specific url patterns (like /api/**). But what happens if the client accesses the url without
     * an access_token. Since an oauthAuthenticationEntryPoint is specified, spring sends back a 401 instead of
     * redirecting the user back to the login page. Otherwise a login page will be served and that would be wrong.
     *
     * If you did not protect specific patterns using http builder, then ss will automatically protect every other URL
     * that was not protected except those that are explicitly ignore()d and ones exposed by the authorization server
     * (like /oauth/authorize, /oauth/token etc.). In this case, we may not have to specify the entry-point ourselves
     * because even that would be automatic. But since, we do customize protection, we will have to set the entry-point
     * or that is what i think.
     *
     * This should tell the client that they are not authorized to access the resource. This means that the client
     * can first go to the authorization server and try to authenticate (get an access token) before coming back and
     * trying to access the resource. The client may do this by redirecting the user to the authorize or token urls
     * depending on whether the client is trusted or not.
     *
     * A trusted client will ask the user for username and password and send them to /oauth/token flow.
     * An untrusted client will not have permission to perform the above "password" flow. So, they will redirect the
     * user to the /oauth/authorize flow where the oauth dance begins and finally results in an access token.
     *
     * Regardless of the path taken by the client, the result is an access_token. Then the client can use this
     * access_token to access the resource here.
     *
     * Note: Basically, this will allow only requests with valid access_token. Otherwise it will send a 401. The client
     * has to go to the auth server to deal with login and get the access_token. Client cannot do those things here.
     *
     * TODO: Configure other items like the entryPoint. There could be other items that lead to a form-login scenario
     * instead of a proper oauth scenario
     *
     */
    @Configuration
    @Order(2)
    @EnableResourceServer
    public static class OauthBasedRestSecurityResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(APPLICATION_NAME)
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                    .anyRequest().authenticated()

            http.exceptionHandling()
                .authenticationEntryPoint(new OAuth2AuthenticationEntryPoint())
        }
    }

    /**
     * Configures the authorization server.
     *
     * At present only "password" flow is enabled. BTW, it is enabled automatically when we configure an authentication
     * manager.
     *
     * /oauth/authorize: Neither protected nor handled by spring
     * -----------------------------------------------------------------------------------------------------------------
     * In general, an oauth flow starts with an /oauth/authorize request. This request is supposed to be protected by
     * the app's web form-login authentication. spring doesn't configure anything for this URL by default. So, the
     * web will use its form-login and its authenticationManager to authenticate the user before allowing access to
     * this request. Once the user logs-in, they will be shown the authorize page where the user clicks on
     * an "authorize" button. I think this page has to be provided by us. Spring security doesn't do much here.
     * This results in a request to /oauth/confirm_access URL.
     *
     * /oauth/confirm_access: Not protected by spring, but handled by spring
     * -----------------------------------------------------------------------------------------------------------------
     * Again, this url is protected by the app's web form-login authentication. This url will be handled by spring
     * and spring will redirect the user to the client with auth-code.
     *
     * /oauth/token: Protected by spring for auth_code and client_id. No client creds required as long as auth_code
     * -----------------------------------------------------------------------------------------------------------------
     * The client will then initiate a call to this url with the auth_code. spring will handle this and return the
     * access_token. TODO: get into the details of this here
     *
     * /oauth/token - password: Protected by spring for client creds and user creds
     * -----------------------------------------------------------------------------------------------------------------
     * For the password flow, the authorize, confirm_access etc. is not needed. Only a direct request to this URL is
     * required. But, the client will send 2 sets of credentials.
     * 1. client credentials in Basic auth header
     * 2. user credentials in form body
     *
     * Spring will configure the filters required for this, but it needs 2 authentication managers to manage this. In
     * general, the clients are setup in-memory. So, by providing an inMemory() clientDetailsService, we implicitly
     * give spring an authentication manager to deal with #1. Next, since the user credentials need the same
     * authentication mechanism used by web, because the user is the same, we can provide an instance of regular web
     * authentication manager to deal with #2. #2 is mandatory only for "password" flow and this flow gets enabled
     * implicitly when it is set
     *
     * Once we give that, spring will authenticate the client first, the user next and offer the access token back. Now,
     * the client can use the access token to request the resource server.
     *
     * Note: If the client was thrown a 401 with resource server, the client should come here and follow either the
     * auth-code or implicit or password (only this is supported) flow here to get the access_token. Then they can go
     * back to the resource server to get access to resources.
     *
     * This discussion is continued in the @EnableResourceServer
     *
     */
    @Configuration
    @Order(3)
    @EnableAuthorizationServer
    public static class OauthBasedRestSecurityAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        }

        /**
         * This configures the in-memory clients, thereby providing the authorization with an implicit authentication
         * manager for clients for oauth authorization server
         * @param clients
         * @throws Exception
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                        .withClient(ANDROID.clientId)
                            .secret(ANDROID.clientSecret)
                            .resourceIds(APPLICATION_NAME)
                            .authorizedGrantTypes(PASSWORD.name, REFRESH_TOKEN.name)
                            .scopes(READ.name, WRITE.name, TRUST.name)
                            .authorities("ROLE_TRUSTED_CLIENT")
                    .and()
                        .withClient(IOS.clientId)
                            .secret(IOS.clientSecret)
                            .resourceIds(APPLICATION_NAME)
                            .authorizedGrantTypes(PASSWORD.name, REFRESH_TOKEN.name)
                            .scopes(READ.name, WRITE.name, TRUST.name)
                            .authorities("ROLE_TRUSTED_CLIENT")
                    .and()
                        .withClient(EXTERNAL.clientId)
                            .secret(EXTERNAL.clientSecret)
                            .resourceIds(APPLICATION_NAME)
                            .authorizedGrantTypes(AUTHORIZATION_CODE.name, IMPLICIT.name, REFRESH_TOKEN.name)
                            .scopes(READ.name, WRITE.name)
                            .authorities("ROLE_CLIENT")
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(authenticationManager)

        }
    }

    /**
     * Use this for web application
     *
     * TODO: Complete this with login, logout and accessing a page
     */
    @Configuration
    @Order(4)
    public static class CookieBasedWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // For anything other than /api/** consider the resource protected.
            http
                    .antMatcher("/**")
                        .authorizeRequests()
                            .antMatchers("/oauth/token").permitAll()
                            .anyRequest().authenticated()
                    .and()
                        .formLogin()
                    .and()
                        .csrf().disable()

        }
    }
}

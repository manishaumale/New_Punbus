package com.idms.base.configuration;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@Import({KeycloakSpringBootConfigResolver.class})
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
		auth.authenticationProvider(keycloakAuthenticationProvider);
	}

	@Bean
	@Override
	protected NullAuthenticatedSessionStrategy sessionAuthenticationStrategy() {
		return new NullAuthenticatedSessionStrategy();
	}

//	@Bean
//	public KeycloakConfigResolver keycloakConfigResolver() {
//		return new KeycloakSpringBootConfigResolver();
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http
		.csrf().disable()
        .requestMatchers()
        .and()
        .authorizeRequests()
        .antMatchers("/actuator/**",  "/api-docs/**","/oauth/*", "/api/v1/user/forgetPassward",
        		"/api/v1/user/resetPassward","/api/v1/user/verifiedOtp", "/api/v1/user/generateOtp/*", "/api/v1/organization/generateOtp/*",
        		"/api/v1/state/state/**","/api/v1/keycloak/getLogin","/api/v1/keycloak/refreshToken/**","/api/v1/trip/*","/api/v1/permit/**","/api/v1/route/*","/api/v1/route/**","/api/v1/trip/*","/api/v1/trip/**"        		).permitAll()
        .antMatchers("/api/**" ).authenticated();
		http.cors();
	}

}

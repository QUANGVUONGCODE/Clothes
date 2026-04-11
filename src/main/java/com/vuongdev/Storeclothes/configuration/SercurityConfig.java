package com.vuongdev.Storeclothes.configuration;

import com.vuongdev.Storeclothes.enums.RolePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SercurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SercurityConfig.class);
    private static final String api = "/api/v1";


    private final String[] publicEntrypoint = {
            api + "/auth/log-in",
            api +"/auth/introspect",
            api +"/auth/refresh",
            api +"/auth/logout"
    };

    @Autowired
    private CustomJwtDecoder jwtDecoder;


    private static final String userEntryPoint = "/users";
    private static final String productVariant = "/product-variants";
    private static final String product = "/products";
    private static final String paymentEntryPoint = "/payments";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        log.info("=== Security Filter Chain Debug ===");
        log.info("Public endpoints: {}", (Object) java.util.Arrays.toString(publicEntrypoint));
        log.info("User endpoint: {}", api + userEntryPoint + "/myInfo");
        log.info("Required roles for user info: ADMIN, USER");
        
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, publicEntrypoint)
                        .permitAll()

                        //product
                        .requestMatchers(HttpMethod.POST, api + product)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + product + "/**")
                        .permitAll()


                        //user
                        .requestMatchers(HttpMethod.POST, api + userEntryPoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + userEntryPoint + "/myInfo")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())



                        //product Variant
                        .requestMatchers(HttpMethod.POST, api + productVariant)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + productVariant)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + productVariant + "/by-ids" + "/**")
                        .permitAll()



                        //Payment
                        .requestMatchers(HttpMethod.POST, api + paymentEntryPoint)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + paymentEntryPoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + paymentEntryPoint +"/{id}")
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT, api + paymentEntryPoint +"/{id}")
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, api + paymentEntryPoint +"/{id}")
                        .hasRole(RolePlay.ADMIN.name())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .csrf(AbstractHttpConfigurer::disable);
        log.info("Security Filter Chain configured successfully");
        log.info("==================================");
        return httpSecurity.build();
    }



    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        log.info("=== JWT Authentication Converter Debug ===");
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        log.info("Authority prefix set to: ROLE_");
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        log.info("JWT Authentication Converter configured");
        log.info("========================================");
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    // password encoder bean moved to PasswordConfig to avoid circular dependency
}

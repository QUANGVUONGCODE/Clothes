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
            api +"/auth/logout",
            api +"/payments/create_payment_url"
    };

    @Autowired
    private CustomJwtDecoder jwtDecoder;


    private static final String userEntryPoint = "/users";
    private static final String productVariant = "/product-variants";
    private static final String product = "/products";
    private static final String paymentEntryPoint = "/payments";
    private static final String orderEntryPoint = "/orders";
    private static final String orderDetailEntryPoint = "/order-details";
    private static final String productImageEntryPoint = "/product-images";
    private static final String departmentEntryPoint = "/departments";
    private static final String categoryEntryPoint = "/categories";
    private static final String subCategoryEntryPoint = "/sub-categories";
    private static final String subCategoryImageEntryPoint = "/sub-category-images";
    private static final String dashboardEntryPoint = "/dashboard";
    private static final String colorEntryPoint = "/colors";
    private static final String sizeEntryPoint = "/sizes";
    private static final String chatEntryPoint = "/chat";
    private static final String reviewEntryPoint = "/reviews";
    private static final String invoiceEntryPoint = "/invoices";

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
                        .requestMatchers(HttpMethod.PUT, api + product + "/{id}")
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + product + "/{id}"    )
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + product + "/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, api + product + "/{id}")
                        .hasRole(RolePlay.ADMIN.name())


                        //user
                        .requestMatchers(HttpMethod.POST, api + userEntryPoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + userEntryPoint + "/myInfo")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())



                        //product Variant
                        .requestMatchers(HttpMethod.POST, api + productVariant)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + productVariant + "/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + productVariant + "/by-ids" + "/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT, api + productVariant + "/**")
                        .hasRole(RolePlay.ADMIN.name())



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

                        .requestMatchers(HttpMethod.POST, api + paymentEntryPoint +"/create_payment_url")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.POST, api + paymentEntryPoint +"/refund")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.POST, api + paymentEntryPoint +"/query")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.POST, api + paymentEntryPoint + "/result/{txnRef}")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api + paymentEntryPoint + "/return")
                        .permitAll()
                        //Order
                        .requestMatchers(HttpMethod.GET, api + orderEntryPoint + "/**")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.POST, api + orderEntryPoint)
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.PUT, api + orderEntryPoint + "/{id}")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.DELETE, api + orderEntryPoint + "/{id}")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api + orderDetailEntryPoint + "/**")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.PUT, api + orderEntryPoint + "/**")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())

                        .requestMatchers(HttpMethod.GET, api + orderEntryPoint + "/code-phone")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())



                        //orderDetail
                        .requestMatchers(HttpMethod.POST, api + orderDetailEntryPoint)
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.DELETE, api + orderDetailEntryPoint + "/{id}")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api + orderDetailEntryPoint + "/{id}")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.PUT, api + orderDetailEntryPoint + "/{id}")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api+ orderDetailEntryPoint + "/**")
                        .hasAnyRole(RolePlay.ADMIN.name(), RolePlay.USER.name())


                        //product-image
                        .requestMatchers(HttpMethod.GET, api + productImageEntryPoint + "/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, api + productImageEntryPoint + "/search-by-image")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, api + productImageEntryPoint + "/**")
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, api + productImageEntryPoint + "/**" )
                        .hasRole(RolePlay.ADMIN.name())


                        //department
                        .requestMatchers(HttpMethod.POST, api + departmentEntryPoint)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + departmentEntryPoint + "/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, api + departmentEntryPoint + "/{id}")
                        .hasRole(RolePlay.ADMIN.name())


                        //category
                        .requestMatchers(HttpMethod.POST, api + categoryEntryPoint)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + categoryEntryPoint + "/**")
                        .permitAll()


                        //sub category
                        .requestMatchers(HttpMethod.POST, api + subCategoryEntryPoint)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + subCategoryEntryPoint + "/**")
                        .permitAll()


                        //sub category image
                        .requestMatchers(HttpMethod.POST, api + subCategoryImageEntryPoint + "/upload" + "/**")
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + subCategoryImageEntryPoint + "/**")
                        .permitAll()

                        //dashboard
                        .requestMatchers(HttpMethod.GET, api + dashboardEntryPoint + "/**")
                        .hasRole(RolePlay.ADMIN.name())

                        //color

                        .requestMatchers(HttpMethod.GET, api + colorEntryPoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, api + colorEntryPoint)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, api + colorEntryPoint + "/**")
                        .permitAll()


                        //size

                        .requestMatchers(HttpMethod.GET, api + sizeEntryPoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, api + sizeEntryPoint)
                        .hasRole(RolePlay.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, api + sizeEntryPoint + "/{id}")
                        .hasRole(RolePlay.ADMIN.name())



                        //review
                        .requestMatchers(HttpMethod.POST, api + reviewEntryPoint)
                        .hasRole(RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api + reviewEntryPoint + "/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT, api + reviewEntryPoint + "/{id}")
                        .hasRole(RolePlay.USER.name())
                        .requestMatchers(HttpMethod.DELETE, api + reviewEntryPoint + "/{id}")
                        .hasRole(RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api + reviewEntryPoint + "/user/{userId}")
                        .hasRole(RolePlay.USER.name())
                        .requestMatchers(HttpMethod.GET, api + reviewEntryPoint + "/product/{productId}/summary")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, api + reviewEntryPoint + "/one")
                        .hasRole(RolePlay.USER.name())

                        //Chat

                        .requestMatchers(HttpMethod.POST, api + chatEntryPoint)
                        .permitAll()

                        //Invoice

                        .requestMatchers(HttpMethod.GET, api + invoiceEntryPoint + "/order-code/{orderCode}")
                        .hasAnyRole(RolePlay.USER.name(), RolePlay.ADMIN.name())

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
        corsConfiguration.addAllowedOrigin("http://localhost:5177");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    // password encoder bean moved to PasswordConfig to avoid circular dependency
}

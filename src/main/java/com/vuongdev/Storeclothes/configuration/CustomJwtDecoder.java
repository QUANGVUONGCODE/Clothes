package com.vuongdev.Storeclothes.configuration;

import com.nimbusds.jose.JOSEException;
import com.vuongdev.Storeclothes.dto.request.requestService.IntrospectRequest;
import com.vuongdev.Storeclothes.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    private static final Logger log = LoggerFactory.getLogger(CustomJwtDecoder.class);
    
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException{
        log.info("=== JWT Decode Debug ===");
        log.info("Token: {}", token.substring(0, Math.min(token.length(), 50)) + "...");
        
        try {
            var response = authenticationService.introspectResponse(
                    IntrospectRequest.builder()
                            .token(token)
                            .build()
            );
            log.info("Token valid: {}", (Object) response.isValid());
            if(!response.isValid()){
                throw new JwtException("Invalid Token " + token);
            }
        }catch (JOSEException |ParseException e){
            log.error("Token introspection error: {}", e.getMessage());
            throw new JwtException(e.getMessage());
        }
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        Jwt jwt = nimbusJwtDecoder.decode(token);
        log.info("JWT Claims: {}", (Object) jwt.getClaims());
        log.info("JWT Scopes: {}", (Object) jwt.getClaim("scope"));
        log.info("JWT Authorities: {}", (Object) jwt.getClaimAsString("authorities"));
        log.info("JWT Subject: {}", (Object) jwt.getSubject());
        log.info("====================");
        return jwt;
    }
}

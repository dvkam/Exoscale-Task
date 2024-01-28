package org.exoscale.openapi;

import lombok.RequiredArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;


class Authorization {

    private final String apiKey;
    private final Mac mac;

    public Authorization(String apiKey, String apiSecret) throws InvalidKeyException {
        try {
            this.mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        };
        SecretKeySpec secretKeySpec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        this.apiKey = apiKey;
    }


    public String getAuthorizationHeader(HttpMethod httpMethod, String path, String queryParameters, Optional<String> requestBody, Optional<String> requestHeaders){
        return createAuthorizationHeader(httpMethod, path, queryParameters, requestBody.orElse(""), "");
    }

    private String createAuthorizationHeader(HttpMethod httpMethod, String path, String queryParameters, String requestBody, String requestHeaders){
        var expirationTimestamp = Instant.now().getEpochSecond() + 300;

        var messageToSign = String.format("%s %s\n%s\n%s\n%s\n%s",
                httpMethod.name(), path, requestBody, queryParameters, requestHeaders, expirationTimestamp);

        return String.format("EXO2-HMAC-SHA256 credential=%s,signed-query-args=%s,expires=%d,signature=%s",
                apiKey, queryParameters.replaceAll(";", ","), expirationTimestamp, generateSignature(messageToSign));
    }


    private String generateSignature(String message) {
        var signatureBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}

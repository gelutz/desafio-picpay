package com.lutzapi.application.gateways;

import com.lutzapi.application.interfaces.ApiGateway;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Getter
@RequiredArgsConstructor
@Component
public class MockyGateway implements ApiGateway {
    private final RestTemplate template;

    @Value("${mocky.url}")
    private String url;

    @Override
    @SuppressWarnings("unchecked")
    public APIGatewayDTO call() {
        ResponseEntity<APIGatewayDTO> response = template.getForEntity(url, APIGatewayDTO.class);
        if (response == null || response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new MockyDefaultExceptin("Erro na transação");
        }

        if (!response.getBody().message().equals("Autorizado")) {
            throw new MockyAuthException("Você não está autorizado");
        }

        return response.getBody();
    }
}

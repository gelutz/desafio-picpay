package com.lutzapi.application.adapters;

import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.interfaces.ApiAdapter;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Getter
@Setter
@AllArgsConstructor
@Component
public class MockyAdapter implements ApiAdapter {
    final String url = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";
    private RestTemplate template;

    @Override
    @SuppressWarnings("unchecked")
    public MockyTransactionDTO call() {
        ResponseEntity<MockyTransactionDTO> response = template.getForEntity(url, MockyTransactionDTO.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new MockyDefaultExceptin("Erro na transação");
        }

        if (!response.getBody().message().equals("Autorizado")) {
            throw new MockyAuthException("Você não está autorizado");
        }

        return response.getBody();
    }
}

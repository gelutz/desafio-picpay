package com.lutzapi.infrastructure.adapters;

import com.lutzapi.infrastructure.dtos.MockyTransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@Getter
@Setter
@AllArgsConstructor
public class MockyAdapter implements ApiAdapter {
    final String url = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";
    final RestTemplate template = new RestTemplate();

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public ResponseEntity<MockyTransactionDTO> call() {
        return template.getForEntity(url, MockyTransactionDTO.class);
    }
}

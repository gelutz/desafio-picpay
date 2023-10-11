package com.lutzapi.application.gateways;

import com.lutzapi.application.interfaces.ApiGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class FakeGateway implements ApiGateway {

    @Override
    @SuppressWarnings("unchecked")
    public APIGatewayDTO call() {
        return new APIGatewayDTO("Autorizado");
    }
}

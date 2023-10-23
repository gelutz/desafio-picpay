package com.lutzapi.application.gateways;

import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.infrastructure.repositories.RestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class MockyAdapterTest {
    private MockyGateway sut;
    @Mock
    private RestTemplate templateMock;

    @BeforeEach
    public void setUp() {
        sut = new MockyGateway(templateMock);
    }

    @Test
    @DisplayName("Caso dê algum problema com a Mocky API e não retornar status code 200")
    public void validationShouldThrowIfErrorInTransaction(){
        APIGatewayDTO mockedDTO = new APIGatewayDTO("Mocked message");
        ResponseEntity<APIGatewayDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.BAD_REQUEST);

        when(templateMock.getForEntity(sut.getUrl(), APIGatewayDTO.class))
                        .thenReturn(mockedResponseEntity);

        assertThrows(MockyDefaultExceptin.class, () -> sut.call());
    }

    @Test
    @DisplayName("Deve lançar o erro MockyAuthException se não houver 'Autorizado' no response")
    public void validationShouldThrowIfNotAuthorized(){
        APIGatewayDTO mockedDTO = new APIGatewayDTO("Mocked message");
        ResponseEntity<APIGatewayDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.OK);

        when(templateMock.getForEntity(sut.getUrl(), APIGatewayDTO.class))
                .thenReturn(mockedResponseEntity);

        assertThrows(MockyAuthException.class, () -> sut.call());
    }
}

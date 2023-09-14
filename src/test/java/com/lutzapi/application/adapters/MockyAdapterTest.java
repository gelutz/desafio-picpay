package com.lutzapi.application.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.infrastructure.repositories.templates.RestTemplate;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class MockyAdapterTest {
    private MockyAdapter sut;
    @Mock
    private RestTemplate templateMock;

    @BeforeEach
    public void setUp() {
        sut = new MockyAdapter(templateMock);
    }

    @Test
    @DisplayName("Caso dê algum problema com a Mocky API e não retornar status code 200")
    public void validationShouldThrowIfErrorInTransaction(){
        MockyTransactionDTO mockedDTO = new MockyTransactionDTO("Mocked message");
        ResponseEntity<MockyTransactionDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.BAD_REQUEST);

        when(templateMock.getForEntity(sut.getUrl(), MockyTransactionDTO.class))
                        .thenReturn(mockedResponseEntity);

        assertThrows(MockyDefaultExceptin.class, () -> sut.call());
    }

    @Test
    @DisplayName("Deve lançar o erro MockyAuthException se não houver 'Autorizado' no response")
    public void validationShouldThrowIfNotAuthorized(){
        MockyTransactionDTO mockedDTO = new MockyTransactionDTO("Mocked message");
        ResponseEntity<MockyTransactionDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.OK);

        when(templateMock.getForEntity(sut.getUrl(), MockyTransactionDTO.class))
                .thenReturn(mockedResponseEntity);

        assertThrows(MockyAuthException.class, () -> sut.call());
    }
}

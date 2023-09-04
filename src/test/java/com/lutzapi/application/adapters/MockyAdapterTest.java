package com.lutzapi.application.adapters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class MockyAdapterTest {

    @BeforeAll
    static void createMocks() {

    }

    @Test
    public void defaultTest(){

    }
}

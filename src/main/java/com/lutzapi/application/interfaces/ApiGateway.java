package com.lutzapi.application.interfaces;

public interface ApiGateway {
    <T extends ApiDTO> T call();
}

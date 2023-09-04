package com.lutzapi.application.interfaces;

public interface ApiAdapter {
    <T extends ApiDTO> T call();
}

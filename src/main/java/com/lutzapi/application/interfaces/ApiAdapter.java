package com.lutzapi.application.interfaces;

import com.lutzapi.application.interfaces.ApiDTO;
import org.springframework.http.ResponseEntity;

public interface ApiAdapter {
    String getUrl();
    <T extends ApiDTO> ResponseEntity<T> call();
}

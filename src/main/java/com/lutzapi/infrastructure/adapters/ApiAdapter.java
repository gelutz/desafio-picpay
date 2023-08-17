package com.lutzapi.infrastructure.adapters;

import com.lutzapi.infrastructure.dtos.ApiDTO;
import org.springframework.http.ResponseEntity;

public interface ApiAdapter {
    String getUrl();
    <T extends ApiDTO> ResponseEntity<T> call();
}

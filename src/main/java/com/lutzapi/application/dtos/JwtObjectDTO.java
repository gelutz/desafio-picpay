package com.lutzapi.application.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtObjectDTO {
	private String email;
	private String tenantId;
	private String accessKey;
}

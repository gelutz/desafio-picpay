package com.lutzapi.application.config.tenant;

import com.lutzapi.domain.entities.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TenantContext {

	@Value("lutzapi.default-tenant")
	private static String defaultTenant;

	@Setter
	@Getter
	private static String currentTenant = defaultTenant;
}

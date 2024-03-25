package com.lutzapi.application.config.tenant;

import lombok.NonNull;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantResolver implements CurrentTenantIdentifierResolver {
	public static final String DEFAULT_SCHEMA = "public";

	@Override
	@NonNull
	public String resolveCurrentTenantIdentifier() {
		return TenantContext.getCurrentTenant() != null
				? TenantContext.getCurrentTenant()
				: DEFAULT_SCHEMA;
	}
	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}

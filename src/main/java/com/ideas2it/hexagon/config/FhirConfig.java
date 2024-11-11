package com.ideas2it.hexagon.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {
    private static final String SERVER = "https://hapi.fhir.org/baseR4";
    private static final FhirContext fhirContext;
    private static final IGenericClient client;

    static {
        fhirContext = FhirContext.forR4();
        client = fhirContext.newRestfulGenericClient(SERVER);
    }

    @Bean
    public static IGenericClient getClient() {
        return client;
    }
}

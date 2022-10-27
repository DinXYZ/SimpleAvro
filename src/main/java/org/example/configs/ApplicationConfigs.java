package org.example.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfigs {

    @Bean
    public Storage getStorage() {
        return StorageOptions.getUnauthenticatedInstance().getService();
    }

    @Bean
    public CredentialsProvider getCredentialsProvider() {
        return NoCredentialsProvider.create();
    }

    @Bean
    public ObjectMapper getMapper() {
        return new ObjectMapper();
    }
}

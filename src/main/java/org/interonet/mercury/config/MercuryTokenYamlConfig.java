package org.interonet.mercury.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "mercury.token")
public class MercuryTokenYamlConfig {
    private long expireMins;

    public long getExpireMins() {
        return expireMins;
    }

    public void setExpireMins(long expireMins) {
        this.expireMins = expireMins;
    }
}

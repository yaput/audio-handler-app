package com.yaput.audio.handler.audiohandler.metada.model;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetadataConfig {
    @Value("${audio-metadata-mysql-config.url}")
    private String url;
    @Value("${audio-metadata-mysql-config.userName}")
    private String userName;
    @Value("${audio-metadata-mysql-config.password}")
    private String password;

    public MetadataConfig() {

    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }
}
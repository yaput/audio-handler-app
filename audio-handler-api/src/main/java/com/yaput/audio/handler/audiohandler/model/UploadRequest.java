package com.yaput.audio.handler.audiohandler.model;

import java.util.Objects;

public record UploadRequest(Object audioFile) {
    public UploadRequest {
        Objects.requireNonNull(audioFile);
    }
}

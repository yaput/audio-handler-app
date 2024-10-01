package com.yaput.audio.handler.audiohandler.model;

import org.springframework.http.HttpStatus;

public record UploadResponse(String message, HttpStatus status) {
}

package com.yaput.audio.handler.audiohandler.service;


import com.yaput.audio.handler.audiohandler.model.UploadResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AudioHandlerServiceAPI {
    ResponseEntity<UploadResponse> uploadAudioFile(int userId, int phraseId, MultipartFile audioFile);
    ResponseEntity<ByteArrayResource> downloadAudioFile(int userId, int phraseId, String audioFormat);
}

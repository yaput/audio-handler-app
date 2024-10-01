package com.yaput.audio.handler.audiohandler.service;

import com.yaput.audio.handler.audiohandler.model.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioHandlerServiceController implements AudioHandlerServiceAPI{
    private final AudioHandlerServiceAPI delegate;

    @Autowired
    public AudioHandlerServiceController(AudioHandlerServiceAPI delegate) {
        this.delegate = delegate;
    }

    @RequestMapping(value = "/audio/user/{userId}/phrase/{phraseId}", method = RequestMethod.POST)
    @Override
    public ResponseEntity<UploadResponse> uploadAudioFile(@PathVariable int userId, @PathVariable int phraseId, @RequestParam("audio_file") final MultipartFile audioFile) {
        return delegate.uploadAudioFile(userId, phraseId, audioFile);
    }

    @RequestMapping(value = "/audio/user/{userId}/phrase/{phraseId}/{audioFormat}", method = RequestMethod.GET)
    @Override
    public ResponseEntity<ByteArrayResource> downloadAudioFile(@PathVariable int userId, @PathVariable int phraseId, @PathVariable String audioFormat) {
        return delegate.downloadAudioFile(userId, phraseId, audioFormat);
    }
}

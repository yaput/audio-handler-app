package com.yaput.audio.handler.audiohandler.service;

import com.yaput.audio.handler.audiohandler.accessor.AudioObjectAccessor;
import com.yaput.audio.handler.audiohandler.converter.model.ValidFileFormat;
import com.yaput.audio.handler.audiohandler.converter.service.AudioConverterServiceAPI;
import com.yaput.audio.handler.audiohandler.metada.model.AudioMetaData;
import com.yaput.audio.handler.audiohandler.metada.service.AudioMetadataServiceAPI;
import com.yaput.audio.handler.audiohandler.model.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AudioHandlerServiceAPIImpl implements AudioHandlerServiceAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioHandlerServiceAPIImpl.class);
    private static final String FILE_PATH_FORMAT = "%s/%s.wav";

    private final AudioObjectAccessor audioObjectAccessor;
    private final AudioConverterServiceAPI audioConverterServiceAPI;
    private final AudioMetadataServiceAPI audioMetadataServiceAPI;

    @Autowired
    public AudioHandlerServiceAPIImpl(AudioObjectAccessor audioObjectAccessor,
                                      AudioConverterServiceAPI audioConverterServiceAPI,
                                      AudioMetadataServiceAPI audioMetadataServiceAPI) {
        this.audioObjectAccessor = audioObjectAccessor;
        this.audioConverterServiceAPI = audioConverterServiceAPI;
        this.audioMetadataServiceAPI = audioMetadataServiceAPI;
    }

    @Override
    public ResponseEntity<UploadResponse> uploadAudioFile(int userId, int phraseId, MultipartFile audioFile) {
        if (audioFile == null || audioFile.isEmpty()) {
            return ResponseEntity.ok().body(new UploadResponse("Please select a file to upload", HttpStatus.BAD_REQUEST));
        }

        String fileExt = getFileExtension(audioFile.getOriginalFilename());
        if (fileExt == null || !fileExt.equalsIgnoreCase("m4a")) {
            return ResponseEntity.badRequest().body(new UploadResponse("Invalid file format or null", HttpStatus.BAD_REQUEST));
        }

        try {
            AudioMetaData existMetadata = audioMetadataServiceAPI.getMetaDataByUserAndPhraseId(userId, phraseId);
            if (existMetadata.userId() == userId && existMetadata.phraseId() == phraseId) {
                return ResponseEntity.ok().body(new UploadResponse("Duplicate data detected", HttpStatus.BAD_REQUEST));
            }
            String formattedFilePath = String.format(FILE_PATH_FORMAT, userId, phraseId);
            // For production code, I'll prefer to process audio convert async using SQS then process it
            byte[] convertedToWavAudio = audioConverterServiceAPI.convertFile(audioFile.getInputStream(), ValidFileFormat.WAV);
            if (convertedToWavAudio.length > 0) {
                audioObjectAccessor.putAudioObject(formattedFilePath, convertedToWavAudio);
                // Actually we can avoid store filePath since object storage such as S3 can be design
                // using subfolder, thus we can just create a folder structure {userId}/{phraseId}.wav
                // Here we can reduce the number of data being stored
                audioMetadataServiceAPI.insertMetadata(userId, phraseId, formattedFilePath);
            } else {
                return ResponseEntity.ok().body(new UploadResponse("Failed convert audio file", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (Exception e) {
            LOGGER.error("Exception while storing audio file for user id {}, phrase id {}", userId, phraseId, e);
            return ResponseEntity.ok()
                    .body(new UploadResponse("Failed Upload file. Check log for more detail.", HttpStatus.INTERNAL_SERVER_ERROR));

        }
        return ResponseEntity.ok().body(new UploadResponse(String.format("Success upload audio file with name: %s-%s.wav", userId, phraseId), HttpStatus.OK));
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadAudioFile(int userId, int phraseId, String audioFormat) {
        if (!audioFormat.equalsIgnoreCase("m4a")) {
            return ResponseEntity.badRequest().build();
        }
        String filePath = String.format(FILE_PATH_FORMAT, userId, phraseId);
        try {
            InputStream audioFileInputStream = audioObjectAccessor.getAudioObject(filePath);
            if (audioFileInputStream != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + String.format("%s-%s.m4a", userId, phraseId) + "\"")
                        .body(new ByteArrayResource(audioConverterServiceAPI.convertFile(audioFileInputStream, getValidFormat(audioFormat))));
            }
        } catch (Exception e) {
            LOGGER.error("Exception while get audio file for user id {} phrase id {} file path {}",
                    userId, phraseId, filePath, e);
        }
        return ResponseEntity.internalServerError().build();
    }

    private ValidFileFormat getValidFormat(String requestedStringFormat) {
        try {
            return ValidFileFormat.valueOf(requestedStringFormat);
        } catch (Exception _) {
            return ValidFileFormat.M4A;
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}

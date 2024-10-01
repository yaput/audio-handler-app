package com.yaput.audio.handler.audiohandler.service;

import com.yaput.audio.handler.audiohandler.accessor.AudioObjectAccessor;
import com.yaput.audio.handler.audiohandler.converter.service.AudioConverterServiceAPI;
import com.yaput.audio.handler.audiohandler.metada.model.AudioMetaData;
import com.yaput.audio.handler.audiohandler.metada.service.AudioMetadataServiceAPI;
import com.yaput.audio.handler.audiohandler.model.UploadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAudioHandlerServiceAPIImpl {
    @Mock
    private AudioObjectAccessor accessor;
    @Mock
    private AudioConverterServiceAPI converterServiceAPI;
    @Mock
    private AudioMetadataServiceAPI metadataServiceAPI;
    @Mock
    private MultipartFile mockMultipartFile;


    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(mockMultipartFile);
        // Define behavior for the mock object
        Mockito.when(mockMultipartFile.getOriginalFilename()).thenReturn("test-audio.m4a");
        Mockito.when(mockMultipartFile.getContentType()).thenReturn("audio/m4a");
        Mockito.when(mockMultipartFile.getSize()).thenReturn(12345L);

        // Provide a dummy byte array for the file's content
        byte[] content = "Dummy audio file content".getBytes();
        Mockito.when(mockMultipartFile.getBytes()).thenReturn(content);
        Mockito.when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));

        Mockito.when(metadataServiceAPI.getMetaDataByUserAndPhraseId(Mockito.anyInt(), Mockito.anyInt())).thenReturn(
                new AudioMetaData(0, 0, "")
        );
    }

    private AudioHandlerServiceAPI getInstance() {
        return new AudioHandlerServiceAPIImpl(accessor, converterServiceAPI, metadataServiceAPI);
    }

    @Test
    public void uploadAudioFile_allProcessesNoIssue_returnStatusOk() {
        ArgumentCaptor<String> filePath = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> audioFile = ArgumentCaptor.forClass(byte[].class);
        Mockito.doNothing().when(accessor).putAudioObject(filePath.capture(), audioFile.capture());
        Mockito.when(converterServiceAPI.convertFile(Mockito.any(), Mockito.any())).thenReturn("DataDummy".getBytes());
        Mockito.doNothing().when(metadataServiceAPI).insertMetadata(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
        ResponseEntity<UploadResponse> response = getInstance().uploadAudioFile(1,1,mockMultipartFile);
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    public void uploadAudioFile_wrongAudioFormat_returnBadRequest() throws IOException {
        Mockito.reset(mockMultipartFile);
        // Define behavior for the mock object
        Mockito.when(mockMultipartFile.getOriginalFilename()).thenReturn("test-audio.mp3");
        Mockito.when(mockMultipartFile.getContentType()).thenReturn("audio/wav");
        Mockito.when(mockMultipartFile.getSize()).thenReturn(12345L);

        // Provide a dummy byte array for the file's content
        byte[] content = "Dummy audio file content".getBytes();
        Mockito.when(mockMultipartFile.getBytes()).thenReturn(content);
        Mockito.when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        ArgumentCaptor<String> filePath = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> audioFile = ArgumentCaptor.forClass(byte[].class);
        Mockito.doNothing().when(accessor).putAudioObject(filePath.capture(), audioFile.capture());
        Mockito.when(converterServiceAPI.convertFile(Mockito.any(), Mockito.any())).thenReturn("DataDummy".getBytes());
        Mockito.doNothing().when(metadataServiceAPI).insertMetadata(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
        ResponseEntity<UploadResponse> response = getInstance().uploadAudioFile(1,1, mockMultipartFile);
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().status());
    }
}

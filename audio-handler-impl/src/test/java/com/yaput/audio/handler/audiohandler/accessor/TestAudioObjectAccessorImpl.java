package com.yaput.audio.handler.audiohandler.accessor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestAudioObjectAccessorImpl {
    @Mock
    private S3Client s3Client;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private AudioObjectAccessor getInstance() {
        return new AudioObjectAccessorImpl(s3Client, "uploaded_audio");
    }

    @Test
    public void getAudioObject_audioFileNotFound_returnNull() {
        Mockito.when(s3Client.getObjectAsBytes(Mockito.any(GetObjectRequest.class))).thenReturn(null);
        InputStream audioInputStream = getInstance().getAudioObject("1/1.wav");
        assertNull(audioInputStream);
    }

    @Test
    public void getAudioObject_audioFileFound_returnNonEmptyInputStream() throws IOException {
        byte[] expectedAudioData = "Test audio data".getBytes();

        // Mock the ResponseBytes for the object retrieval
        ResponseBytes responseBytes = ResponseBytes.fromByteArray(GetObjectResponse.builder().build(), expectedAudioData);
        Mockito.when(s3Client.getObjectAsBytes(Mockito.any(GetObjectRequest.class))).thenReturn(responseBytes);
        InputStream audioInputStream = getInstance().getAudioObject("1/1.wav");
        assertNotNull(audioInputStream);
        assertTrue(audioInputStream.readAllBytes().length > 0);
    }

    @Test
    public void putAudioObject_successPutObject_noThrowable() {
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class))).thenReturn(
                PutObjectResponse.builder()
                        .versionId("1")
                        .build()
        );
        getInstance().putAudioObject("1/1.wav", "Data File Test".getBytes());
    }
}

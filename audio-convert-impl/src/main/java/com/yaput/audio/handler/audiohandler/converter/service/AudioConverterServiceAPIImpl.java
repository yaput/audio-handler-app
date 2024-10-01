package com.yaput.audio.handler.audiohandler.converter.service;

import com.yaput.audio.handler.audiohandler.converter.model.ConverterConfig;
import com.yaput.audio.handler.audiohandler.converter.model.ValidFileFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.*;

@Service
public class AudioConverterServiceAPIImpl implements AudioConverterServiceAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioConverterServiceAPIImpl.class);
    private final ConverterConfig converterConfig;

    @Autowired
    public AudioConverterServiceAPIImpl(ConverterConfig converterConfig) {
        this.converterConfig = converterConfig;
    }

    @Override
    public byte[] convertFile(InputStream fileInputStream, ValidFileFormat targetFormat) {
        try {
            // Prepare to convert audio using JAVE
            AudioAttributes audio = new AudioAttributes();
            audio.setBitRate(converterConfig.getBitRate());
            audio.setChannels(converterConfig.getChannels());
            audio.setSamplingRate(converterConfig.getSamplingRate());
            audio.setCodec(targetFormat.getCodec());

            // Set encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setAudioAttributes(audio);
            attrs.setOutputFormat(targetFormat.getStringFormat());

            // Use MultimediaObject to read the audio from InputStream
            MultimediaObject multimediaObject = new MultimediaObject(createTempFileFromInputStream(fileInputStream, targetFormat.getStringFormat()));

            // Use ByteArrayOutputStream to capture the converted audio
            File targetFile = File.createTempFile("audio", String.format(".%s", targetFormat.getStringFormat()));
            Encoder encoder = new Encoder();
            encoder.encode(multimediaObject, targetFile, attrs);

            byte[] audioBytes;
            try (FileInputStream fis = new FileInputStream(targetFile);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[converterConfig.getBufferLimit()];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                audioBytes = baos.toByteArray();
            }

            // Clean up the temporary file
            targetFile.delete();

            return audioBytes;
        } catch (Exception e) {
            LOGGER.error("Error occurred during audio conversion", e);
        }
        return new byte[0];
    }

    private File createTempFileFromInputStream(InputStream inputStream, String format) throws IOException {
        File tempFile = File.createTempFile("temp", String.format(".%s", format));
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[converterConfig.getBufferLimit()];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}

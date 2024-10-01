package com.yaput.audio.handler.audiohandler.metadata.service;

import com.yaput.audio.handler.audiohandler.metada.accessor.AudioMetadataAccessor;
import com.yaput.audio.handler.audiohandler.metada.model.AudioMetaData;
import com.yaput.audio.handler.audiohandler.metada.service.AudioMetadataServiceAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AudioMetadataServiceAPIImpl implements AudioMetadataServiceAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioMetadataServiceAPIImpl.class);

    private final AudioMetadataAccessor accessor;

    @Autowired
    public AudioMetadataServiceAPIImpl(AudioMetadataAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public AudioMetaData getMetaDataByUserAndPhraseId(int userId, int phraseId) {
        try {
            return accessor.getMetaDataByUserIdAndPhraseId(userId, phraseId);
        } catch (SQLException e) {
            LOGGER.error("Failed to get metadata by user id {} and phrase id {}", userId, phraseId, e);
        }
        return new AudioMetaData(0, 0, "");
    }

    @Override
    public void insertMetadata(int userId, int phraseId, String filePath) {
        try {
            accessor.insertMetaData(userId, phraseId, filePath);
        } catch (SQLException e) {
            LOGGER.error("Failed to insert metadata for user id {} phrase id {}", userId, phraseId);
        }
    }
}

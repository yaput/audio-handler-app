package com.yaput.audio.handler.audiohandler.metada.accessor;

import com.yaput.audio.handler.audiohandler.metada.model.AudioMetaData;

import java.sql.SQLException;
import java.util.List;


public interface AudioMetadataAccessor {
    AudioMetaData getMetaDataByUserIdAndPhraseId(int userId, int phraseId) throws SQLException;
    void insertMetaData(int userId, int phraseId, String clientAudioFormat) throws SQLException;
    List<AudioMetaData> getMetadataByUserId(int userId) throws SQLException;
}

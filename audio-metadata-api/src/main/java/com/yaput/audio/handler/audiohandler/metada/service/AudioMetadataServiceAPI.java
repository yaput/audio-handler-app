package com.yaput.audio.handler.audiohandler.metada.service;

import com.yaput.audio.handler.audiohandler.metada.model.AudioMetaData;

public interface AudioMetadataServiceAPI {
    AudioMetaData getMetaDataByUserAndPhraseId(int userId, int phraseId);
    void insertMetadata(int userId, int phraseId, String clientAudioFormat);
}

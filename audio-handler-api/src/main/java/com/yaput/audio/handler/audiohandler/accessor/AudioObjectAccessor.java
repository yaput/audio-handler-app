package com.yaput.audio.handler.audiohandler.accessor;

import java.io.InputStream;

public interface AudioObjectAccessor {
    void putAudioObject(String key, byte[] audioFile);
    InputStream getAudioObject(String filePath);
}

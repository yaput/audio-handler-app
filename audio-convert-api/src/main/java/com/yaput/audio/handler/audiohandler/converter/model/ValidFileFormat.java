package com.yaput.audio.handler.audiohandler.converter.model;

public enum ValidFileFormat {
    WAV("wav", "pcm_s16le"),
    M4A("mp4", "aac");

    private final String stringFormat;
    private final String codec;

    ValidFileFormat(String stringFormat, String codec) {
        this.stringFormat = stringFormat;
        this.codec = codec;
    }

    public String getStringFormat() {
        return stringFormat;
    }

    public String getCodec() {
        return codec;
    }
}

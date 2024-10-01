package com.yaput.audio.handler.audiohandler.converter.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {
    @Value("${audio-converter.bitRate}")
    private int bitRate;
    @Value("${audio-converter.channels}")
    private int channels;
    @Value("${audio-converter.samplingRate}")
    private int samplingRate;
    @Value("${audio-converter.bufferLimit}")
    private int bufferLimit;

    public ConverterConfig() {}

    public int getBitRate() {
        return bitRate;
    }

    public int getChannels() {
        return channels;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public int getBufferLimit() {
        return bufferLimit;
    }
}

package com.yaput.audio.handler.audiohandler.converter.service;

import com.yaput.audio.handler.audiohandler.converter.model.ValidFileFormat;

import java.io.InputStream;

public interface AudioConverterServiceAPI {
    byte[] convertFile(InputStream fileInputStream, ValidFileFormat targetFormat);
}

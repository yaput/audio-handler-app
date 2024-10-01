package com.yaput.audio.handler.audiohandler.metadata.accessor;

import com.yaput.audio.handler.audiohandler.metada.accessor.AudioMetadataAccessor;
import com.yaput.audio.handler.audiohandler.metada.model.AudioMetaData;
import com.yaput.audio.handler.audiohandler.metada.model.MetadataConfig;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioMetadataAccessorImpl implements AudioMetadataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioMetadataAccessorImpl.class);
    private final Connection conn;

    @Autowired
    public AudioMetadataAccessorImpl(MetadataConfig metadataConfig) throws SQLException {
        this.conn = DriverManager.getConnection(metadataConfig.getUrl(), metadataConfig.getUserName(), metadataConfig.getPassword());
    }

    @Override
    public AudioMetaData getMetaDataByUserIdAndPhraseId(int userId, int phraseId) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM audio_metadata WHERE userId = ? AND phraseId = ?");
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, phraseId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new AudioMetaData(
                    rs.getInt("userId"),
                    rs.getInt("phraseId"),
                    rs.getString("filePath")
            );
        }
        return new AudioMetaData(0, 0, "");
    }

    @Override
    public void insertMetaData(int userId, int phraseId, String filePath) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO audio_metadata(userId, phraseId, filePath) values (?,?,?)");
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, phraseId);
        preparedStatement.setString(3, filePath);
        preparedStatement.execute();
    }

    @Override
    public List<AudioMetaData> getMetadataByUserId(int userId) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM audio_metadata WHERE userId = ?");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        List<AudioMetaData> result = new ArrayList<>();
        while(rs.next()) {
            result.add(new AudioMetaData(
                    rs.getInt("userId"),
                    rs.getInt("phraseId"),
                    rs.getString("filePath")
            ));
        }

        return result;
    }

    @PreDestroy
    private void onDestroy() throws SQLException {
        conn.close();
    }
}

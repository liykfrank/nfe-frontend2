package org.iata.bsplink.filemanager.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;

@Converter
public class JpaJsonBsplinkConfigConverter implements
        AttributeConverter<BsplinkFileBasicConfig, String> {

    @Override
    public String convertToDatabaseColumn(BsplinkFileBasicConfig meta) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BsplinkFileBasicConfig convertToEntityAttribute(String dbData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(dbData,
                    BsplinkFileBasicConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

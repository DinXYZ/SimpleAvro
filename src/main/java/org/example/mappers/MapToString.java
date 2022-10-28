package org.example.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.sdk.transforms.DoFn;
import org.example.dto.MyUser;

import java.io.Serializable;

public class MapToString extends DoFn<MyUser, String> implements Serializable {
    private final ObjectMapper mapper;

    public MapToString(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @ProcessElement
    public void processElement(ProcessContext c) throws JsonProcessingException {
        String value = mapper.writeValueAsString(c.element());
        c.output(value);
    }
}

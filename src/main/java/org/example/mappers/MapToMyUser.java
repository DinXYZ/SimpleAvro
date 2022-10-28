package org.example.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.example.dto.MyUser;

import java.io.Serializable;

public class MapToMyUser extends DoFn<GenericRecord, MyUser> implements Serializable {
    private final ObjectMapper mapper;

    public MapToMyUser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @ProcessElement
    public void processElement(ProcessContext c) throws JsonProcessingException {
        MyUser myUser = mapper.readValue(c.element().toString(), MyUser.class);
        c.output(myUser);
    }
}

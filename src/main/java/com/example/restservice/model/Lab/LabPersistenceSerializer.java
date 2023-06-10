package com.example.restservice.model.Lab;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LabPersistenceSerializer extends StdSerializer<Lab> {
    
    public LabPersistenceSerializer() {
        this(null);
    }
  
    public LabPersistenceSerializer(Class<Lab> t) {
        super(t);
    }

    @Override
    public void serialize(
    Lab value, JsonGenerator jgen, SerializerProvider provider) 
    throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        // write basic string properties
        jgen.writeStringField("name", value.getName());
        jgen.writeStringField("number", value.getNumber());
        jgen.writeStringField("building", value.getBuilding());
        
        // create features array
        jgen.writeArrayFieldStart("features");
        for (String feature : value.getFeatures()) {
            jgen.writeString(feature);
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
}

package com.example.restservice.model.Lab;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class LabDeserializer extends StdDeserializer<Lab> {
    
    public LabDeserializer() { 
        this(null); 
    } 

    public LabDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Lab deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonParseException {
        JsonNode node = jp.getCodec().readTree(jp);
        // get name, building, etc
        String name = node.get("name").asText();
        String number = node.get("number").asText();
        String building = node.get("building").asText();
        
        // get array of features
        ArrayNode featuresNode = (ArrayNode) node.get("features");
        String[] features = new String[featuresNode.size()];
        int i = 0;
        for (JsonNode feature : featuresNode) {
            features[i++] = feature.asText();
        }

        // return deserialized lab
        return new Lab(name, number, building, features);
    }
}

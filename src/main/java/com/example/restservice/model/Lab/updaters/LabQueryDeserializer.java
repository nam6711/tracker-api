package com.example.restservice.model.Lab.updaters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode; 

public class LabQueryDeserializer extends StdDeserializer<LabQuery[]> {
    
    public LabQueryDeserializer() { 
        this(null); 
    } 

    public LabQueryDeserializer(Class<?> vc) { 
        super(vc); 
    } 

    @Override
    public LabQuery[] deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        ArrayNode queries = (ArrayNode) node.get("data");
        LabQuery[] dates = new LabQuery[queries.size()];

        // object mapper to read the individual nodes into a LabQuery
        ObjectMapper mapper = new ObjectMapper();

        // loop through and create the list of nodes
        int i = 0;
        for (JsonNode query : queries) { 
            dates[i++] = mapper.readValue(query.toString(), LabQuery.class);
        }

        // sort the dates!
        sortByTime(dates);

        return dates;
    }

    private void sortByTime(LabQuery[] source) {
        LabQuery buffer;

        // compare all items
        int i = 0;
        do {
            // check if you need to swap the two
            if (i + 1 < source.length &&
            source[i + 1] != null && 
            (source[i].getStartTimeLong() > source[i + 1].getStartTimeLong())) {
                // swap the two
                buffer = source[i + 1];
                source[i + 1] = source[i];
                source[i] = buffer;

                // reset the loop
                i = 0;
                continue;
            }

            // continue the loop
            ++i;
        } while (i < source.length);
    }
}

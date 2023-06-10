package com.example.restservice.model.Lab;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.example.restservice.model.Lab.updaters.LabMeetingContainer;
import com.example.restservice.model.Lab.updaters.LabQuery;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonComponent
public class LabQuerySerializer extends StdSerializer<Lab> {
    
    public LabQuerySerializer() {
        this(null);
    }
  
    public LabQuerySerializer(Class<Lab> t) {
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

        // write the labs schedule to the json file sent over to user
        jgen.writeArrayFieldStart("schedule");
        LabMeetingContainer meetings = value.getLabMeetingContainer();
        LabQuery[][] meetingDaysOfWeek = meetings.getorganizedSchedule();
        // loop through days of week and generate the schedule property
        for (LabQuery[] dayOfWeek : meetingDaysOfWeek) {
            jgen.writeStartArray();
            // loop through and add each individual meeting as an array
            for (LabQuery meeting : dayOfWeek) {
                jgen.writeStartArray();
                jgen.writeString(meeting.getStartTime());
                jgen.writeString(meeting.getEndTime());
                jgen.writeString(meeting.getMeeting());
                jgen.writeEndArray();
            }
            jgen.writeEndArray();
        }
        jgen.writeEndArray();

        // finish writing the object
        jgen.writeEndObject();
    }
}

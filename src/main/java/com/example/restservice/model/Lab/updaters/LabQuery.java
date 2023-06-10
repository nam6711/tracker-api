package com.example.restservice.model.Lab.updaters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * class LabQuery
 * author Noah Manoucheri
 * purpose:
 *      when querying from the rit api the data needs to be saved for
 *          readabliity
 *      this hold the meeting data for that lab
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabQuery {
    private String room_id, start, end, day, date, meeting;
    private long startTimeLong;

    public LabQuery(@JsonProperty("room_id") String room_id, @JsonProperty("start") String start, @JsonProperty("end") String end, @JsonProperty("day") String day, @JsonProperty("date") String date, @JsonProperty("meeting") String meeting) {
        this.room_id = room_id;
        this.start = start;
        this.end = end;
        this.day = day;
        this.date = date;
        this.meeting = meeting;
        
        // creates a long version of the start time of the meeting for sorting
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date item = sdf.parse(this.date + " " + this.start);
            // System.out.println(item.toString());
            this.startTimeLong = item.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            this.startTimeLong = 0;
        }
    }

    public long getStartTimeLong() {
        return this.startTimeLong;
    }

    public String getStartTime() {
        return this.start;
    }

    public String getEndTime() {
        return this.end;
    }

    public String getDay() {
        return this.day;
    }

    public String getRoomId() {
        return room_id;
    }

    public String getMeeting() {
        return this.meeting;
    }

    public String[] getMeetingInformation() {
        return new String[] {start,end,meeting,date};
    }

    @Override
    public String toString() {
        return meeting + " : " + date + " from " + start + " - " + end;
    }
}

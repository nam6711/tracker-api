package com.example.restservice.model.Lab.updaters;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LabMeetingContainer {
    private HashMap<String, LabQuery[]> data;
    private LabQuery[][] organizedSchedule;

    public LabMeetingContainer() {
    }

    public void setData(HashMap<String, LabQuery[]> data) {
        this.data = data;
        loadDataArray();
    }

    public LabQuery[][] getorganizedSchedule() {
        return this.organizedSchedule;
    }

    private void loadDataArray() {
        LabQuery[][] queryArray = new LabQuery[7][];
        String[] dates = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        for (int i = 0; i < 7; i++) { 
            // get the current day of week
            LabQuery[] items = data.get(dates[i]);

            // if nothing scheduled, skip it
            if (items == null) {
                queryArray[i] = new LabQuery[0];
                continue;
            }

            // create new array
            LabQuery[] current = new LabQuery[items.length];

            // fill array
            int j = 0;
            for (LabQuery item : items) {
                current[j++] = item;
            }

            // add it on B]
            queryArray[i] = current;
        } 

        this.organizedSchedule = queryArray;
    }

    public void display() { 
        System.out.println("\n+++++++++DISPLAYING+++++++++\n");
        for (LabQuery[] item : organizedSchedule) {
            if (item.length == 0)
                continue;
            
            System.out.println(item[0].getDay());
            
            for (LabQuery query : item) {
                System.out.println("\t" + query.toString());
            }
        }
    }
}

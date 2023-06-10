package com.example.restservice.model.Lab.updaters;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class LabScheduleUpdater implements Runnable {

    private final LabMeetingContainer container;
    private String name;
    private String labID;
    Thread t;

    public LabScheduleUpdater(String threadName, String labID, LabMeetingContainer container) {
        this.labID = labID;
        this.container = container;

        // create thread
        name = threadName;
        t = new Thread(this, name);
        System.out.println("Lab scheduled to update: " + t);
        t.start();
    }

    /**
     * method: run
     * author: Noah Manoucheri
     * purpose:
     *      a lab's thread will loop and query its schedule every 24 hours
     *          to update itself
     *      the lab, after querying, will sleep for 24 hours
     */
    public void run() { 
        // will hold items by day of the week
        HashMap<String, LabQuery[]> queries = new HashMap<String, LabQuery[]>();   
            
        // setup the mapper
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LabQuery[].class, new LabQueryDeserializer());
        mapper.registerModule(module);
        JsonNode jsonData;
        
        // url vars
        final String url = "https://api.rit.edu/rooms/" + labID + "/meetings?date=";
        final String key = "&RITAuthorization=hPMWQJXKEbVLURMOcJnekKRtlKtbjeStmWRPdojy";
        String queryString = "";

        // holds date objects
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(); // current date
        Calendar cal = Calendar.getInstance();

        do {
            // will hold items by day of the week
            queries = new HashMap<String, LabQuery[]>();  
            date = new Date(); // current date
            cal = Calendar.getInstance();

            // loop through the next week and compile a new list for the labs to hold
            int i = 0;
            do {
                String dateString = formatter.format(date);
                queryString = url + dateString + key;
                
                // try getting the HTTP request
                try {
                    // fetch the data
                    jsonData = mapper.readTree(new URL(queryString));
                    // query and put the item into the map
                    LabQuery[] query = mapper.readValue(jsonData.toString(), LabQuery[].class);    
                    if (query.length > 0)
                        queries.put(query[0].getDay(), query);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("UNABLE TO READ TO LABQUERY CLASS");
                }
                
                // sets calendar to the next day
                cal.add(Calendar.DATE, 1);
                date = cal.getTime();
            } while (++i < 7); // loops through the next week 

            // test by reading all the values
            container.setData(queries); 

            // loop the guy every like 24 hours
            try {
                System.out.println("========= LOADED " + t + " =========");
                Thread.sleep(60000 * 60 * 24);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        } while (true);
        
    }
}

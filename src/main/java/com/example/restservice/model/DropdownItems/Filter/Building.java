package com.example.restservice.model.DropdownItems.Filter;

import com.example.restservice.model.Lab;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Building extends Filter {
    // json properties
    @JsonProperty("abbreviation") private String abbreviation;
    @JsonProperty("num") private String num;

    public Building(@JsonProperty("name") String name, @JsonProperty("abbreviation") String abbreviation, @JsonProperty("num") String num) {
        super("Building", name);

        this.abbreviation = abbreviation;
        this.num = num;
    }

    @Override
    public void update(String name) {
        super.update(name);

        
        // iterate through all subscribed labs and alert them that the building
        //      name changed
        for (Lab lab : this.labs.values()) {
            lab.setLabBuilding(this);
        }
    }

    /**
     * @return String representing the building number
     */
    public String getNum() {
        return this.num;
    }

    /**
     * @return String building abbreviation
     */
    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void updateBuildingInfo(Building building) {
        // assign unique values of a building
        this.abbreviation = building.getAbbreviation();
        this.num = building.getNum();
    }
}
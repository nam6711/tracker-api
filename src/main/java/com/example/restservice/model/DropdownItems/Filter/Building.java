package com.example.restservice.model.DropdownItems.Filter;

import com.example.restservice.model.DropdownItems.Item;
import com.example.restservice.model.Lab.Lab;
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
    public void update(String oldName) { 
        // iterate through all subscribed labs and alert them that the building
        //      name changed
        for (Lab lab : this.labs.values()) {
            lab.setLabBuilding(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateSelf(Item updatedItem) {
        if (updatedItem instanceof Building) {
            Building drop = (Building) updatedItem;
            
            // set new name
            String oldName = this.getName();
            this.setName(drop.getName());

            // set num and abbreviation
            this.num = drop.getNum();
            this.abbreviation = drop.getAbbreviation();
            
            this.update(oldName);
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

    public String getType() {
        return "Building";
    }
}
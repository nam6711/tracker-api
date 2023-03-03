package com.example.restservice.model.DropdownItems.Filter;

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
}
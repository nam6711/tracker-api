package com.example.restservice.model.DropdownItems.Filter;

import com.example.restservice.model.Lab;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a simple class representing features that a lab may have
 */
public class Feature extends Filter {
    public Feature(@JsonProperty("name") String name) {
        super("Feature", name);
    }

    @Override
    public void update(String name) {
        // store old string
        String oldName = this.getName();
        // set name        
        super.update(name);

        System.out.println(oldName);

        
        // iterate through all subscribed labs and alert them that the building
        //      name changed
        for (Lab lab : this.labs.values()) {
            lab.setFeatureName(oldName, this.getName());
        }
    }
}

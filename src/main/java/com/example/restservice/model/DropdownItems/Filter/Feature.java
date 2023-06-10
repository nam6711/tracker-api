package com.example.restservice.model.DropdownItems.Filter;

import com.example.restservice.model.DropdownItems.Item;
import com.example.restservice.model.Lab.Lab;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a simple class representing features that a lab may have
 */
public class Feature extends Filter {
    public Feature(@JsonProperty("name") String name) {
        super("Feature", name);
    }

    @Override
    public void update(String oldName) {  
        // iterate through all subscribed labs and alert them that the building
        //      name changed
        for (Lab lab : this.labs.values()) {
            lab.setFeatureName(oldName, this.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateSelf(Item updatedItem) {
        if (updatedItem instanceof Feature) {
            Feature drop = (Feature) updatedItem;
            
            String oldName = this.getName();
            this.setName(drop.getName());
            
            this.update(oldName);
        }
    }

    public String getType() {
        return "Feature";
    }
}

package com.example.restservice.model.DropdownItems.Filter;

import com.example.restservice.model.DropdownItems.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a simple class representing a Hour state a Lab may have
 */
public class Hour extends Filter {
    public Hour(@JsonProperty("name") String name) {
        super("Hour", name);
    }

    /**
     * {@inheritDoc}
     */
    public void updateSelf(Item updatedItem) {
        if (updatedItem instanceof Hour) {
            Hour drop = (Hour) updatedItem;
            this.setName(drop.getName());
        }
    }

    public String getType() {
        return "Hour";
    }
}
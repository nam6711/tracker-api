package com.example.restservice.model.DropdownItems.Filter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a simple class representing a Hour state a Lab may have
 */
public class Hour extends Filter {
    public Hour(@JsonProperty("name") String name) {
        super("Hour", name);
    }
}
package com.example.restservice.model.DropdownItems.Filter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a simple class representing features that a lab may have
 */
public class Feature extends Filter {
    public Feature(@JsonProperty("name") String name) {
        super("Feature", name);
    }
}

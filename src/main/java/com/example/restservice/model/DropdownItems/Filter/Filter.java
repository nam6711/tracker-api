package com.example.restservice.model.DropdownItems.Filter;

import java.util.Map;

import com.example.restservice.model.Lab;
import com.example.restservice.model.DropdownItems.Dropdown;
import com.example.restservice.model.DropdownItems.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * base structure for creating a filter. it has a type, and a name
 * will be used by child classes in order to create Filter types
 */
public abstract class Filter implements Item {
    // JSON Properties
    @JsonProperty("type") private String type;
    @JsonProperty("name") private String name; // will always be lower case

    // parent dropdown, may be changed, so is technically an observable
    private Dropdown parent;

    // observers
    Map<String, Lab> labs;
    Map<String, Item> items;

    public Filter(@JsonProperty("type") String type, @JsonProperty("name") String name) {
        this.type = type;
        this.name = name.toLowerCase();
    }

    /**
	 * {@inheritDoc}
	 */
    public void removeAll() {
    }

    /**
     * {@inheritDoc}
     */
    public void removeItem(Item item) {
    }

    /**
     * checks if the Filter is a Building, and if it is whether or not it
     *      is a Building with a matching name with what was requested.
     * 
     * @param buildingName String name of the building we are searching for
     * @return if a matching building is found, return that building, otherwise return null
     */
    public Building findBuilding(String buildingName) {
        // if this filter is a building
        //  and it has the same name as the requested building
        // then return a reference to this building instance
        if (this instanceof Building && this.name.equals(buildingName)) {
            Building foundBuilding = (Building)this;
            return foundBuilding;
        }

        return null;
    }

    public Feature findFeature(String featureName) {
        // if this filter is a Feature
        //  and it has the same name as the requested feature
        // then return a reference to this filter instance
        if (this instanceof Feature && this.name.equals(featureName)) {
            Feature foundBuilding = (Feature)this;
            return foundBuilding;
        }

        return null;
    }

    public void setParent(Dropdown item) {
        this.parent = parent;
    }

    /**
     * gets the filters type
     * @return the Filter's type
     */
    public String getType() {
        return type;
    }

    /**
	 * {@inheritDoc}
	 */
    public String getName() {
        return name;
    }
}

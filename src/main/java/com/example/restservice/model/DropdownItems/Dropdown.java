package com.example.restservice.model.DropdownItems;

import java.util.Map;

import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.qos.logback.core.filter.Filter;

public class Dropdown implements Item {
    // json properties
    @JsonProperty("type") private String type;
    @JsonProperty("name") private String name;
    @JsonProperty("items") private Item[] items;
    
    // holds the parent of the dropdown
    private Dropdown parent;

    public Dropdown(@JsonProperty("name") String name, @JsonProperty("items") Item[] items) {
        this.type = "Dropdown";
        this.name = name.toLowerCase();
        this.items = items;
    }

    /**
     * this runs through all items within a given dropdown and
     *      sets their parent to the current dropdown.
     * whenever the parent experiences an update, or something changes, 
     *      the parent will alert all its children via the items array.
     * lastly, this allows the parent to search for information from 
     *      its children in case a Lab needs to reference something
     *      like a Building number, or a Lab Feature
     */
    public void loadDropdownItems() {
        for (Item item : this.items) {
            // tell item to subscribe to this as the parent
            item.setParent(this);
        }
    }

    public Building findBuilding(String buildingName) {
        for (Item dropdownItem : this.items) {
            Building result = dropdownItem.findBuilding(buildingName);
            // if the queried result was not null, then we can return 
            //      the value as we've found the right building
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    public Feature findFeature(String feature) {
        for (Item dropdownItem : this.items) {
            // store the queried item
            Feature result = dropdownItem.findFeature(feature);
            
            // if the found result was not null, then add it to
            //      the found labs
            if (result != null) {
                return result;
            }
        }

        return null;
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

    public void setParent(Dropdown item) {
        this.parent = item;
    }

    /**
	 * {@inheritDoc}
	 */
    public String getName() {
        return name;
    }
}

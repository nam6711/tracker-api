package com.example.restservice.model.DropdownItems.Filter;

import java.util.Map;
import java.util.TreeMap;

import com.example.restservice.model.DropdownItems.Dropdown;
import com.example.restservice.model.DropdownItems.Item;
import com.example.restservice.model.Lab.Lab;
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

    public Filter(@JsonProperty("type") String type, @JsonProperty("name") String name) {
        this.type = type;
        this.name = name.toLowerCase();

        this.labs = new TreeMap<>();
    }

    /**
     * when a lab is assigned a feature or building, it will subscribe to
     *      said filter. this is so that if the lab is deleted, the filter can
     *      stop tracking it
     * 
     * labs will also have their features edited if a feature is deleted
     * 
     * if a building is deleted, all labs connected to it will be deleted
     *  
     * @param lab lab that is subscribing to the Filter
     */
    public void subscribe(Lab lab) {
        this.labs.put(lab.getId(), lab);
    }

    /**
     * when a lab changes some property, it will alert its features that it
     *      will no longer have so that the Filters no longer
     *      will track or alert it of updates
     * 
     * @param lab
     */
    public void unsubscribe(Lab lab) {
        this.labs.remove(lab.getId());
    }

    /**
	 * {@inheritDoc}
	 */
    public void remove() {

        // if this is a building, then tell all labs to remove themselves
        if (this instanceof Building) {

            // tell parent to remove this item
            this.parent.removeItem(this);
            return;
        }

        // otherwise, just tell the labs to remove the feature

        // tell parent to remove this item
        this.parent.removeItem(this);

        // alert all subscribing labs to unsubscribe, and that this item
        //      no longer exists
        for (Lab lab : this.labs.values()) {
            lab.removeFeature(this);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void removeItem(Item item) {
    }

    public void update(String name) {
        // iterate through all subscribed labs and alert them that the building
        //      name changed
        for (Lab lab : this.labs.values()) {
            lab.setFeatureName(name, this.name);
        }
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

    /**
     * {@inheritDoc}
     */
    public Item checkItemName(String itemName) {
        // if the item name is the same as this item, then return this item
        //      otherwise return null
        if (this.getName().equals(itemName)) { 
            return this;
        }

        return null;
    }

    public void setParent(Dropdown newParent) {
        // first, check if the Item already has a parent. 
        if (this.parent != null) {
            // if the filter already has a parent, remove itself from the parent
            this.parent.removeItem(this); 
            // tell the new parent to add the dropdowns
            newParent.addItem(this);
        }

        // set the new parent and add the item to it
        this.parent = newParent;
        // we only call add item once a filter already has a parent as when
        //      the server launches, the parent dropdowns already have a reference
        //      to their child filters. if we call add item here, on launch duplicate
        //      filters will be created
    }

    /**
     * gets the filters type
     * @return the Filter's type
     */
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * {@inheritDoc}
	 */
    public String getName() {
        return name;
    }

    public Lab[] getLabs() {
        Lab[] labArray = new Lab[this.labs.size()];
        int i = 0;
        for (Map.Entry<String, Lab> lab : this.labs.entrySet()) {
            labArray[i++] = lab.getValue();
        }

        return labArray;
    }
}

package com.example.restservice.model.DropdownItems;


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
     * 
     * whenever the parent experiences an update, or something changes, 
     *      the parent will alert all its children via the items array.
     * 
     * lastly, this allows the parent to search for information from 
     *      its children in case a Lab needs to reference something
     *      like a Building number, or a Lab Feature
     */
    public void loadDropdownItems() {
        for (Item item : this.items) {
            // tell item to subscribe to this as the parent
            item.setParent(this);
            
            // if this is a dropdown, then set its parent
            if (item instanceof Dropdown) {
                Dropdown dropdown = (Dropdown) item;
                dropdown.loadDropdownItems();
            }
        }
    }

    public Item findItem(String itemName) {
        // iterate through all items and return the item we are on
        //      if its name matches what we're looking for
        // if no items are found return null
        for (Item dropdownItem : this.items) {
            // search through all the given items in this dropdown
            Item result = dropdownItem.checkItemName(itemName);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * responsible for searching through a dropdown and finding whether or not
     *      it has a building with a given name. if it does, return that building.
     *      otherwise return null
     * 
     * @param buildingName matching name of building we are searching for
     * 
     * @return a {@link Building} that matches the given name request. null if none found
     */
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

    /**
     * {@inheritDoc}
     */
    public Item checkItemName(String itemName) { 
        // if the item name is the same as this item, then return this item
        //      otherwise search through this dropdown's items for it
        if (this.getName().equals(itemName)) { 
            return this;
        }

        // return if any of the dropdowns had the item inside it
        return this.findItem(itemName);
    }

    /**
     * responsible for searching through a dropdown and finding whether or not
     *      it has a feature with a given name. if it does, return that feature.
     *      otherwise return null
     * 
     * @param feature matching name of feature we are searching for
     * 
     * @return a {@link Filter} that matches the given name request. null if none found
     */
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
     * responsible for searching through a dropdown and finding whether or not
     *      it has a dropdown with a given name. if it does, return that dropdown.
     *      otherwise return null
     * 
     * @param name matching name of dropdown we are searching for
     * 
     * @return a {@link Dropdown} that matches the given name request. null if none found
     */
    public Dropdown findDropdown(String name) {
        // if the current dropdown is what we're lookin for, return it!
        if (this.name.equals(name)) {
            return this;
        }

        // iterate through all items in our collection and check if they're 
        //      a dropdown and have the same title as what we're looking for
        for (Item item : this.items) {
            if (item instanceof Dropdown) {
                // convert the current item to a dropdown
                Dropdown currentItem = (Dropdown)item;
                Dropdown result = currentItem.findDropdown(name);
                
                // if the current result is not null, then we've found our dropdown!
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * removes all items from the dropdown, and then alerts the parent to
     *      remove this
     * 
     * only performs this if the dropdown has an existing parent
     */
    public void remove() {
        if (this.parent != null) {
            // call each individual dropdown and set their parents to
            //      this dropdown's parent
            for (Item dropdownItem : this.items) {
                dropdownItem.setParent(this.parent);
            }
    
            // tell this dropdown's parent to remove this
            this.parent.removeItem(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeItem(Item itemToRemove) {
        // create a buffer to hold the new item
        Item[] itemBuffer = new Item[this.items.length - 1]; 

        // iterate through the entire item array and copy it onto the new array
        //      so we can allow the new filter to be added in nicely
        int i = 0;
        for (Item item : this.items) {
            if (item != itemToRemove) 
                itemBuffer[i++] = item;
        }

        // point this.items to the item buffer now
        this.items = itemBuffer;
    }

    public void addItem(Item newItem) {
        // create a buffer to hold the new item
        Item[] itemBuffer = new Item[this.items.length + 1];

        // iterate through the entire item array and copy it onto the new array
        //      so we can allow the new filter to be added in nicely
        int i = 0;
        for (Item item : this.items) {
            itemBuffer[i++] = item;
        }
        itemBuffer[i] = newItem;

        // point this.items to the item buffer now
        this.items = itemBuffer;
    }

    /**
     * {@inheritDoc}
     */
    public void updateSelf(Item updatedItem) {
        if (updatedItem instanceof Dropdown) {
            Dropdown drop = (Dropdown) updatedItem;
            this.name = drop.getName();
        }
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
	 * {@inheritDoc}
	 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item[] getItems() {
        return this.items;
    }

    public String getType() {
        return "Dropdown";
    }
}

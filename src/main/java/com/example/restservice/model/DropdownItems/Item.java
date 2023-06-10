package com.example.restservice.model.DropdownItems;

import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.example.restservice.model.DropdownItems.Filter.Hour;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dropdown.class, name = "Dropdown"),
        @JsonSubTypes.Type(value = Building.class, name = "Building"),
        @JsonSubTypes.Type(value = Feature.class, name = "Feature"),
        @JsonSubTypes.Type(value = Hour.class, name = "Hour"),
})
/**
 * interface for composite design pattern. Items may be leaves
 *      or components
 */
public interface Item {  
    /**
     * method will run through and check a given Item as to whether or not it
     *      is a Building with a matching name. This is used so that Lab 
     *      classes can reference data on Buildings on campus instead of having
     *      to hard code values to themselves
     * 
     * @param buildingName String name of the building we are searching for
     * @return if a matching building is found, return that building, otherwise return null
     */
    Building findBuilding(String buildingName);

    Feature findFeature(String feature);

    /**
     * checks if a given item name matches the instantiated Item
     * 
     * @param itemName name of the item we're looking for
     * 
     * @return returns the instantiated item if it is the item we're looking for
     *      returns null otherwise
     */
    Item checkItemName(String itemName);

    /**
     * removes an item from a listing of all items
     */
    void removeItem(Item item);

    void setParent(Dropdown item);

    /**
     * any item should return its id, as it is used for mapping to components
     * @return String id of the lab
     */
    String getName();

    /**
     * if an item is removed from a dropdown, it should be able to process
     *      and reflect that persistently
     * 
     * if a dropdown is removed, it shall remove itself permanently
     */
    void remove();

    /**
     * Every item can be updated by being given a new version.
     * 
     * The class will run through the important properties, and apply
     *      them to itself.
     * 
     * @param updatedItem the new item to use when updating self
     */
    void updateSelf(Item updatedItem);

    String getType();
}

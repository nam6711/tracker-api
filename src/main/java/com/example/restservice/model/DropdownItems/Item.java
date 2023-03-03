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
     * whenever an Item is removed from the persistent Items list, it needs
     *      to alert all Lab's or other Items that are reliant upon it.
     * this will alert the subscribed objects that the Item was removed so that
     *      their properties are properly saved and don't cause any errors.
     * this will run through all ItemSubscribers and LabSubscribers 
     */
    void removeAll();

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
     * removes an item from a listing of all items
     */
    void removeItem(Item item);

    void setParent(Dropdown item);

    /**
     * any item should return its id, as it is used for mapping to components
     * @return String id of the lab
     */
    String getName();
}

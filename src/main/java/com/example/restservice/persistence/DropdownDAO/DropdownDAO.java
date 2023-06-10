package com.example.restservice.persistence.DropdownDAO;

import java.io.IOException;

import com.example.restservice.model.DropdownItems.Dropdown;
import com.example.restservice.model.DropdownItems.Item;
import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.example.restservice.persistence.LabDAO.LabDAO;

import ch.qos.logback.core.filter.Filter;

/**
 * Defines the interface for how to interact 
 * and manipulate Lab object persistence in JSON files
 * 
 * @author Nat Manoucheri
 */
public interface DropdownDAO {
    /**
     * sets a connection to the lab DAO in order to allow
     *  labs to recieve updates to existing filters
     * 
     * @param dropdownDAO reference to the dropdownDAO
     */
    void setLabDAO(LabDAO labDAO);

    /**
     * searches through existing dropdowns to find a given Building
     * returns information on the building if found, or null otherwise
     * 
     * @param buildingName String representing the name of a building
     * @return Building reference if a building was found. otherwise returns null
     */
    Building findBuilding(String buildingName);

    /**
     * 
     * 
     * @param features String array representing the names of features requested
     * @return Feature array of all features a lab has requested. returns [] if nothing is found
     */
    Feature[] findFeatures(String[] features);
    
    /**
     * Gets a lab with the specified name as queried by a user
     * 
     * @param name the term to check for seeing if a {@link Lab lab} exists
     * 
     * @return a {@link Lab lab} with the same name as the searched term
     * <br>
     * or returns null if nothing is found
     * 
     * @throws IOException for any issues that may arise
     */
    Dropdown getDropdown(String name) throws IOException;

    /**
     * Get all existing labs within the labs Map
     * 
     * @return a {@link Lab lab} array with all loaded Labs from the labs Map
     * 
     * @throws IOException for any issues that may arise
     */
    Dropdown[] getDropdowns() throws IOException;

    /**
     * Creates and saves a {@linkplain Filter}
     * 
     * @param filter {@linkplain Filter} object to be created and saved
     *
     * @return new {@link Filter} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Item createItem(String dropdownLocation, Item filter) throws IOException;

    /**
     * Moves a {@linkplain Filter} to another dropdown
     * 
     * @param {@link Filter} object to be updated and saved
     * 
     * @return updated {@link Filter} if successful, null if
     * {@link Filter} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Boolean moveItem(String dropdownLocation, String filterName) throws IOException;

    /**
     * Updates and saves a {@linkplain Building}
     * 
     * @param {@link Building} object to be updated and saved
     * 
     * @return updated {@link Building} if successful, null if
     * {@link Building} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Item updateItem(String itemName, Item updatedItem) throws IOException;  

    Boolean deleteItem(String itemname) throws IOException;
} 

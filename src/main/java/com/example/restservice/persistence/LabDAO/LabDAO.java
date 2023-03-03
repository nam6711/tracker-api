package com.example.restservice.persistence.LabDAO;

import java.io.IOException;

import com.example.restservice.model.Lab;
import com.example.restservice.persistence.DropdownDAO.DropdownDAO;

/**
 * Defines the interface for how to interact 
 * and manipulate Lab object persistence in JSON files
 * 
 * @author Nat Manoucheri
 */
public interface LabDAO {

    /**
     * sets a connection to the dropdown DAO in order to allow
     *  labs to reference the existing filters for use in building
     *  properties
     * 
     * @param dropdownDAO reference to the dropdownDAO
     */
    void setDropdownDAO(DropdownDAO dropdownDAO);
    
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
    Lab getLab(String name) throws IOException;

    /**
     * Get all existing labs within the labs Map
     * 
     * @return a {@link Lab lab} array with all loaded Labs from the labs Map
     * 
     * @throws IOException for any issues that may arise
     */
    Lab[] getLabs() throws IOException;
}

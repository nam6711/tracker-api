package com.example.restservice.persistence.LabDAO;

import java.io.IOException;

import com.example.restservice.model.Lab.Lab;
import com.example.restservice.persistence.DropdownDAO.DropdownDAO;
import com.fasterxml.jackson.databind.JsonNode;

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
    JsonNode getLab(String name) throws IOException;

    /**
     * Get all existing labs within the labs Map
     * 
     * @return a {@link Lab lab} array with all loaded Labs from the labs Map
     * 
     * @throws IOException for any issues that may arise
     */
    JsonNode getLabs() throws IOException;

    /**
     * Creates and saves a {@linkplain Lab}
     * 
     * @param lab {@linkplain Lab} object to be created and saved
     *
     * @return new {@link Lab} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lab createLab(Lab account) throws IOException;

    /**
     * Updates and saves a {@linkplain Lab}
     * 
     * @param {@link Lab} object to be updated and saved
     * 
     * @return updated {@link Lab} if successful, null if
     * {@link Lab} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Lab updateLab(String labID, Lab lab) throws IOException;

    /**
     * Deletes a {@linkplain Lab} with the given name
     * 
     * @param name The name of the {@link Lab}
     * 
     * @return true if the {@link Lab} was deleted
     * <br>
     * false if hero with the given name does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteLab(String name) throws IOException;

    boolean saveLabs() throws IOException;
}

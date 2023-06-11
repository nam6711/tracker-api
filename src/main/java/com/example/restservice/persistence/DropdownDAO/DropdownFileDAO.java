package com.example.restservice.persistence.DropdownDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.restservice.model.DropdownItems.Dropdown;
import com.example.restservice.model.DropdownItems.DropdownSerializer;
import com.example.restservice.model.DropdownItems.Item;
import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.example.restservice.model.Lab.Lab;
import com.example.restservice.persistence.LabDAO.LabDAO;

@Component
public class DropdownFileDAO implements DropdownDAO {
    Map<String, Dropdown> parentDropdowns; // holds the top-most dropdowns
    private ObjectMapper objectMapper;

    private String filename;

    // this contains a reference to the labDAO, which is used in order
    // to reference Labs of any updates to buildings/filters
    private LabDAO labDAO;

    public DropdownFileDAO(@Value("${dropdowns.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        SimpleModule module = new SimpleModule();
        module.addSerializer(Dropdown[].class, new DropdownSerializer());
        this.objectMapper.registerModule(module);
        load(); // function to load all data from JSON file
    }

    public void setLabDAO(LabDAO labDAO) {
        this.labDAO = labDAO;
    }

    /**
     * Creates an array of {@link Lab lab} from the labs Map 
     * using a search term. If the search term
     * is empty, return all {@link Lab lab} in labs Map
     * 
     * @param comparisonTerm is used for determining which labs to return
     * 
     * @return an array of {@link Lab lab} lab
     */
    private Dropdown[] getDropdownArray(String comparisonTerm) {
        ArrayList<Dropdown> dropdownArrayList = new ArrayList<>();

        // iterate through all labs loaded in memory and check if
        // any have the same name as the searched term
        // if the comparisonTerm is null, the program will return all Labs
        for (Dropdown dropdown : parentDropdowns.values()) {
            // if the sent in comparison term is null
            //      return every lab in the labs Map
            // if the current lab itterated onto has the same name as the comparison term
            //      add it to the array list of labs
            // else pass over it
            if (comparisonTerm == null || dropdown.getName().contains(comparisonTerm)) {
                dropdownArrayList.add(dropdown);
            }
        }

        // finally transform the compiled list of labs into a array of Labs
        // then return it back to whatever requested a array of labs
        Dropdown[] dropdownArray = new Dropdown[dropdownArrayList.size()];
        dropdownArrayList.toArray(dropdownArray);
        return dropdownArray;
    }

    /**
     * If no parameter is given, send null to the getLabArray
     * so that all labs in memory are loaded into an array and returned
     * 
     * @return an array of all {@link Lab lab} in the labs Map
     */
    private Dropdown[] getDropdownArray() {
        return getDropdownArray(null);
    }

    /**
     * Loads {@link Dropdown lab} from the JSON file into the map
     * 
     * @return true if the file was read correctly
     * @throws IOException for when an error occurs
     */
    private boolean load() throws IOException {
        parentDropdowns = new TreeMap<>();

        // loads all labs from JSON and maps into an array of Labs 
        Dropdown[] dropdownArray = objectMapper.readValue(new File(filename), Dropdown[].class);
        
        // tell labs to initialize their objects, and take the returned items
        //      to be loaded persistently
        for (Dropdown dropdown : dropdownArray) {
            parentDropdowns.put(dropdown.getName(), dropdown);
            // tell the dropdown to organize its filters
            dropdown.loadDropdownItems();
        }

        // finish
        return true;
    }

    private Boolean saveDropdowns() throws IOException {
        // loads all labs into an array for saving to JSON
        Dropdown[] dropdownArray = getDropdownArray();

        objectMapper.writeValue(new File(filename), dropdownArray);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dropdown getDropdown(String name) {
        synchronized(parentDropdowns) {
            if (parentDropdowns.containsKey(name)) 
                return parentDropdowns.get(name);
            else
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dropdown[] getDropdowns() throws IOException {
        synchronized(parentDropdowns) {
            return getDropdownArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Item createItem(String dropdownLocation, Item filter) throws IOException {
        synchronized (parentDropdowns) {
            // run through each lab, and then check if the given dropdown location
            //      is either within the dropdown, or the dropdown itself.
            // the chosen dropdown will take the filter and add it to its list of
            //      items
            for(Dropdown dropdown : this.parentDropdowns.values()) {
                Dropdown result = dropdown.findDropdown(dropdownLocation);
                // if the result of the previous query is not null, then we've found
                //      our dropdown!! so add it on, and save the labs
                if (result != null) {
                    result.addItem(filter);
                    filter.setParent(result);
                    saveDropdowns(); // may throw an IOException
                    break;
                }
            }

            return filter;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Boolean moveItem(String dropdownLocation, String filterName) throws IOException {
        // see if the item is a building or feature.
        synchronized(parentDropdowns) {
            Item foundItem = null;
            for (Dropdown dropdown : this.parentDropdowns.values()) {
                foundItem = dropdown.findItem(filterName);
                
                // if the item exists, then leave
                if (foundItem != null) {
                    break;
                }
            } 
            
            for(Dropdown dropdown : this.parentDropdowns.values()) {
                Dropdown result = dropdown.findDropdown(dropdownLocation);
                // if the result of the previous query is not null, then we've found
                //      our dropdown!! so add it on, and save the labs
                if (result != null && foundItem != null) {
                    // tell the filter to set itself to a new dropdown
                    foundItem.setParent(result);
    
                    saveDropdowns(); // may throw an IOException
                    return true;
                }
            }

            // if the new dropdown was never found, return false
            return false;
        }  

    }

    public Item updateItem(String itemName, Item updatedItem) throws IOException {
        synchronized (parentDropdowns) {
            Item foundItem = null;
            for (Dropdown dropdown : this.parentDropdowns.values()) {
                foundItem = dropdown.findItem(itemName);
                
                // if the item exists, then leave
                if (foundItem != null) {
                    foundItem.updateSelf(updatedItem);
                    if (saveDropdowns() && labDAO.saveLabs())
                        return foundItem;
                }
            }
            return null;
        }
    }

    public Boolean deleteItem(String dropdownItem) throws IOException {
        synchronized(parentDropdowns) { 
            for (Dropdown dropdown : this.parentDropdowns.values()) {
                Item result = dropdown.findItem(dropdownItem);
                
                // if the item exists, then remove it
                if (result != null) {
                    // if a building, delete all attatched labs
                    if (result instanceof Building) {
                        Building building = (Building) result;
                        Lab[] labs = building.getLabs();
                        for (Lab lab : labs) {
                            this.labDAO.deleteLab(lab.getId());
                            lab.deleteSelf();
                        }
                    }

                    // remove the filter
                    result.remove();
                    return (this.saveDropdowns());
                }
            }
            
            return false;
        }
    }

    public Building findBuilding(String buildingName) {
        // go to the 'colleges' dropdown in order to 
        //      find the Buildings and iterate through
        //      to see if we can find what we're looking for
        return parentDropdowns.get("colleges").findBuilding(buildingName);
    }

    public Feature findSingleFeature(String feature) {
        // query the parent dropdown that holds all features for the current item
        Feature result = parentDropdowns.get("features").findFeature(feature);
        // if the found result was not null, then add it to
        //      the found labs
        if (result != null) {
            return result;
        }

        return null;
    }

    public Feature[] findFeatures(String[] features) {              
        // loop control variable
        int i = 0;

        // initialize an array to hold all items found in the array
        Feature[] labFeatures = new Feature[features.length];               

        for (String feature : features) {
            // query the parent dropdown that holds all features for the current item
            Feature result = parentDropdowns.get("features").findFeature(feature);
            // if the found result was not null, then add it to
            //      the found labs
            if (result != null) {
                labFeatures[i++] = result;
            }
        }

        return labFeatures;
    }
}

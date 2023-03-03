package com.example.restservice.persistence.LabDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.restservice.model.Lab;
import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.example.restservice.persistence.DropdownDAO.DropdownDAO;

@Component
public class LabFileDAO implements LabDAO {
    Map<String, Lab> labs; // mapping for all labs after loaded from JSON
    private ObjectMapper objectMapper;

    private String filename;

    // this contains a reference to the dropdownDAO, which is used in order
    //      to reference Filters for data combination within labs. Labs use
    //      pre-built data on Buildings and Filters so as to not recreate items
    private DropdownDAO dropdownDAO;

    public LabFileDAO(@Value("${labs.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
    }
    
    public void setDropdownDAO(DropdownDAO dropdownDAO) {
        this.dropdownDAO = dropdownDAO;

        // now that we have a reference to the DropdownDAO, we can load the Labs
        try {
            load(); // function to load all data from JSON file
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
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
    private Lab[] getLabArray(String comparisonTerm) {
        ArrayList<Lab> labArrayList = new ArrayList<>();

        // iterate through all labs loaded in memory and check if
        // any have the same name as the searched term
        // if the comparisonTerm is null, the program will return all Labs
        for (Lab lab : labs.values()) {
            // if the sent in comparison term is null
            //      return every lab in the labs Map
            // if the current lab itterated onto has the same name as the comparison term
            //      add it to the array list of labs
            // else pass over it
            if (comparisonTerm == null || lab.getId().contains(comparisonTerm)) {
                labArrayList.add(lab);
            }
        }

        // finally transform the compiled list of labs into a array of Labs
        // then return it back to whatever requested a array of labs
        Lab[] labArray = new Lab[labArrayList.size()];
        labArrayList.toArray(labArray);
        return labArray;
    }

    /**
     * If no parameter is given, send null to the getLabArray
     * so that all labs in memory are loaded into an array and returned
     * 
     * @return an array of all {@link Lab lab} in the labs Map
     */
    private Lab[] getLabArray() {
        return getLabArray(null);
    }

    /**
     * Loads {@link Lab lab} from the JSON file into the map
     * 
     * @return true if the file was read correctly
     * @throws IOException for when an error occurs
     */
    private boolean load() throws IOException {
        labs = new TreeMap<>();

        // loads all labs from JSON and maps into an array of Labs 
        Lab[] labArray = objectMapper.readValue(new File(filename), Lab[].class);

        // iterate through the array, placing the current lab into the labs Map
        for (Lab lab : labArray) {
            // finds and sets the lab's building
            String building = lab.getBuilding();
            Building labBuilding = this.dropdownDAO.findBuilding(building);
            lab.setLabBuilding(labBuilding);
            // finds all features for a lab
            String[] features = lab.getFeatures();
            Feature[] labFeatures = this.dropdownDAO.findFeatures(features);
            lab.setLabFeatures(labFeatures);
            labs.put(lab.getId(), lab);
        }

        // finish
        return true;
    }

    private boolean saveLabs() throws IOException {
        // loads all labs into an array for saving to JSON
        Lab[] labArray = getLabArray();

        objectMapper.writeValue(new File(filename), labArray);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lab getLab(String name) {
        synchronized(labs) {
            if (labs.containsKey(name)) 
                return labs.get(name);
            else
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lab[] getLabs() throws IOException {
        synchronized(labs) {
            return getLabArray();
        }
    }

    
}

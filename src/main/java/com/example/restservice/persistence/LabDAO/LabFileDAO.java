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
    // to reference Filters for data combination within labs. Labs use
    // pre-built data on Buildings and Filters so as to not recreate items
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
        } catch (IOException e) {
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
            // return every lab in the labs Map
            // if the current lab itterated onto has the same name as the comparison term
            // add it to the array list of labs
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
            // give account its needed values
            this.initializeLab(lab);

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
        synchronized (labs) {
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
        synchronized (labs) {
            return getLabArray();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Lab createLab(Lab lab) throws IOException {
        synchronized (labs) {
            // give account its needed values
            this.initializeLab(lab);

            if (!labs.containsValue(lab)) {
                labs.put(lab.getId(), lab);
                saveLabs(); // may throw an IOException
            }

            return lab;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Lab updateLab(Lab lab) throws IOException {
        synchronized (labs) {
            // give account its needed values
            this.initializeLab(lab);

            // check if the lab already exists
            if (labs.containsKey(lab.getId()) == false)
                return null; // product does not exist
            else 
                labs.get(lab.getId()).deleteSelf();

            // put new lab
            labs.put(lab.getId(), lab);
            saveLabs(); // may throw an IOException
            return lab;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteLab(String name) throws IOException {
        synchronized(labs) {
            if (labs.containsKey(name)) {
                labs.remove(name);
                return saveLabs();
            }
            else
                return false;
        }
    }

    private void initializeLab(Lab lab) {
        // before lab can be read, it must be given access to the specified
        // data
        // those being the building and features
        // building
        Building labBuilding = this.dropdownDAO.findBuilding(lab.getBuilding());
        lab.setLabBuilding(labBuilding);
        // features
        Feature[] labFeatures = this.dropdownDAO.findFeatures(lab.getFeatures());
        lab.setLabFeatures(labFeatures);
    }
}

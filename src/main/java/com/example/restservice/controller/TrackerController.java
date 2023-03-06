package com.example.restservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.restservice.model.Lab;
import com.example.restservice.persistence.DropdownDAO.DropdownDAO;
import com.example.restservice.persistence.LabDAO.LabDAO; 

import com.example.restservice.model.DropdownItems.Item;
import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;

@RestController
@RequestMapping("tracker")
public class TrackerController {
    private static final Logger LOG = Logger.getLogger(TrackerController.class.getName());
	private LabDAO labDAO;
    private DropdownDAO dropdownDAO;

	public TrackerController(LabDAO labDAO, DropdownDAO dropdownDAO) {
        this.dropdownDAO = dropdownDAO;
		this.labDAO = labDAO;

        this.labDAO.setDropdownDAO(this.dropdownDAO);
        this.dropdownDAO.setLabDAO(this.labDAO);
	}

    /**
     * 
     * @param name is a String used to find a specific lab to be returned
     * 
     * @return a responseEntity with a {@link Lab lab} that fits the name
     * of the quieried name with the request<br>
     * OK if lab is found<br>
     * INTERNAL_SERVER_ERROR otherwise
     * Example: Return a lab with the name "New Media Lab" (SUCCEEDS)
     * GET http://localhost:8080/tracker/getLab/New%20Media%20Lab
     * Example: Return a lab with the name "Adobe" (FAILS)
     * GET http://localhost:8080/tracker/getLab/Adobe
     */
	@GetMapping("getLab/{name}")
	public ResponseEntity<Lab> getLab(@PathVariable String name) {
		LOG.info("GET /lab " + name);
        try {
            Lab lab = labDAO.getLab(name);
            if (lab != null)
                return new ResponseEntity<Lab>(lab,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

    /**
     * 
     * @return a responseEntity with a {@link Lab lab} array of all existing
     * labs<br>
     * OK if array is loaded<br>
     * INTERNAL_SERVER_ERROR otherwise
     * Example: Return all Labs
     * GET http://localhost:8080/tracker/getLabs
     */
    @GetMapping("getLabs")
    public ResponseEntity<Lab[]> getLabs() {
        LOG.info("GET /labs");
        try {
            Lab[] labs = labDAO.getLabs();
            if (labs != null)
                return new ResponseEntity<Lab[]>(labs,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Lab} with the provided Lab object
     * 
     * @param lab - The {@link Lab} to create
     * 
     * @return ResponseEntity with created {@link Lab} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Lab} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("createLab")
    public ResponseEntity<Lab> createLab(@RequestBody Lab lab) {
        LOG.info("POST /lab " + lab);

        // Replace below with your implementation
        try {
            Lab newLab = labDAO.createLab(lab);
            if (lab != null)
                return new ResponseEntity<Lab>(newLab,HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Lab} with the provided {@linkplain Lab} object, if it exists
     * 
     * @param lab The {@link Lab} to update
     * 
     * @return ResponseEntity with updated {@link Lab} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("updateLab")
    public ResponseEntity<Lab> updateLab(@RequestBody Lab lab) {
        LOG.info("PUT /lab " + lab);

        try {
            Lab newLab = labDAO.updateLab(lab);
            if (newLab != null) 
                return new ResponseEntity<Lab>(newLab,HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Lab} with the given id
     * 
     * @param id The id of the {@link Lab} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("deleteLab/{id}")
    public ResponseEntity<Boolean> deleteLab(@PathVariable String id) {
        LOG.info("DELETE /lab " + id);

        try {
            Boolean deleted = labDAO.deleteLab(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*********** DROPDOWN REQUESTS ***********/
    @GetMapping("getDropdowns")
    public ResponseEntity<Item[]> getDropdowns() {
        LOG.info("GET /dropdowns");
        try {
            Item[] dropdowns = dropdownDAO.getDropdowns();
            if (dropdowns != null)
                return new ResponseEntity<Item[]>(dropdowns,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Lab} with the provided Lab object
     * 
     * @param lab - The {@link Lab} to create
     * 
     * @return ResponseEntity with created {@link Lab} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Lab} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("createFilter/{dropdownLocation}")
    public ResponseEntity<Item> createProduct(@PathVariable String dropdownLocation, @RequestBody Item filter) {
        LOG.info("POST /filter " + filter.getName() + " @ " + dropdownLocation);

        try {
            Item newFilter = dropdownDAO.createFilter(dropdownLocation, filter);
            if (newFilter != null)
                return new ResponseEntity<Item>(newFilter,HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("moveFilter/{dropdownLocation}/{filterName}")
    public ResponseEntity<Boolean> moveFilter(@PathVariable String dropdownLocation, @PathVariable String filterName) {
        LOG.info("PUT /moveFilter " + filterName + " @ " + dropdownLocation);

        try {
            Boolean result = dropdownDAO.moveFilter(dropdownLocation, filterName);
            if (result)
                return new ResponseEntity<Boolean>(result,HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("updateBuildingInfo")
    public ResponseEntity<Building> updateBuildingInfo(@RequestBody Building building) {
        LOG.info("PUT /building-info " + building);

        try {
            Building newBuilding = dropdownDAO.updateBuildingInfo(building);
            if (newBuilding != null) 
                return new ResponseEntity<Building>(newBuilding,HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("updateBuildingName")
    public ResponseEntity<Building> updateBuildingName(@RequestBody String[] names) {
        LOG.info("PUT /building-name " + names);

        try {
            Building newBuilding = dropdownDAO.updateBuildingName(names);
            if (newBuilding != null) 
                return new ResponseEntity<Building>(newBuilding,HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("updateFeatureName")
    public ResponseEntity<Feature> updateFeatureName(@RequestBody String[] names) {
        LOG.info("PUT /building-name " + names);

        try {
            Feature newFeature = dropdownDAO.updateFeatureName(names);
            if (newFeature != null) 
                return new ResponseEntity<Feature>(newFeature,HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("deleteDropdownItem/{dropdownItemName}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable String dropdownItemName) {
        LOG.info("DELETE /dropdown-item " + dropdownItemName);

        try {
            boolean deleted = dropdownDAO.deleteItem(dropdownItemName);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
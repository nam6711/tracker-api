package com.example.restservice.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.restservice.model.Lab;
import com.example.restservice.persistence.DropdownDAO.DropdownDAO;
import com.example.restservice.persistence.LabDAO.LabDAO;
import com.example.restservice.model.DropdownItems.Item;

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
     * GET http://localhost:8080/tracker/lab/New%20Media%20Lab
     * Example: Return a lab with the name "Adobe" (FAILS)
     * GET http://localhost:8080/tracker/lab/Adobe
     */
	@GetMapping("lab/{name}")
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
     * GET http://localhost:8080/tracker/lab
     */
    @GetMapping("lab")
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


    /*********** DROPDOWN REQUESTS ***********/
    @GetMapping("dropdown")
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
}
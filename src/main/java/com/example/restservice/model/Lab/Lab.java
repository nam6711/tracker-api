package com.example.restservice.model.Lab;  

import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.example.restservice.model.DropdownItems.Filter.Filter;
import com.example.restservice.model.Lab.updaters.LabMeetingContainer;
import com.example.restservice.model.Lab.updaters.LabScheduleUpdater;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lab {
	// JSON features for each lab
	@JsonProperty("name")
	private String name;
	@JsonProperty("number")
	private String number;
	@JsonProperty("building")
	private String building;
	@JsonProperty("features")
	private String[] features; 

	// concrete set of features and buildings (needed so that if
	// a Filter is removed, the Lab is updated instantly and can 
	// reflect that change)
	private Building labBuilding;
	private Feature[] labFeatures;

	// holds schedule for the lab and updates it
	private LabScheduleUpdater lsu;
	private LabMeetingContainer meetings;

	public Lab(@JsonProperty("name") String name,
	@JsonProperty("number") String number,
	@JsonProperty("building") String building,
	@JsonProperty("features") String[] features) {
		this.name = name;
		this.number = number;
		this.building = building;
		this.features = features; 

		// set up the scheduler
		this.meetings = new LabMeetingContainer();
	}

	/**
	 * sets the array of references to Feature instances
	 * 
	 * subscribes to feature instance so that labs are updated if a lab
	 * 		is removed
	 * 
	 * @param features Feature instances the lab will hold
	 */
	public void setLabFeatures(Feature[] features) {
		// if the lab has existing features, unsubscribe from them
		// create a array of strings that represents the features 
		this.features = new String[features.length]; 

		if (this.labFeatures != null) {
			for (Feature feature : this.labFeatures) {
				feature.unsubscribe(this);
			}
		} 
		
		// no set the lab features to the new list
		this.labFeatures = features; 

		// subscribe to the new features
		int i = 0;
		for (Filter filter : this.labFeatures) {
			filter.subscribe(this);
			this.features[i++] = filter.getName();
		}
	}

	public void removeFeature(Filter feature) {
		// create a buffer to hold the new item
        Feature[] featureBuffer = new Feature[this.labFeatures.length - 1]; 

        // iterate through the entire item array and copy it onto the new array
        //      so we can allow the new filter to be added in nicely
        int i = 0;
        for (Feature item : this.labFeatures) {
            if (item != feature)  {
				featureBuffer[i++] = item;
			}
			else {
				feature.unsubscribe(this);
			} 
        }

        // point this.items to the item buffer now
        this.labFeatures = featureBuffer;

		// update the string array that gets sent to the front end
		i = 0;
		this.features = new String[this.labFeatures.length];
		for (Filter filter : this.labFeatures) {
			this.features[i++] = filter.getName();
		}
	}

	/**
	 * unsubscribes self from all existing filters so that the filters
	 * 		don't try and reference non existant Labs
	 */
	public void deleteSelf() {
		for (Feature filter : labFeatures) {
			filter.unsubscribe(this);
		}
		
		this.labBuilding.unsubscribe(this);

		// TODO set the lsu to interrupt and end
	}

	public void setFeatureName(String oldName, String newName) {
		// iterate through the features array and update the names on it
		//		to reflect their new name

		for (int i = 0; i < this.features.length; ++i) {
			if (this.features[i].equals(oldName)) {
				this.features[i] = newName;
				break;
			}
		}
	}

	/**
	 * sets a reference to a lab's building so that the lab can access
	 * 		info like a building's number, abbreviation, etc..
	 * 
	 * @param building Building object that a Lab belongs to
	 */
	public void setLabBuilding(Building building) {
		// if the lab already has a building, unsubscribe from it
		if (this.labBuilding != null && this.labBuilding != building) { 
			this.labBuilding.unsubscribe(this);
		}
		this.labBuilding = building;
		this.building = building.getName();
		this.labBuilding.subscribe(this); 

		// with the new update, set the thread to update
		this.lsu = new LabScheduleUpdater(name, this.getId(), meetings);
	}

	/**
	 * the lab when updated sets its features to match that of the copy
	 * 		lab so we unsubscribe from any existing features
	 * 
	 * on top of that we set the name to whatever we change it to
	 * 
	 * NOTHING ELSE SHOULD CHANGE, IF YOU NEED TO ALTER ROOM NUMBER OR
	 * 		LAB BUILDING, YOULL NEED TO DELETE THIS ONE AND CREATE A NEW LAB
	 * @param lab
	 */
	public void updateSelf(Lab lab) {
		// unsubscribe from old dropdowns and features
		for (Feature feature : this.labFeatures) {
			feature.unsubscribe(this);
		}
		this.labFeatures = null;

		// now update the lab name and number
		this.name = lab.getName(); 
	}

	public String getName() {
		return this.name;
	}

	public String getNumber() {
		return this.number;
	}

	public String getBuilding() {
		return this.building;
	}

	public String[] getFeatures() {
		return this.features;
	}

	public String getId() {
		return this.labBuilding.getNum() + "-" + this.number;
	}

	public LabMeetingContainer getLabMeetingContainer() {
		return this.meetings;
	}

	@Override
	public String toString() {
		return this.name + "," + this.building + "," + this.number; 
	}
}
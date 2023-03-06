package com.example.restservice.model; 

import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
import com.example.restservice.model.DropdownItems.Filter.Filter;
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
	@JsonProperty("schedule") 
	private String[][][] schedule;

	// concrete set of features and buildings (needed so that if
	// a Filter is removed, the Lab is updated instantly and can 
	// reflect that change)
	private Building labBuilding;
	private Feature[] labFeatures;

	public Lab(@JsonProperty("name") String name,
	@JsonProperty("number") String number,
	@JsonProperty("building") String building,
	@JsonProperty("features") String[] features,
	@JsonProperty("schedule") String[][][] schedule) {
		this.name = name;
		this.number = number;
		this.building = building;
		this.features = features;
		this.schedule = schedule;
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
		if (this.labFeatures != null) {
			System.out.println("HERE");
			for (Feature feature : this.labFeatures) {
				feature.unsubscribe(this);
			}
		}
		
		// no set the lab features to the new list
		this.labFeatures = features;
		// subscribe to the new features
		for (Filter filter : this.labFeatures) {
			filter.subscribe(this);
		}
	}

	public void removeFeature(Filter feature) {
		// create a buffer to hold the new item
        Feature[] featureBuffer = new Feature[this.labFeatures.length - 1]; 

        // iterate through the entire item array and copy it onto the new array
        //      so we can allow the new filter to be added in nicely
        int i = 0;
        for (Feature item : this.labFeatures) {
            if (item != feature) 
				featureBuffer[i++] = item;
			else 
				feature.unsubscribe(this);
        }

        // point this.items to the item buffer now
        this.labFeatures = featureBuffer;
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
	}

	public String getName() {
		return this.name;
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
}
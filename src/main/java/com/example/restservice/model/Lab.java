package com.example.restservice.model;

import java.util.Map;

import com.example.restservice.model.DropdownItems.Filter.Building;
import com.example.restservice.model.DropdownItems.Filter.Feature;
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
	@JsonProperty("schedule") private String[][][] schedule;

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

	public String getBuilding() {
		return this.building;
	}

	public String[] getFeatures() {
		return this.features;
	}

	public void setLabFeatures(Feature[] features) {
		this.labFeatures = features;
	}

	/**
	 * sets a reference to a lab's building so that the lab can access
	 * 		info like a building's number, abbreviation, etc..
	 * 
	 * @param building Building object that a Lab belongs to
	 */
	public void setLabBuilding(Building building) {
		this.labBuilding = building;
		// TODO : Set labs up to subscribe to certain features
	}

	public String getId() {
		return this.labBuilding.getNum() + "-" + this.number;
	}
}
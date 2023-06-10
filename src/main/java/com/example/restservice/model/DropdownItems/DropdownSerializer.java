package com.example.restservice.model.DropdownItems;

import java.io.IOException;

import com.example.restservice.model.DropdownItems.Filter.Building;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DropdownSerializer extends StdSerializer<Dropdown[]> {
    
    public DropdownSerializer() {
        this(null);
    }
  
    public DropdownSerializer(Class<Dropdown[]> t) {
        super(t);
    }

    @Override
    public void serialize(
    Dropdown[] value, JsonGenerator jgen, SerializerProvider provider) 
    throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        
        // iterate through all parent dropdowns
        for (Item item : value) {
            Dropdown dropdown = (Dropdown) item;
            writeThroughDropdown(jgen, dropdown);
        }

        jgen.writeEndArray();
    }

    private void writeThroughDropdown(JsonGenerator jgen, Dropdown item) throws IOException {
        // these are dropdowns
        jgen.writeStartObject();
        jgen.writeStringField("type", item.getType());
        jgen.writeStringField("name", item.getName());
        
        // write the array of items
        jgen.writeArrayFieldStart("items");
        for (Item dropdownItem : item.getItems()) {
            // if dropdown, run through the rounds
            if (dropdownItem instanceof Dropdown) {
                Dropdown dropdown = (Dropdown) dropdownItem;
                writeThroughDropdown(jgen, dropdown);
            } else {
                jgen.writeStartObject();
                // generic filter items
                jgen.writeStringField("type", dropdownItem.getType());
                jgen.writeStringField("name", dropdownItem.getName());

                // write building properties
                if (dropdownItem instanceof Building) {
                    Building building = (Building) dropdownItem;
                    jgen.writeStringField("abbreviation", building.getAbbreviation());
                    jgen.writeStringField("num", building.getNum());
                }
                jgen.writeEndObject();
            }
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}

package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Compartment> compartments;

    public Inventory() {
    	this.compartments = new ArrayList<>();
    	this.compartments.add(new Compartment("Food", 100));
    	this.compartments.add(new Compartment("Munitions", 100));
    	this.compartments.add(new Compartment("Other", 100));
    }

    public List<Compartment> getCompartments() {
        return this.compartments;
    }

    public boolean hasFreeSpace() {
        return this.compartments.stream().anyMatch(Compartment::hasFreeSpace);
    }
}

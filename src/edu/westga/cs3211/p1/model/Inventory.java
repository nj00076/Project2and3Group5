package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Compartment> compartments;

    public Inventory() {
        compartments = new ArrayList<>();
        compartments.add(new Compartment("Food", 100));
        compartments.add(new Compartment("Munitions", 100));
        compartments.add(new Compartment("Other", 100));
    }

    public List<Compartment> getCompartments() {
        return compartments;
    }

    public boolean hasFreeSpace() {
        return compartments.stream().anyMatch(Compartment::hasFreeSpace);
    }
}

package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Compartment> compartments;

    public Inventory() {
        this.compartments = new ArrayList<>();

        List<Stock.SpecialQuality> foodQuals = List.of(
            Stock.SpecialQuality.PERISHABLE,
            Stock.SpecialQuality.LIQUID,
            Stock.SpecialQuality.FLAMMABLE
        );
        this.compartments.add(new Compartment("Food", 300, foodQuals));

        List<Stock.SpecialQuality> munitionsQuals = List.of(
            Stock.SpecialQuality.PERISHABLE,
            Stock.SpecialQuality.FLAMMABLE
        );
        this.compartments.add(new Compartment("Munitions", 150, munitionsQuals));

        List<Stock.SpecialQuality> otherQuals = List.of(
            Stock.SpecialQuality.PERISHABLE,
            Stock.SpecialQuality.LIQUID,
            Stock.SpecialQuality.FLAMMABLE
        );
        this.compartments.add(new Compartment("Other", 100, otherQuals));
    }

    public List<Compartment> getCompartments() {
        return this.compartments;
    }

    public void addCompartment(Compartment compartment) {
        this.compartments.add(compartment);
    }

    public List<Compartment> getCompatibleCompartments(Stock stock) {
        List<Compartment> result = new ArrayList<>();
        for (Compartment compartment : this.compartments) {
            if (compartment.canStore(stock)) {
                result.add(compartment);
            }
        }
        return result;
    }
}

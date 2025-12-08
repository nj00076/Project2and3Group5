package edu.westga.cs3211.p1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an inventory containing multiple compartments.
 * Each compartment can store stock items according to its capacity
 * and allowed qualities.
 * @author nj00076
 * @version cs3211
 */
public class Inventory {

    private List<Compartment> compartments;

    /**
     * Constructs an Inventory with default compartments:
     * - Food (capacity 300, allows PERISHABLE, LIQUID, FLAMMABLE)
     * - Munitions (capacity 150, allows PERISHABLE, FLAMMABLE)
     * - Other (capacity 100, allows PERISHABLE, LIQUID, FLAMMABLE)
     */
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

    /**
     * Returns the list of compartments in this inventory.
     * Modifications to the returned list will affect the inventory.
     *
     * @return the list of compartments
     */
    public List<Compartment> getCompartments() {
        return this.compartments;
    }

    /**
     * Adds a new compartment to this inventory.
     *
     * @param compartment the compartment to add
     */
    public void addCompartment(Compartment compartment) {
        this.compartments.add(compartment);
    }

    /**
     * Returns a list of compartments that can store the given stock.
     * A compartment is compatible if it has enough free space
     * and allows all special qualities of the stock.
     *
     * @param stock the stock to check
     * @return a list of compatible compartments
     */
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

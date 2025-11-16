package edu.westga.cs3211.p1.model;

public class Stock {
    private String name;
    private int size;
    private String condition;
    private String expirationDate;
    private String specialQuals;

    public Stock(String name, int size, String condition, String expirationDate, String specialQuals) {
        this.name = name;
        this.size = size;
        this.condition = condition;
        this.expirationDate = expirationDate;
        this.specialQuals = specialQuals;
    }

    public String getName() { 
    	return this.name; 
    }
    
    public int getSize() { 
    	return this.size; 
    }
    
    public String getCondition() { 
    	return this.condition; 
    }
    
    public String getExpirationDate() { 
    	return this.expirationDate; 
    }
    
    public String getSpecialQuals() { 
    	return this.specialQuals; 
    }
}

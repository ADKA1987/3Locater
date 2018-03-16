package a3locater.tre.se.a3locater.domain;

import java.util.List;
import java.util.Set;

public class Location {

    private List<String> floors;
    private List<String> areas;
    private List<String> desks;
    private List<String> floorPlans;

    public Location() {
    }

    public List<String> getFloors() {
        return floors;
    }

    public void setFloors(List<String> floors) {
        this.floors = floors;
    }

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    public List<String> getDesks() {
        return desks;
    }

    public void setDesks(List<String> desks) {
        this.desks = desks;
    }

    public List<String> getFloorPlans() {
        return floorPlans;
    }

    public void setFloorPlans(List<String> floorPlans) {
        this.floorPlans = floorPlans;
    }
}

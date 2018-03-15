package a3locater.tre.se.a3locater.domain;

import java.util.List;
import java.util.Set;

public class Location {

    private Set<String> floors;
    private Set<String> areas;
    private Set<String> desks;

    public Location() {
    }

    public Set<String> getFloors() {
        return floors;
    }

    public void setFloors(Set<String> floors) {
        this.floors = floors;
    }

    public Set<String> getAreas() {
        return areas;
    }

    public void setAreas(Set<String> areas) {
        this.areas = areas;
    }

    public Set<String> getDesks() {
        return desks;
    }

    public void setDesks(Set<String> desks) {
        this.desks = desks;
    }
}

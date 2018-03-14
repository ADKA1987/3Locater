package a3locater.tre.se.a3locater.domain;

public class EmployeeLocation {
    private String empName;
    private String location;
    private String locationImage;

    public EmployeeLocation(String empName, String location, String locationImage) {
        this.empName = empName;
        this.location = location;
        this.locationImage = locationImage;
    }

    public String getEmpName() {
        return empName;
    }

    public String getLocation() {
        return location;
    }

    public String getLocationImage() {
        return locationImage;
    }

    @Override
    public String toString() {
        return "EmployeeLocation{" +
                "empName='" + empName + '\'' +
                ", location='" + location + '\'' +
                ", locationImage='" + locationImage + '\'' +
                '}';
    }
}

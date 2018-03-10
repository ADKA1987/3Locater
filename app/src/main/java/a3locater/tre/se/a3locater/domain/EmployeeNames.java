package a3locater.tre.se.a3locater.domain;

/**
 * Created by LENOVO on 3/10/2018.
 */

public class EmployeeNames {
    private String empName;
    private String location;
    private String locationImage;

    public EmployeeNames(String empName, String location, String locationImage) {
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
        return "EmployeeNames{" +
                "empName='" + empName + '\'' +
                ", location='" + location + '\'' +
                ", locationImage='" + locationImage + '\'' +
                '}';
    }
}

package a3locater.tre.se.a3locater.domain;

public class EmployeeLocation {
    private String empName;
    private String location;
    private String locationImage;
    private String profilePic;
    public EmployeeLocation(String empName, String location, String locationImage,String profilePic) {
        this.empName = empName;
        this.location = location;
        this.locationImage = locationImage;
        this.profilePic = profilePic;
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

    public String getProfilePic(){ return  profilePic;}
    @Override
    public String toString() {
        return "EmployeeLocation{" +
                "empName='" + empName + '\'' +
                ", location='" + location + '\'' +
                ", locationImage='" + locationImage + '\'' +
                ", profilePic="+profilePic+"}";
    }
}

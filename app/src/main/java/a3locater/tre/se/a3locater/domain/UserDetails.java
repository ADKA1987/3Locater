package a3locater.tre.se.a3locater.domain;

import java.sql.Blob;

/**
 * Created by LENOVO on 3/12/2018.
 */

public class UserDetails {
    private String empId;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private String team;
    private String profilePic;

    public UserDetails(String empId, String name, String email, String mobileNumber, String role, String team, String profilePic) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.team = team;
        this.profilePic = profilePic;
    }

    public String getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getRole() {
        return role;
    }

    public String getTeam() {
        return team;
    }

    public String getProfilePic() {
        return profilePic;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "empId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", role='" + role + '\'' +
                ", team='" + team + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}

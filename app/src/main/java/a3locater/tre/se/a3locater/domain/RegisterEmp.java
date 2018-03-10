package a3locater.tre.se.a3locater.domain;

/**
 * Created by LENOVO on 3/10/2018.
 */

public class RegisterEmp {
    private String empEmail;
    private String location;

    public RegisterEmp(String empEmail, String location) {
        this.empEmail = empEmail;
        this.location = location;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "RegisterEmp{" +
                "empEmail='" + empEmail + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

package sit.int221.projectintegrate.DTO;

public class AuthenticationResponse {

    private String jwt;

    private String roles;

    private String email;

    public  String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getJwt() {
        return jwt;
    }


    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    public String getRoles() {
        return roles;
    }


    public void setRoles(String roles) {
        this.roles = roles;
    }

    public AuthenticationResponse(String jwt ,String roles ,String email) {
        this.jwt = jwt;
        this.roles = roles;
        this.email = email;
    }
}

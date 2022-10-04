package sit.int221.projectintegrate.DTO;

public class AuthenticationResponse {

    private String jwt;

    private String roles;

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

    public AuthenticationResponse(String jwt ,String roles) {
        this.jwt = jwt;
        this.roles = roles;
    }
}

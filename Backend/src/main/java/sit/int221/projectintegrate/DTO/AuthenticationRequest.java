package sit.int221.projectintegrate.DTO;

public class AuthenticationRequest {
    private String email;
    private String userpassword;

    public String getEmail(){return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUserpassword(){return userpassword;}

    public void setUserpassword(String userpassword) {this.userpassword = userpassword;}

}

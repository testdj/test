package rs.ac.uns.naucnacentrala.security.auth;

public class JwtAuthenticationRequest {

    private String password;
    private String username;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setPassword(password);
        this.setUsername(username);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

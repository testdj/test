package rs.ac.uns.naucnacentrala.dto;

public class UserTokenState {

    private Long expiresIn;
    private String accessToken;

    public UserTokenState() {
        this.expiresIn = null;
        this.accessToken = null;
    }

    public UserTokenState(String accessToken, long expiresIn) {
        this.expiresIn = expiresIn;
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
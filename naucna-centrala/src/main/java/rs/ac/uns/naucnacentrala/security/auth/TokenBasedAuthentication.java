package rs.ac.uns.naucnacentrala.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenBasedAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final UserDetails principle;
	private String token;

	public TokenBasedAuthentication(UserDetails principle) {
		super(principle.getAuthorities());
		this.principle = principle;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public UserDetails getPrincipal() {
		return principle;
	}

}

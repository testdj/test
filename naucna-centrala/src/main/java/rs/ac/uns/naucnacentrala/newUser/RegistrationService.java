package rs.ac.uns.naucnacentrala.newUser;

import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.UserDTO;

import java.util.List;


public interface RegistrationService {

    public abstract List<UserDTO> getAllNotActivated(String username);
    public abstract UserDTO getUserFromProcessVariables(String processInstanceID);
}

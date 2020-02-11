package rs.ac.uns.naucnacentrala.newJournal;

import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.User;

import java.util.List;

public interface JournalService {

    public abstract List<CasopisDTO> getAllNotActivated(String username);
    public abstract CasopisDTO getCasopisFromProcessVariables(String processInstanceID);
}

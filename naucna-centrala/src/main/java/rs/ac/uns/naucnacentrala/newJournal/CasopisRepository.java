package rs.ac.uns.naucnacentrala.newJournal;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.naucnacentrala.model.Casopis;

import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis,Long> {

    public Casopis findByIssn(String issn);

    public Casopis findByProcessInstanceId(String processInstanceId);

    public Casopis findByNaziv(String naziv);

    public List<Casopis> findAllByEnabled(Boolean enabled);

    public List<Casopis> findByGlavniUrednik(String glavniUrednik);

}

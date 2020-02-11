package rs.ac.uns.naucnacentrala.authorisation;


import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.naucnacentrala.model.Authority;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findOneByName(String name);

}

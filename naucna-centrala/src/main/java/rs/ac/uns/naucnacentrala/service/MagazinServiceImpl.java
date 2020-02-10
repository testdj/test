package rs.ac.uns.naucnacentrala.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MagazinServiceImpl implements MagazinService {

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public List<Casopis> getAllActivated() {
        return casopisRepository.findAllByEnabled(true);
    }

    @Override
    public Casopis save(Casopis casopis) {
        return casopisRepository.save(casopis);
    }

    @Override
    public Casopis getOne(Long id){
        return casopisRepository.getOne(id);
    }


}

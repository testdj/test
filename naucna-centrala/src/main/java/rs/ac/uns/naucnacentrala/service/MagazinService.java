package rs.ac.uns.naucnacentrala.service;

import rs.ac.uns.naucnacentrala.model.Casopis;

import java.util.List;

public interface MagazinService {

    List<Casopis> getAllActivated();

    Casopis save(Casopis casopis);

    Casopis getOne(Long id);
}

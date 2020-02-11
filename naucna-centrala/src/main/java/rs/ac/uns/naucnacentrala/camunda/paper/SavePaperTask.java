package rs.ac.uns.naucnacentrala.camunda.paper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.Paper;
import rs.ac.uns.naucnacentrala.newJournal.CasopisRepository;
import rs.ac.uns.naucnacentrala.newPaper.PaperRepository;
import rs.ac.uns.naucnacentrala.newUser.UserRepository;
import rs.ac.uns.naucnacentrala.storage.StorageProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SavePaperTask implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String naslov = execution.getVariable("naslov").toString();
        String apstrakt = execution.getVariable("apstrakt").toString();
        String kljucniPojmovi = execution.getVariable("kljucni_pojmovi").toString();
        String pdfPath =  "/" + execution.getVariable("pdf").toString();
        Long noId = Long.valueOf(execution.getVariable("naucna_oblast").toString());

        Paper paper = new Paper();
        paper.setNaslov(naslov);
        paper.setApstrakt(apstrakt);
        paper.setPojmovi(kljucniPojmovi);
        paper.setPdf(pdfPath);
        paper.setNaucnaOblastId(noId);

    }
}
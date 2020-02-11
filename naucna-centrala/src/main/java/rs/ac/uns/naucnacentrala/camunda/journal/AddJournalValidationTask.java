package rs.ac.uns.naucnacentrala.camunda.journal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.newJournal.JournalService;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.newJournal.CasopisRepository;
import rs.ac.uns.naucnacentrala.newUser.UserRepository;
import rs.ac.uns.naucnacentrala.newJournal.MagazinService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;

@Service
public class AddJournalValidationTask implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JournalService journalService;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    MagazinService magazinService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CasopisDTO casopis = journalService.getCasopisFromProcessVariables(execution.getProcessInstanceId());
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        System.out.println(casopis.getNaziv());
        System.out.println(casopis.getIssn());
        boolean flag_val=true;
        HashMap<String,String> valErrors=new HashMap<String,String>();
        Set<ConstraintViolation<CasopisDTO>> violations = validator.validate(casopis);
        for (ConstraintViolation<CasopisDTO> violation : violations) {
            valErrors.put(violation.getPropertyPath().toString(),violation.getMessage());
            flag_val=false;
        }
        if(!flag_val) {
            execution.setVariable("validationErrors", valErrors);
        }else{
            Casopis maybe=casopisRepository.findByProcessInstanceId(execution.getProcessInstanceId());
            if(casopisRepository.findByIssn(casopis.getIssn())!=null && casopisRepository.findByIssn(casopis.getIssn()).getId()!=maybe.getId()){
                valErrors.put("issn", "Already exists");
                execution.setVariable("validationErrors", valErrors);
                flag_val = false;
            }else {
                Casopis realCasopis= ObjectMapper.map(casopis,Casopis.class);
                execution.setVariable("validationErrors", null);
                realCasopis.setProcessInstanceId(execution.getProcessInstanceId());
                if(maybe!=null){
                    realCasopis.setId(maybe.getId());
                }
                String glavniUrednik=SecurityContextHolder.getContext().getAuthentication().getName();
                realCasopis.setCasopisStatus(CasopisStatus.WAITING_FOR_INPUT);
                realCasopis.setGlavniUrednik(glavniUrednik);
                magazinService.save(realCasopis);
            }
        }
        execution.setVariable("flag_val",flag_val);
    }

}

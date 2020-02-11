package rs.ac.uns.naucnacentrala.camunda.journal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.newJournal.CasopisRepository;

@Service
public class ResetStatusTask implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Casopis casopis=casopisRepository.findByProcessInstanceId(delegateExecution.getProcessInstanceId());
        casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_INPUT);
        casopisRepository.save(casopis);
    }

}

package rs.ac.uns.naucnacentrala.camunda.paper;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class ValidatePaper implements JavaDelegate {



    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        boolean flag_r = true;

        delegateExecution.setVariable("flag_r",flag_r);
    }
}

package rs.ac.uns.naucnacentrala.camunda.paper;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.newUser.NaucnaOblastRepository;

@Service
public class SetNOViewCompleteListener implements TaskListener {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        String noId = runtimeService.getVariable(delegateTask.getProcessInstanceId(),"naucna_oblast").toString();
        for (NaucnaOblast naucnaOblast : naucnaOblastRepository.findAll()) {
            if (Long.valueOf(noId) == naucnaOblast.getId()) {
                runtimeService.setVariable(delegateTask.getProcessInstanceId(), "noDefValue", naucnaOblast.getName());
                break;
            }
        }
    }
}

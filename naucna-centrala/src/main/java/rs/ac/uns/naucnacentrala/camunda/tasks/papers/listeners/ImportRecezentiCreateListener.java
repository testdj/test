package rs.ac.uns.naucnacentrala.camunda.tasks.papers.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.types.MultiSelectionFormType;
import rs.ac.uns.naucnacentrala.camunda.types.MultiSelectionStringFormType;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class ImportRecezentiCreateListener implements TaskListener {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FormService formService;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<FormField> formFieldList = formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if (formFieldList != null) {
            for (FormField field : formFieldList) {
                if (field.getId().equals("selectedRecezenti")) {
                    String noId = runtimeService.getVariable(delegateTask.getProcessInstanceId(),"naucna_oblast").toString();
                    CasopisPV casopis = (CasopisPV)runtimeService.getVariable(delegateTask.getProcessInstanceId(),"casopis");
                    Map<String, String> items = ((MultiSelectionStringFormType) field.getType()).getValues();
                    for (String recezent : casopis.getRecezenti().get(noId)) {
                        items.put(recezent, recezent);
                    }
                }
            }
        }
    }
}
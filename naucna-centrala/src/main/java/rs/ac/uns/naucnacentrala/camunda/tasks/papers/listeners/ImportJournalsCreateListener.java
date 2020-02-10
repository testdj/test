package rs.ac.uns.naucnacentrala.camunda.tasks.papers.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.service.MagazinService;

import java.util.List;
import java.util.Map;

@Service
public class ImportJournalsCreateListener implements TaskListener {

    @Autowired
    FormService formService;

    @Autowired
    MagazinService magazinService;

    @Override
    public void notify(DelegateTask delegateTask) {

        List<FormField> formFieldList=formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if(formFieldList!=null){
            for(FormField field : formFieldList){
                if( field.getId().equals("selBox1")){
                    Map<String,String> items = ((EnumFormType)field.getType()).getValues();
                    for(Casopis casopis : magazinService.getAllActivated()){
                        items.put(casopis.getId().toString(),casopis.getNaziv());
                    }
                }

            }
        }

    }

}

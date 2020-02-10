package rs.ac.uns.naucnacentrala.camunda.tasks.journals.add.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.types.MultiSelectionFormType;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.repository.NacinPlacanjaRepository;
import rs.ac.uns.naucnacentrala.repository.NaucnaOblastRepository;

import java.util.List;
import java.util.Map;

@Service
public class AddJournalFormTaskListener implements TaskListener {

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    FormService formService;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<FormField> formFieldList=formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if(formFieldList!=null){
            for(FormField field : formFieldList){
                if( field.getId().equals("naucne_oblasti")){
                    Map<Long,String> items = ((MultiSelectionFormType)field.getType()).getValues();
                    for(NaucnaOblast naucnaOblast : naucnaOblastRepository.findAll()){
                        items.put(naucnaOblast.getId(),naucnaOblast.getName());
                    }
                }
                if( field.getId().equals("nacin_placanja")){
                    Map<Long,String> items = ((MultiSelectionFormType)field.getType()).getValues();
                    for(NacinPlacanja nacinPlacanja : nacinPlacanjaRepository.getAll()){
                        items.put(nacinPlacanja.getId(),nacinPlacanja.getName());
                    }
                }
            }
        }
    }

}

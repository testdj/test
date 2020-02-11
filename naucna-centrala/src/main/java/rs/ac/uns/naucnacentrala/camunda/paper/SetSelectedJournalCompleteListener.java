package rs.ac.uns.naucnacentrala.camunda.paper;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.newJournal.JournalService;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.newJournal.MagazinService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetSelectedJournalCompleteListener implements TaskListener {

    @Autowired
    JournalService journalService;

    @Autowired
    MagazinService magazinService;

    @Autowired
    FormService formService;

    @Override
    public void notify(DelegateTask delegateTask) {

        List<FormField> formFieldList=formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if(formFieldList!=null){
            for(FormField field : formFieldList){
                if( field.getId().equals("selBox1")){
                    Casopis casopis = magazinService.getOne(Long.valueOf(field.getValue().getValue().toString()));
                    CasopisPV casopisPV= ObjectMapper.map(casopis, CasopisPV.class);
                    for(User recezent : casopis.getRecezenti()){
                        for(NaucnaOblast no : recezent.getNaucneOblasti()){
                            ArrayList<String> noRecezenti = casopisPV.getRecezenti().get(no.getId().toString());
                            if(noRecezenti==null){
                                noRecezenti = new ArrayList<>();
                            }
                            noRecezenti.add(recezent.getUsername());
                            casopisPV.getRecezenti().put(no.getId().toString(),noRecezenti);
                        }
                    }
                    for(User urednik : casopis.getUrednici()){
                        for(NaucnaOblast no : urednik.getNaucneOblasti()){
                            casopisPV.getUrednici().put(no.getId().toString(),urednik.getUsername());
                        }
                    }
                    delegateTask.getExecution().setVariable("casopis", casopisPV);
                }
            }
        }

    }

}

package rs.ac.uns.naucnacentrala.camunda.service;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.LongValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.NacinPlacanjaDTO;
import rs.ac.uns.naucnacentrala.dto.NaucnaOblastDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.NacinPlacanjaRepository;
import rs.ac.uns.naucnacentrala.repository.NaucnaOblastRepository;
import rs.ac.uns.naucnacentrala.utils.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JournalServiceImpl implements JournalService{

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    private static final String[] variableArr={"naziv","issn","cena","prezcena","nacin_placanja","naucne_oblasti","ko_placa"};

    @Override
    public CasopisDTO getCasopisFromProcessVariables(String processInstanceID) {
        CasopisDTO casopis=new CasopisDTO();
        ArrayList<String> variableNames=new ArrayList<String>(Arrays.asList(variableArr));
        VariableMap map=runtimeService.getVariablesTyped(processInstanceID,variableNames,true);

        StringValue issnVal=map.getValueTyped("issn");
        casopis.setIssn(issnVal.getValue());
        StringValue nazivVal=map.getValueTyped("naziv");
        casopis.setNaziv(nazivVal.getValue());
        TypedValue koPlacaVal=map.getValueTyped("ko_placa");
        casopis.setKoPlaca(koPlacaVal.getValue().toString());
        LongValue cenaVal=map.getValueTyped("cena");
        casopis.setCena(cenaVal.getValue());
        ObjectValue nacinPlacanjaValue=map.getValueTyped("nacin_placanja");
        for(String id : (List<String>) nacinPlacanjaValue.getValue()){
            NacinPlacanja np=nacinPlacanjaRepository.getOne(Long.valueOf(id));
            NacinPlacanjaDTO npDTO= ObjectMapper.map(np,NacinPlacanjaDTO.class);
            casopis.getNaciniPlacanja().add(npDTO);
        }
        ObjectValue naucneOblastiValue=map.getValueTyped("naucne_oblasti");
        for(String id : (List<String>) naucneOblastiValue.getValue()){
            NaucnaOblast no=naucnaOblastRepository.getOne(Long.valueOf(id));
            NaucnaOblastDTO noDTO= ObjectMapper.map(no,NaucnaOblastDTO.class);
            casopis.getNaucneOblasti().add(noDTO);
        }

        return casopis;
    }

    @Override
    public List<CasopisDTO> getAllNotActivated(String username) {
        ArrayList<CasopisDTO> casopisi=new ArrayList<CasopisDTO>();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(username).active().taskName("Pregled unetih podataka").list();

        for(Task task : taskList){
            Casopis casopis=casopisRepository.findByProcessInstanceId(task.getProcessInstanceId());
            if(casopis!=null) {
                CasopisDTO casopisDTO = ObjectMapper.map(casopis, CasopisDTO.class);
                casopisDTO.setTaskId(task.getId());
                casopisi.add(casopisDTO);
            }
        }
        return casopisi;
    }
}

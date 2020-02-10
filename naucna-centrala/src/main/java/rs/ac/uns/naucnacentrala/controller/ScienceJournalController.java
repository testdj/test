package rs.ac.uns.naucnacentrala.controller;

import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.utils.Utils;
import rs.ac.uns.naucnacentrala.utils.ObjectMapper;

import java.util.List;

@Controller
@RequestMapping("/restapi/journal")
@CrossOrigin("*")
public class ScienceJournalController {

    private static final String PROCESS_KEY="dodavanjeCasopisa";

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    Utils utils;

    @PreAuthorize("hasRole('UREDNIK')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<FormFieldsDto> startProcess(){

        Task task = utils.startProcess(PROCESS_KEY);
        return new ResponseEntity<>(utils.createFormDTO(task,task.getProcessInstanceId()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    public ResponseEntity getAllMine(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CasopisDTO> retList= ObjectMapper.mapAll(casopisRepository.findByGlavniUrednik(username), CasopisDTO.class);
        return ResponseEntity.ok().body(retList);
    }

}

package rs.ac.uns.naucnacentrala.camunda;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.value.BooleanValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.naucnacentrala.newJournal.JournalService;
import rs.ac.uns.naucnacentrala.newUser.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.dto.FormSubmissionDto;
import rs.ac.uns.naucnacentrala.dto.TaskDTO;
import rs.ac.uns.naucnacentrala.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/restapi/bpmn")
@CrossOrigin("*")
public class BPMNController {

    private static final String UREDNIK_PROCESS_KEY="registracijaUrednika";
    private static final String VALIDATION_FLAG_VARIABLE="flag_val";
    private static final String VALIDATION_ERRORS_VARIABLE="validationErrors";

    @Autowired
    Utils utils;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    IdentityService identityService;

    @Autowired
    FormService formService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    JournalService journalService;

    @PreAuthorize("hasRole('UREDNIK') or hasRole('ADMIN') or hasRole('AUTHORS') or hasRole('RECEZENT')")
    @RequestMapping(method = RequestMethod.POST, value = "/protected/form/{taskId}")
    public ResponseEntity postProtected(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        HashMap<String, Object> map = utils.mapListToDto(dto);
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId=task.getProcessInstanceId();
        if(task.getAssignee().equals(auth.getName())) {
            formService.submitTaskForm(taskId, map);
            if(map.get("koautori")!=null) {
                runtimeService.setVariable(processInstanceId,"koautori", map.get("koautori"));
            }
        }else{
            return ResponseEntity.status(403).build();
        }


        List<Execution> executions=runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        if(executions.size()==0){
            return  ResponseEntity.status(201).build();
        }else {
            BooleanValue validationValue = runtimeService.getVariableTyped(processInstanceId, VALIDATION_FLAG_VARIABLE);
            if(validationValue!=null){
                if (validationValue.getValue()) {
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
                else {
                    ObjectValue valErrorsVal = runtimeService.getVariableTyped(task.getProcessInstanceId(), VALIDATION_ERRORS_VARIABLE);
                    HashMap<String, String> valErrors = (HashMap<String, String>) valErrorsVal.getValue();
                    Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0);
                    valErrors.put("taskID", activeTask.getId());
                    return ResponseEntity.badRequest().body(valErrors);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/form/{taskId}")
    public ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println("TASKKKKKKKKKKK: " + task);
        HashMap<String, Object> map = utils.mapListToDto(dto);
        String processInstanceId=task.getProcessInstanceId();
        System.out.println("INSTANCE ID: " + processInstanceId);
        if(task.getName().equals("Unos podataka i naucnih oblasti")) {
            System.out.println("TASKKK NAMEEE: " + task.getName());
            System.out.println("MAPAAAAAAAAAAAAA: " + map.toString());
            formService.submitTaskForm(taskId, map);

            Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
            if (execution == null) {
                return ResponseEntity.status(201).build();
            } else {
                BooleanValue validationValue = runtimeService.getVariableTyped(processInstanceId, VALIDATION_FLAG_VARIABLE);
                if (validationValue.getValue()) {
                    return new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    ObjectValue valErrorsVal = runtimeService.getVariableTyped(task.getProcessInstanceId(), VALIDATION_ERRORS_VARIABLE);
                    HashMap<String, String> valErrors = (HashMap<String, String>) valErrorsVal.getValue();
                    Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0);
                    valErrors.put("taskID", activeTask.getId());
                    return ResponseEntity.badRequest().body(valErrors);
                }
            }
        }else{
            return ResponseEntity.status(403).build();
        }
    }


    @PreAuthorize("hasRole('UREDNIK') or hasRole('AUTHORS')")
    @RequestMapping(method = RequestMethod.GET, value = "/task/active/{processInstanceId}")
    public ResponseEntity getActiveTask(@PathVariable String processInstanceId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        Task task = taskService.createTaskQuery().active().processInstanceId(processInstanceId).singleResult();
        if(task!=null) {
            if (task.getAssignee().equals(auth.getName())) {
                return ResponseEntity.ok().body(utils.createFormDTO(task, processInstanceId));
            } else {
                return ResponseEntity.status(403).build();
            }
        }else{
            return ResponseEntity.status(404).build();
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/admin/registration")
    public ResponseEntity getActiveAdminRegTasks() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        return ResponseEntity.ok().body(registrationService.getAllNotActivated(auth.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/admin/journal")
    public ResponseEntity getActiveAdminJournalTasks() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        return ResponseEntity.ok().body(journalService.getAllNotActivated(auth.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/urednik")
    public ResponseEntity<FormFieldsDto> startProcessUrednik(){
        Task task = utils.startProcess(UREDNIK_PROCESS_KEY);
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(utils.createFormDTO(task,task.getProcessInstanceId()), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/tasks/{id}")
    public ResponseEntity getMyTask(@PathVariable String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Task task= taskService.createTaskQuery().taskId(id).singleResult();

        if(task!=null) {
            if (task.getAssignee().equals(auth.getName())) {
                return ResponseEntity.ok().body(utils.createFormDTO(task, task.getProcessInstanceId()));
            } else {
                return ResponseEntity.status(403).build();
            }
        }else{
            return ResponseEntity.status(404).build();
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    public ResponseEntity getMyTasks(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTHHHHHHHHHHHH: " + auth.getCredentials());
        List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(auth.getName()).processDefinitionKey("obradaNaucnogRada").list();
        System.out.println("TASKKKKKKKKKKKKS: " + tasks.size());

        ArrayList<TaskDTO> ret = new ArrayList<>();
        for(Task task : tasks){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTaskId(task.getId());
            taskDTO.setName(task.getName());
            System.out.println("TASKKKKKK: " +task.getProcessInstanceId());
            String rad=runtimeService.getVariable(task.getProcessInstanceId(),"naslov").toString();
            System.out.println("RAAAAAAAAAAAAAAAD: " + rad);
            taskDTO.setProcessInstanceId(task.getProcessInstanceId());
            taskDTO.setRad(rad);
            ret.add(taskDTO);
        }

        return ResponseEntity.ok().body(ret);
    }


}

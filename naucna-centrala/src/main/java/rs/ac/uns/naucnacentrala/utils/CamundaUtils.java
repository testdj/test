package rs.ac.uns.naucnacentrala.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.dto.FormSubmissionDto;
import rs.ac.uns.naucnacentrala.dto.KoautorDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class CamundaUtils {

    @Autowired
    FormService formService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ObjectMapper objectMapper;

    public FormFieldsDto createFormDTO(Task task, String processInstanceId){
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new FormFieldsDto(task.getId(),processInstanceId,properties);
    }

    public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            //String escaped=temp.getFieldValue().toString().replaceAll("\"","\\\\\"");
            System.out.println(temp.getFieldId()+":   "+temp.getFieldValue());
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    public Task startProcess(String processKey) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            identityService.setAuthenticatedUserId(auth.getName());
        }

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey);

        Task task=taskService.createTaskQuery().active().processInstanceId(pi.getId()).list().get(0);

        return  task;
    }

}

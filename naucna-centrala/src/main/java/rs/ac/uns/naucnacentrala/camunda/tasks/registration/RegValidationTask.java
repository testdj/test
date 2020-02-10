package rs.ac.uns.naucnacentrala.camunda.tasks.registration;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.service.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.LoginService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class RegValidationTask implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    RegistrationService registrationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception{
        HashMap<String,String> valErrors=new HashMap<String,String>();
        UserDTO user = registrationService.getUserFromProcessVariables(execution.getProcessInstanceId());
        boolean flag_val=true;
        if(!flag_val) {
            execution.setVariable("validationErrors", valErrors);
        }else{
            if(userRepository.findByUsername(user.getUsername())!=null){
                valErrors.put("username", "Already exists");
                execution.setVariable("validationErrors", valErrors);
                flag_val = false;
            }else {
                User realUser=ObjectMapperUtils.map(user,User.class);
                realUser.setEnabled(false);
                System.out.println("REAL USERRRRRRRRRRRRRRRRRRRRRRRR: " + realUser.getUsername() + realUser.getEmail());
                execution.setVariable("validationErrors", null);
                loginService.register(realUser, "ROLE_USER");
            }
        }
        execution.setVariable("flag_val",flag_val);
    }

}

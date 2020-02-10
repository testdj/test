package rs.ac.uns.naucnacentrala.camunda.tasks.registration;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.service.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.LoginService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;

@Service
public class RegUrednikValidationTask implements JavaDelegate {

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
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        boolean flag_val=true;
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        for (ConstraintViolation<UserDTO> violation : violations) {
            valErrors.put(violation.getPropertyPath().toString(),violation.getMessage());
            flag_val=false;
        }
        if(!flag_val) {
            execution.setVariable("validationErrors", valErrors);
        }else{
            if(userRepository.findByUsername(user.getUsername())!=null){
                valErrors.put("username", "Already exists");
                execution.setVariable("validationErrors", valErrors);
                flag_val = false;
            }else {
                execution.setVariable("validationErrors", null);
                User realUser= ObjectMapper.map(user,User.class);
                realUser.setEnabled(true);
                loginService.register(realUser, "ROLE_UREDNIK");
            }
        }
        execution.setVariable("flag_val",flag_val);
    }

}

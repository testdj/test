package rs.ac.uns.naucnacentrala.camunda.user;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.newUser.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.newUser.UserRepository;
import rs.ac.uns.naucnacentrala.authorisation.LoginService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapper;

import java.util.HashMap;

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
                User realUser= ObjectMapper.map(user,User.class);
                realUser.setEnabled(false);
                System.out.println("REAL USERRRRRRRRRRRRRRRRRRRRRRRR: " + realUser.getUsername() + realUser.getEmail());
                execution.setVariable("validationErrors", null);
                loginService.register(realUser, "ROLE_USER");
            }
        }
        execution.setVariable("flag_val",flag_val);
    }

}

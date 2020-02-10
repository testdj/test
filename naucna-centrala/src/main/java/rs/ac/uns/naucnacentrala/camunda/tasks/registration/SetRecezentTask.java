package rs.ac.uns.naucnacentrala.camunda.tasks.registration;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.AuthorityRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetRecezentTask implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Authority> authorityList = new ArrayList<>();
        Authority authority= authorityRepository.findOneByName("ROLE_RECEZENT");
        User user = userRepository.findByUsername(delegateExecution.getVariable("username").toString());
        authorityList.add(authority);
        user.setAuthorities(authorityList);
        identityService.createMembership(user.getUsername(),"recezent");
        userRepository.save(user);
    }


}

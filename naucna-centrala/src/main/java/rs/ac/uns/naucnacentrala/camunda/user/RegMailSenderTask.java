package rs.ac.uns.naucnacentrala.camunda.user;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class RegMailSenderTask implements JavaDelegate {


    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String link = "http://localhost:8080/restapi/registration/complete/"+delegateExecution.getProcessInstanceId();
        System.out.println(link);
        String subject="Naucna Centrala registration confirmation";
        String to=delegateExecution.getVariable("email").toString();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mail  = new MimeMessageHelper(mimeMessage,true);
        mail.setSentDate(new Date());
        mail.setSubject("Registration confirmation");
        mail.setText("Hello, you can confirm your registration by clicking on this link: "+link);
        mail.setTo(to);
        mail.setFrom("naucna.centrala28@gmail.com");

        javaMailSender.send(mimeMessage);

    }

}

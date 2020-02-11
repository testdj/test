package rs.ac.uns.naucnacentrala.camunda.paper;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.newJournal.CasopisRepository;
import rs.ac.uns.naucnacentrala.newUser.UserRepository;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

@Service

public class NotifyAuthorAndEditor implements JavaDelegate {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mail  = new MimeMessageHelper(mimeMessage,true);
        mail.setSentDate(new Date());
        mail.setFrom("naucna.centrala28@gmail.com");

        String task = delegateExecution.getCurrentActivityName();
        if(task.equals("Slanje razloga odbijanja autoru")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Nazalost, Vas rad nije relevantan za nas casopis.");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Slanje mejla autoru za ispravku rada")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Format rada nije skladu sa nasim kriterijumima. Molimo, ispravite ga u sto kracem roku.");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Slanje maila za odbijanje zbog tehnickih razloga")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Nazalost, Vas rad je odbijen iz tehnickih razloga.");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti urednika o izboru novog recezenta")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Potrebno je da izaberete novog recezenta");
            CasopisPV kome= (CasopisPV) delegateExecution.getVariable("casopis");
            System.out.println("CASOPISSSSSSSSSSSSSSSSSSSSssssss: " + kome.getGlavniUrednik());
            User u = userRepository.findByUsername(kome.getGlavniUrednik());
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti korisnika o odbijanju")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Nazalost, Vas rad je odbijen.");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti recezente o izmenama")) {
            mail.setSubject("Potrebna recenzija");
            mail.setText("Pojavio se rad koji je potrebno recenzirati.");

            String vratio = runtimeService.getVariable(delegateExecution.getProcessInstanceId(),"selectedRecezenti").toString();
            System.out.println("VRATIOOOOOOOOOOOOOOOOOOOOOOOOOOO " + vratio);
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti korisnika o velikim ispravkama")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Potrebno je da napravite vece izmene u radu kako bi bio objavljen.");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti korisnika o malim ispravkama")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Molimo izvrsite manje ispravke rada");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti urednika o izmenama")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Autor je izvrsio manje izmene rada.");
            CasopisPV kome= (CasopisPV) delegateExecution.getVariable("casopis");
            System.out.println("CASOPISSSSSSSSSSSSSSSSSSSSssssss: " + kome.getGlavniUrednik());
            User u = userRepository.findByUsername(kome.getGlavniUrednik());
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti urednika o zavrsetku dorade")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Autor je zavrsio doradu rada.");
            CasopisPV kome= (CasopisPV) delegateExecution.getVariable("casopis");
            System.out.println("CASOPISSSSSSSSSSSSSSSSSSSSssssss: " + kome.getGlavniUrednik());
            User u = userRepository.findByUsername(kome.getGlavniUrednik());
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti autora o doradi")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Molimo Vas, da doradite rad.");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Obavesti korisnika o potvrdi")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Sa zadovoljstvom Vas obavestavamo da je Vas rad prihvacen!");
            String kome=delegateExecution.getVariable("author").toString();
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Slanje mejla uredniku")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Pojavio se novi rad koji bi trebalo da pregledate.");
            CasopisPV kome= (CasopisPV) delegateExecution.getVariable("casopis");
            System.out.println("CASOPISSSSSSSSSSSSSSSSSSSSssssss: " + kome.getGlavniUrednik());
            User u = userRepository.findByUsername(kome.getGlavniUrednik());
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }else if(task.equals("Slanje mejla autoru")) {
            mail.setSubject("Status poslatog rada");
            mail.setText("Primili smo Vas rad, bice pregledan u najkracem mogucem roku.");
            String kome=delegateExecution.getVariable("author").toString();
            System.out.println("KOMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE: " + kome);
            User u = userRepository.findByUsername(kome);
            mail.setTo(u.getEmail());
            javaMailSender.send(mimeMessage);
        }



    }
}

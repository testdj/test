package rs.ac.uns.naucnacentrala.service;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.naucnacentrala.dto.LoginResponseDTO;
import rs.ac.uns.naucnacentrala.dto.UserTokenState;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.AuthorityRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.security.TokenUtils;
import rs.ac.uns.naucnacentrala.security.auth.JwtAuthenticationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class LoginServiceImpl implements  LoginService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    IdentityService identityService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public User checkCredentials(JwtAuthenticationRequest request) {
        User user=userRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;
    }


    @Override
    public boolean register(User user, String role) throws Exception{
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityRepository.findOneByName(role);
        System.out.println("ROLEEE:" + role);
        authorities.add(authority);
        System.out.println("auttth: " + authority.getName() + authority.getAuthority() + authority.getId());
        System.out.println("AUTHORITIESSSS: " + authorities.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(authorities);
        user = userRepository.save(user);
        org.camunda.bpm.engine.identity.User userCamunda=identityService.newUser(user.getUsername());
        userCamunda.setLastName(user.getPrezime());
        userCamunda.setEmail(user.getEmail());
        userCamunda.setPassword(user.getPassword());
        userCamunda.setFirstName(user.getIme());
        System.out.println("USERRRR: " + user.getUsername());
        identityService.saveUser(userCamunda);
        System.out.println("sacuvaooooooooo");
        String processedRole=processRoleString(role).toLowerCase();
        System.out.println("ROLEEEEEEEEEEEEEEEEEEEE: " + processedRole);
        System.out.println("CAMUNDAAA IDDDDD: " + userCamunda.getId());
        identityService.createMembership(userCamunda.getId(),processedRole);
        System.out.println("uspeoooo");


        return true;
    }

    @Override
    public LoginResponseDTO login(JwtAuthenticationRequest request) {
        User user=userRepository.findByUsername(request.getUsername());
        if(user!=null){
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())&&user.isEnabled()){
                long expiresIn = tokenUtils.getExpiredIn();
                String jwt = tokenUtils.generateToken(request.getUsername());
                LoginResponseDTO token=new LoginResponseDTO(jwt,((Authority)user.getAuthorities().toArray()[0]).getName(),user.getUsername(),expiresIn);
                // Vrati user-a sa tokenom kao odgovor na uspesnu autentifikaciju
                return token;
            }
        }
        return null;
    }

    @Override
    public UserTokenState refreshAuthenticationToken(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        User user = (User) userRepository.findByUsername(username);
        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastSifraResetDate())) {
            long expiresIn = tokenUtils.getExpiredIn();
            String refreshedToken = tokenUtils.refreshToken(token);
            UserTokenState newToken=new UserTokenState(refreshedToken,expiresIn);
            // Vrati user-a sa tokenom kao odgovor na uspesnu autentifikaciju
            return newToken;
        } else {
            return null;
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String username) throws Exception{
        User user= userRepository.findByUsername(username);
        if(passwordEncoder.matches(oldPassword,user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }else{
            throw new Exception();
        }
    }

    private String processRoleString(String s){
        return s.replace("ROLE_","");
    }

}

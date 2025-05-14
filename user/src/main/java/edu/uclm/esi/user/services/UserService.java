package edu.uclm.esi.user.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.user.dao.UserDao;
import edu.uclm.esi.user.model.User;



@Service
public class UserService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenStorageService tokenStorageService;

    @Autowired
    private SessionToken sessionToken;


    @Autowired
    private ProxyBEUsuarios proxyBEUsuarios;

    public User createUser(User user) {
        if (userDao.existsByName(user.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with email: " + user.getEmail());
        }
        return userDao.save(user);
    }

    public void addCredit(String name, int amount) {
    User user = userDao.findByName(name);
    if (user == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    userDao.delete(user);
    user.setCredit(user.getCredit() + amount);
    
    userDao.save(user);
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User getUserByName(String name) {
        User userfound = userDao.findByName(name);
        return userfound;
    }

    public boolean checkUserCredit(User user) {
        // Lógica para verificar el crédito del usuario
        System.out.println("El nombre es: "+user.getName());
        int result=user.getCredit();
        System.out.println("El credito es: "+result);
        if (result<=0) {
            System.out.println("El credito es menor o igual a 0");
            return false;
        }else {
            System.out.println("El credito es mayor a 0");
            return true;
        }
    }

    public void savePasswordRecoveryToken(String email, String token) {
        // Lógica para guardar el token en la base de datos con una expiración
        tokenStorageService.saveToken(token, email, LocalDateTime.now().plusHours(1));

    }

    public void sendPassword(String email, String token) {
        // Lógica para enviar la contraseña al usuario
        if (!tokenStorageService.isTokenValid(token,email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token.");
        }
        String password = userDao.findByEmail(email).getPassword();
        String subject = "Password Recovery";
        String body = "Your password is: "+password;

        emailService.sendEmail(email, subject, body);

    }

    public void sendTokenVerify(String email) {
        // Token random generation
        String token = java.util.UUID.randomUUID().toString();
        tokenStorageService.saveToken(token, email, LocalDateTime.now().plusHours(1));
        String subject = "Email Verification";
        String body = "Your verification token is: "+token;

        emailService.sendEmail(email, subject, body);
    }

    public boolean verifyToken(String token, String email) {
        // Lógica para verificar el token
        return tokenStorageService.isTokenValid(token,email);
    }

    public void payCredit(User user, int amount) {
        // Lógica para pagar el crédito
        System.out.println("El nombre es: "+user.getName());

        int n=user.getCredit() - amount;
        userDao.delete(user);
        user.setCredit(n);
        System.out.println("El nuevo credito es: "+user.getCredit());
        System.out.println("El nuevo credito 2es: "+n);
        System.out.println("El amount es: "+amount);
        userDao.save(user);
    }
}

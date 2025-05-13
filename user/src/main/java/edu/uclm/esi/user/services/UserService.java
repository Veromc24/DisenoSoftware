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
    private ProxyBEUsuarios proxyBEUsuarios;

    public User createUser(User user) {
        if (userDao.existsByName(user.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with email: " + user.getEmail());
        }
        return userDao.save(user);
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User getUserByName(String name) {
        User userfound = userDao.findByName(name);
        return userfound;
    }

    public void checkUserCredit(String token) {
        String result = proxyBEUsuarios.checkCredit(token);
        if (result == null || result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or insufficient credit for user.");
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
}
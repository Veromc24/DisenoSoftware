package edu.uclm.esi.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.user.dao.UserDao;
import edu.uclm.esi.user.model.User;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProxyBEUsuarios proxyBEUsuarios;

    public User createUser(User user) {
        if (userDao.existsByName(user.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with email: " + user.getEmail());
        }
        return userDao.save(user);
    }

    public User getUserByEmail(String email) {
        return userDao.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
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
}
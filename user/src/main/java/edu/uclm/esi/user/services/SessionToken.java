package edu.uclm.esi.user.services;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.user.dao.UserDao;
import edu.uclm.esi.user.model.User;

@Service
public class SessionToken {

    private final ConcurrentHashMap<String, User> tokenStore = new ConcurrentHashMap<>();

    @Autowired
    private UserDao userDao; // Make sure this import matches your actual UserDao package

    public void saveToken(String token, User user) {
        if(tokenStore.isEmpty()) {
            tokenStore.put(token, user);
        }
        else {
            tokenStore.replace(token, user);
        }
    }

    public User getUser(String token) {
        User user = tokenStore.get(token);
        if (user == null) {
            return null;
        }
        // Refresca desde la base de datos por si hay cambios
        return userDao.findByName(user.getName());
    }


    public void clearTokens() {
        tokenStore.clear();
    }

    public boolean isTokenValid(String token) {
        return tokenStore.containsKey(token);
    }

    public String getToken() {
        if (tokenStore.isEmpty()) {
            return null; // or throw an exception
        }
        return tokenStore.keys().nextElement();
    }



}

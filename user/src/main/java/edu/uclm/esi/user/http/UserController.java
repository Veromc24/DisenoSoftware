package edu.uclm.esi.user.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.user.model.User;
import edu.uclm.esi.user.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/checkCredit")
    public ResponseEntity<String> checkCredit(@RequestHeader("Authorization") String token) {
        userService.checkUserCredit(token);
        return ResponseEntity.ok("Credit is valid.");
    }

    @GetMapping("/loginConGetYParametros")
    public String login(@RequestParam String name, @RequestParam String password) {
        return "1234";
    }

    @GetMapping("/loginConPathYParametro/{name}")
    public String login2(@PathVariable String name, @RequestParam String password) {
        return "1234";
    }

    @PostMapping("/loginConPathYBody/{name}")
    public String loginConPathYBody(@PathVariable String name, @RequestBody String password) {
        return "1234";
    }

    @PostMapping("/loginConBody")
    public String loginConBody(@RequestBody Map<String, String> password) {
        return "1234";
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> data) {
        System.out.println("Datos recibidos en /signup: " + data);

        String name = data.get("name");
        String password = data.get("password");
        String email = data.get("email");

        if (name == null || password == null || email == null) {
            System.err.println("Faltan parámetros: name=" + name + ", password=" + password + ", email=" + email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameters");
        }

        System.out.println("Nombre: " + name + ", Email: " + email + ", Contraseña: " + password);

        User user = new User(name, password, email);
        userService.createUser(user);

        System.out.println("Usuario creado exitosamente: " + name + ", Email: " + email);

        // Respuesta en formato JSON
        Map<String, String> response = Map.of("message", "User created successfully");
        return ResponseEntity.ok(response);
    }
}

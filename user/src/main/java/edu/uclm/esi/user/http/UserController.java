package edu.uclm.esi.user.http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import edu.uclm.esi.user.services.EmailService;
import edu.uclm.esi.user.services.SessionToken;
import edu.uclm.esi.user.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private SessionToken sessionToken; 

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
    public ResponseEntity<Map<String, String>> checkCredit(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (token == null || !sessionToken.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
        User user = sessionToken.getUser(token);
        if (userService.checkUserCredit(user)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Crédito verificado correctamente");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Crédito insuficiente");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/loginConBody")       //Este es el método que se va a usar para el login
    public ResponseEntity<Map<String, String>> loginConBody(@RequestBody Map<String, String> credentials) {
        String name = credentials.get("name");
        String password = credentials.get("password");

        if (name == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameters");
        }

        // Validar que los parámetros no contengan caracteres maliciosos
        if (!name.matches("^[a-zA-Z0-9._-]{3,}$") || !password.matches("^[a-zA-Z0-9@#$%^&+=]{6,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input format");
        }

        User user = userService.getUserByName(name); // Método que busca al usuario por nombre

        if (user == null || !userService.checkPassword(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // Generar un token aleatorio UUID
        String token = UUID.randomUUID().toString();

        sessionToken.saveToken(token, user);
        
        Map<String, String> response = Map.of("token", token,"credits", String.valueOf(user.getCredit()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> data) {

        String name = data.get("name");
        String password = data.get("password");
        String email = data.get("email");

        if (name == null || password == null || email == null) {
            System.err.println("Faltan parámetros: name=" + name + ", password=" + password + ", email=" + email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameters");
        }


        User user = new User(name, password, email);
        userService.createUser(user);


        // Respuesta en formato JSON
        Map<String, String> response = Map.of("message", "User created successfully");
        return ResponseEntity.ok(response);
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("/recoverPassword")
    public ResponseEntity<Map<String, String>> recoverPassword(@RequestBody Map<String, String> data) {
        String email = data.get("email");

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email parameter");
        }

        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Generar un token seguro (puedes usar UUID o JWT)
        String token = UUID.randomUUID().toString(); // Generar un token único
        userService.savePasswordRecoveryToken(email, token); // Guardar el token con temporalidad

        // Enviar el token al correo electrónico del usuario
        emailService.sendEmail(email, "Recuperación de Contraseña",
            "Usa este token para recuperar tu contraseña: " + token);

        Map<String, String> response = Map.of("message", "Correo de recuperación enviado");
        return ResponseEntity.ok(response);
    }

     @PostMapping("/sendPasswordResetEmail")
    public ResponseEntity<Map<String, String>> sendPasswordResetEmail(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String token = data.get("token");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email parameter");
        }

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email parameter");
        }

        userService.sendPassword(email,token);

        Map<String, String> response = Map.of("message", "Contraseña enviada al correo");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendVerifyToken")
    public ResponseEntity<Map<String, String>> SendVerifyToken(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing email parameter");
        }

        userService.sendTokenVerify(email);

        Map<String, String> response = Map.of("message", "Token de verificación enviado al correo");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<Map<String, String>> verifyToken(@RequestBody Map<String, String> data) {
        String token = data.get("token");
        String email = data.get("email");
        if (token == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing token or email parameter");
        }

        boolean isValid = userService.verifyToken(token,email);
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        

        Map<String, String> response = Map.of("message", "Token is valid");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addCredit")
    public ResponseEntity<Map<String, String>> addCredit(
        @RequestBody Map<String, Object> data) {

        String token = sessionToken.getToken();
        int amount = (int) data.get("amount");
        User user = sessionToken.getUser(token); // Obtener el usuario asociado al token
        userService.addCredit(user.getName(), amount);

        Map<String, String> response = Map.of("message", "Credit added successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payCredit")
    public ResponseEntity<Map<String, String>> payCredit(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, Object> data) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (token == null || !sessionToken.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
        int amount = (int) data.get("amount");
        User user = sessionToken.getUser(token);
        userService.payCredit(user, amount);

        Map<String, String> response = Map.of("message", "Credit paid successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/deleteToken")
    public ResponseEntity<Map<String, String>> deleteToken() {
        String token = sessionToken.getToken();
        if(sessionToken.isTokenValid(token)&&token!=null){
        Map<String, String> response = Map.of("message", "Logout successful");
        sessionToken.clearTokens();
        return ResponseEntity.ok(response);
        
        }
        else{
            Map<String, String> response = Map.of("message", "Session expired");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/checkSession")
    public ResponseEntity<Map<String, String>> checkSession() {
        String token = sessionToken.getToken();
        if(sessionToken.isTokenValid(token)&&token!=null){
        Map<String, String> response = Map.of("message", "Token found");
        return ResponseEntity.ok(response);
        
        }
        else{
            Map<String, String> response = Map.of("message", "No token");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/getEmail")
    public ResponseEntity<Map<String, String>> getEmail(@RequestParam String name) {
        User user = userService.getUserByName(name);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Map<String, String> response = Map.of("email", user.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getCredits")
public ResponseEntity<Map<String, Object>> getCredits(@RequestBody Map<String, String> data) {
    String name = data.get("name");
    if (name == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Missing name parameter"));
    }
    User user = userService.getUserByName(name);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "No user logged in"));
    }
    Map<String, Object> response = new HashMap<>();
    response.put("credits", user.getCredit());
    response.put("username", user.getName());
    return ResponseEntity.ok(response);
}
}

package edu.uclm.esi.user.http;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {

    @GetMapping("/loginConGetYParametros")
    public String login(@RequestParam String name, @RequestParam String pwd) {
        return "1234";
    }

    @GetMapping("/loginConPathYParametro/{name}")
    public String login2(@PathVariable String name, @RequestParam String pwd) {
        return "1234";
    }

    @PostMapping("/loginConPathYBody/{name}")
    public String loginConPathYBody(@PathVariable String name, @RequestBody String pwd) {
        return "1234";
    }

    @PostMapping("/loginConBody")
    public String loginConBody(@RequestBody Map<String, String> pwd) {
        return "1234";
    }
}

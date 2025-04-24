package edu.uclm.esi.circuits.http;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import edu.uclm.esi.circuits.services.CircuitService; // Adjust the package path as needed
import edu.uclm.esi.circuits.services.ProxyBEUsuarios;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import edu.uclm.esi.circuits.model.Circuit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/circuits")
@CrossOrigin(origins = "*")
public class CircuitController {

    @Autowired
    private CircuitService service;

    @PostMapping("/createCircuit")
    public String createCircuit(@RequestBody Map<String, Object> body) {
        if (!body.containsKey("table") || !body.containsKey("outputQubits")) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "The request body must contain qubits and outputQubits");
        }
        return this.service.createCircuit(body);
    }

    @PostMapping("/generateCode")
    public Map<String, Object> generateCode(HttpServletRequest request, @RequestParam(required = false) String name,
            @RequestBody Circuit circuit) {
        if (name != null) {
            circuit.setName(name);
        }

        // Obtener el token desde el encabezado "token_generacion"
        String token = request.getHeader("token_generacion");

        // Verificar el token antes de continuar
        ProxyBEUsuarios.get().checkCredit(token);

        if (circuit.getQubits() > 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Circuit exceeds the maximum allowed size (6 rows).");
        }

        try {
            return this.service.generateCode(circuit, token);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, e.getMessage());
        }
    }
}
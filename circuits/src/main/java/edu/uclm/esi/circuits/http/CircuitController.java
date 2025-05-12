package edu.uclm.esi.circuits.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // Adjust the package path as needed
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.circuits.model.Circuit;
import edu.uclm.esi.circuits.services.CircuitService;
import edu.uclm.esi.circuits.services.ProxyBEUsuarios;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/circuits")
@CrossOrigin(origins = "*")
public class CircuitController {

    @Autowired
    private CircuitService service;

    @PostMapping("/createCircuit")
    public ResponseEntity<String> createCircuit(@RequestBody Map<String, Object> body) {
        try {
            String result = this.service.createCircuit(body);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            throw e; // Re-lanzar excepciones específicas
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
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
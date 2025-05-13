package edu.uclm.esi.circuits.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // Adjust the package path as needed
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<Map<String, String>> createCircuit(@RequestBody Map<String, Object> body) {
        try {
            String result = this.service.createCircuit(body);
            Map<String, String> response = new HashMap<>();
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
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
        try {
            return this.service.generateCode(circuit);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, e.getMessage());
        }
    }

    @GetMapping("/getCircuit")
    public ResponseEntity<Circuit> getCircuit(@RequestParam String id) {
        try {
            Circuit circuit = this.service.getCircuitById(id);
            return ResponseEntity.ok(circuit);
        } catch (ResponseStatusException e) {
            throw e; // Re-lanzar excepciones específicas
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }
}
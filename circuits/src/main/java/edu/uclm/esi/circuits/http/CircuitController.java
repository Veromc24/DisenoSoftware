package edu.uclm.esi.circuits.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.circuits.model.Circuit;
import edu.uclm.esi.circuits.services.CircuitService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("circuits")
public class CircuitController {

    @Autowired
    private CircuitService service;

    @PostMapping("/createCircuit")
    public String createCircuit(@RequestBody Circuit circuit) {
        if (circuit.getTable() == null || circuit.getOutputQubits() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The request body must contain qubits and outputQubits");
        }
       return this.service.createCircuit(circuit);
    }

    @PostMapping("/generateCode")
    public String generateCode(@RequestParam(required = false) String name, @RequestBody Circuit circuit){
        if (name != null){
            circuit.setName(name);
        }
        return this.service.generateCode(circuit);
    }
}

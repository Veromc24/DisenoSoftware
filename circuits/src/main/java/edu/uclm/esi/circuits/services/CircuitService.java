package edu.uclm.esi.circuits.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.uclm.esi.circuits.model.Circuit;

@Service
public class CircuitService {
public String createCircuit(Circuit circuit) {

       return "hola";

    }

    public String generateCode(Circuit circuit) {

        return circuit.generateCode();
 
     }
}

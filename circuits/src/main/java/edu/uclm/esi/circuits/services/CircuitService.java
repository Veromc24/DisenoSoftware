package edu.uclm.esi.circuits.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import edu.uclm.esi.circuits.dao.CircuitDao; // Adjust the class name and package path if necessary
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import edu.uclm.esi.circuits.model.Circuit; // Adjust the package path if necessary

@Service
public class CircuitService {

    @Autowired
    private CircuitDao circuitDAO;

    public String createCircuit(Map<String, Object> body) {
        return "hola";
    }

    public Map<String, Object> generateCode(Circuit circuit, String token) {
        // Check if the number of rows (2^inputQubits) exceeds the limit (e.g., 6)
        // Note: The image shows getQubits returning table.length. If the check should be on input qubits,
        // the logic in getQubits or the check here needs adjustment.
        // Assuming the check is intended for the value returned by the image's getQubits():
        if (circuit.getQubits() > 6) { // Check based on table.length
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Circuit exceeds the maximum allowed size (6 rows).");
        }

        // TODO: Implement credit check using the token
        // ProxyBEU.checkCredit(token);

        Map<String, Object> result = new HashMap<>();

        // Generate the code using the Circuit object
        String code = circuit.generateCode();

        // Save the circuit if it has a valid name
        if (circuit.getName() != null) {
            this.circuitDAO.save(circuit);
        }

        // Add the generated code to the result map
        result.put("code", code);

        return result;
    }
}

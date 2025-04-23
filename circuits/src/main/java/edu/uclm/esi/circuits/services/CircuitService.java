package edu.uclm.esi.circuits.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import edu.uclm.esi.circuits.dao.CircuitDao; // Adjust the class name and package path if necessary
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import edu.uclm.esi.circuits.model.Circuit; // Adjust the package path if necessary
import edu.uclm.esi.circuits.services.ProxyBEUsuarios; // Import the proxy

@Service
public class CircuitService {

    @Autowired
    private CircuitDao circuitDAO;

    @Autowired // Inject the proxy
    private ProxyBEUsuarios proxyBEUsuarios;

    public String createCircuit(Map<String, Object> body) {
        return "hola";
    }

    public Map<String, Object> generateCode(Circuit circuit, String token) throws Exception {
    
        if (circuit.getQubits() > 6) { 
            // Check based on table.length
            ProxyBEUsuarios.get().checkCredit(token); // Check credit using the proxy
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Circuit exceeds the maximum allowed size (6 rows).");
        }

        Map<String, Object> result = new HashMap<>();

    
        String code = circuit.generateCode();
        if (circuit.getName() != null) {
            this.circuitDAO.save(circuit);
        }
        result.put("code", code);

        return result;
    }
}

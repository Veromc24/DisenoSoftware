package edu.uclm.esi.circuits.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service; // Adjust the class name and package path if necessary
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.circuits.dao.CircuitDao;
import edu.uclm.esi.circuits.model.Circuit; // Adjust the package path if necessary

@Service
public class CircuitService {

    @Autowired
    private CircuitDao circuitDAO;

    @Autowired // Inject the proxy
    private ProxyBEUsuarios proxyBEUsuarios;

    public String createCircuit(Map<String, Object> body) {
        try {



            int [][] table = (int[][])body.get("table");

            int outputQubits = (int) body.get("outputQubits");

            

            Circuit circuit = new Circuit();

            circuit.setTable(table);

            circuit.setOutputQubits(outputQubits);

            this.circuitDAO.save(circuit);

    

            return "Circuit created successfully with ID: " + circuit.getId();

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,

                    "An error occurred while creating the circuit: " + e.getMessage());

        }
    }

    public Map<String, Object> generateCode(Circuit circuit, String token) throws Exception {
        
        if (circuit.getQubits() > 6) {
            if (token == null)
                throw new Exception("You must provide a token to generate a code for a circuit with more than 6 qubits");
            ProxyBEUsuarios.get().checkCredit(token);
        }

        String templateCode = this.readFile("ibm.local.txt");
        String code = circuit.generateCode(templateCode);

        if (circuit.getName() != null) {
            this.circuitDAO.save(circuit);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        return result;
    }

    private String readFile(String fileName) throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream fs = classLoader.getResourceAsStream(fileName)) {
            byte[] b = new byte[fs.available()];
            fs.read(b);
            return new String(b);
        }
    }
}

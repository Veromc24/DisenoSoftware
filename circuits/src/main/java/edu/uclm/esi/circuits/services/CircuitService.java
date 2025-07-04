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
            Object outputQubitsObj = body.get("outputQubits");
            if (outputQubitsObj == null || !(outputQubitsObj instanceof Integer)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or missing 'outputQubits' parameter");
            }
            int outputQubits = (int) outputQubitsObj;

            Object tableObj = body.get("table");
            if (tableObj == null || !(tableObj instanceof java.util.List)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or missing 'table' parameter");
            }
            int[][] table;
            try {
                @SuppressWarnings("unchecked")
                java.util.List<java.util.List<Integer>> tableList = (java.util.List<java.util.List<Integer>>) tableObj;
                table = tableList.stream()
                                 .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                                 .toArray(int[][]::new);
            } catch (ClassCastException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid 'table' format. Expected a 2D array of integers.");
            }

            // Obtener el nombre del circuito
            Object nameObj = body.get("name");
            String name = (nameObj instanceof String) ? (String) nameObj : null;

            // Crear y guardar el circuito
            Circuit circuit = new Circuit();
            circuit.setTable(table);
            circuit.setOutputQubits(outputQubits);
            circuit.setName(name); // Asignar el nombre al circuito


            if (table.length > 6) {
                this.circuitDAO.save(circuit);
            }
            

            return "Circuit created successfully with ID: " + circuit.getId();
        } catch (ResponseStatusException e) {
            throw e; // Re-lanzar excepciones específicas
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the circuit: " + e.getMessage());
        }
    }

    public Map<String, Object> generateCode(Circuit circuit, String token) throws Exception {

        // Si la tabla es grande, comprobar crédito
        if (circuit.getTable().length > 6) {
            String checkResponse = proxyBEUsuarios.checkCredit(token);
            if (checkResponse == null || !checkResponse.contains("Crédito verificado correctamente")) {
                throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Crédito insuficiente");
            }
            String payResponse = proxyBEUsuarios.payCredit(token, 1);
            if (payResponse == null || !payResponse.contains("Credit paid successfully")) {
                throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "No se pudo descontar el crédito");
            }
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

    public Circuit getCircuitById(String id) {
        try {
            return this.circuitDAO.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Circuit not found with ID: " + id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving the circuit: " + e.getMessage());
        }
    }
}

package edu.uclm.esi.circuits.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import edu.uclm.esi.circuits.dao.CircuitDao; // Adjust the class name and package path if necessary
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import edu.uclm.esi.circuits.model.Circuit; // Adjust the package path if necessary

@Service
public class CircuitService {

      @Autowired
      private CircuitDao circuitDAO;
      
  public String createCircuit( Map<String, Object> body) {
       return "hola";
      }

   public Map<String, Object> generateCode(Circuit circuit) {
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

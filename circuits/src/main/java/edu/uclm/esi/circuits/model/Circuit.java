package edu.uclm.esi.circuits.model;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Circuit {
    @Id
    @Column(length = 36)
    private String id;
    private int outputQubits;
    @Lob
    @Column(columnDefinition = "TEXT", name = "truth_table")
    private int[][] table;
    @Column(length = 50)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOutputQubits() {
        return outputQubits;
    }

    public void setOutputQubits(int outputQubits) {
        this.outputQubits = outputQubits;
    }

    public int[][] getTable() {
        return table;
    }

    public void setTable(int[][] table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the number of rows in the truth table (2^inputQubits).
     * Based on the provided image.
     */
    public int getQubits() {
        // As per the image, return the length of the table (number of rows)
        if (this.table == null) {
            return 0;
        }
        return this.table.length;
    }

     public String generateCode(String template) {
        return template
                .replace("#NAME#", this.name != null ? this.name : "UnnamedCircuit")
                .replace("#QUBITS#", String.valueOf(getQubits()))
                .replace("#OUTPUT QUBITS#", String.valueOf(this.outputQubits))
                .replace("#INITIALIZE#", generateInitializeCode())
                .replace("#CIRCUIT#", generateCircuitCode())
                .replace("#MEASURES#", generateMeasureCode());
    }

    private String generateInitializeCode() {
        return ""; 
    }

    private String generateCircuitCode() {
        if (this.table == null || this.table.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        int totalQubits = this.table[0].length;
        int inputQubits = totalQubits - this.outputQubits;

        for (int[] row : this.table) {
            sb.append("# Row: ").append(Arrays.toString(row)).append("\n");

            // Simulate input using X gates
            for (int i = 0; i < inputQubits; i++) {
                if (row[i] == 1) {
                    sb.append("circuit.x(greg[").append(i).append("])\n");
                }
            }

            // Apply outputs
            for (int i = 0; i < this.outputQubits; i++) {
                int qubitIndex = inputQubits + i;
                if (row[qubitIndex] == 1) {
                    sb.append("circuit.x(greg[").append(qubitIndex).append("])\n");
                }
            }

            sb.append("circuit.barrier()\n");

            // Reset input qubits
            for (int i = 0; i < inputQubits; i++) {
                if (row[i] == 1) {
                    sb.append("circuit.x(greg[").append(i).append("])\n");
                }
            }
        }

        return sb.toString();
    }

    private String generateMeasureCode() {
        if (this.table == null || this.table.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        int inputQubits = this.table[0].length - this.outputQubits;

        for (int i = 0; i < this.outputQubits; i++) {
            int qubitIndex = inputQubits + i;
            sb.append("circuit.measure(greg[").append(qubitIndex).append("], creg[").append(i).append("])\n");
        }

        return sb.toString();
    }

    public Circuit() {
        this.id = java.util.UUID.randomUUID().toString();
    }
}
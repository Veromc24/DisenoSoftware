package edu.uclm.esi.circuits.model;

import java.util.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
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

    public String generateCode() {
        // Generate code based on the table
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                code.append("Qubit ").append(i).append(" -> Qubit ").append(table[i][j]).append("\n");
            }
        }
        System.out.println(code.toString());
        return code.toString();
    }

    public Circuit() {
        this.id = java.util.UUID.randomUUID().toString();
    }
}
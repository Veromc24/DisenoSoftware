package edu.uclm.esi.circuits.model;

public class Circuit {
    private int outputQubits;
    private int[][] table;

    public int getOutputQubits() {
        return outputQubits;
    }

    public int[][] getTable() {
        return table;
    }

    public void setOutputQubits(int outputQubits) {
                this.outputQubits = outputQubits;
        }
    
    public void setTable(int[][] table) {
           this.table = table;
    }

    public String generateCode() {
        return "hola";
    }
}

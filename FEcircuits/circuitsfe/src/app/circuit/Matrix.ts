export class Matrix {
  values: number[][];

  constructor(inputQubits: number, outputQubits: number) {
    this.values = [];
    const rows = Math.pow(2, inputQubits);
    const cols = inputQubits + outputQubits;

    for (let i = 0; i < rows; i++) {
      this.addRow(i, inputQubits, outputQubits);
    }
  }

  private addRow(index: number, inputQubits: number, outputQubits: number): void {
    const row: number[] = [];
    const binary = index.toString(2).padStart(inputQubits, '0'); // Convert index to binary and pad with zeros

    // Fill the row with zeros
    for (let i = 0; i < inputQubits + outputQubits; i++) {
      row.push(0);
    }

    // Populate the input qubits part of the row
    for (let i = 0; i < inputQubits; i++) {
      row[i] = parseInt(binary.charAt(i));
    }

    this.values.push(row);
  }
}

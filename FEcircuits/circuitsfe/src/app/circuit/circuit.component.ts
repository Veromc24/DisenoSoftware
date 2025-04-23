import { Component } from '@angular/core';
import { CircuitService } from '../circuit.service';  // Asegúrate de importar el servicio desde la ubicación correcta
import { Matrix } from './Matrix';

@Component({
  selector: 'app-circuit',
  standalone: false,
  templateUrl: './circuit.component.html',
  styleUrls: ['./circuit.component.css']  // Corregí styleUrl a styleUrls
})
export class CircuitComponent {
  inputQubits: number;
  outputQubits: number;
  matrix: Matrix;
  showMatrix: boolean = false; // Nueva propiedad para controlar la visibilidad de la tabla

  constructor(private service: CircuitService) {  // Inyectar el servicio correctamente
    this.inputQubits = 3;
    this.outputQubits = 3;
    this.matrix = new Matrix(this.inputQubits, this.outputQubits);  // Asegúrate de que Matrix tenga el constructor correcto
  }

  buildMatrix() {
    this.matrix = new Matrix(this.inputQubits, this.outputQubits); // Reconstruir la matriz
    this.showMatrix = true; // Mostrar la tabla al pulsar el botón
  }

  negate(row: number, col: number) {
    this.matrix.values[row][col] = this.matrix.values[row][col] === 0 ? 1 : 0;
  }

  generateCode() {
    this.service.generateCode(this.outputQubits, this.matrix).subscribe(
      (ok: any) => {
        console.log("Todo ha salido bien");
      },
      (error: any) => {
        console.error("Algo ha ido mal", error);
      }
    );
  }
}

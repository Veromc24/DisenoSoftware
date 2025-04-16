import { Component } from '@angular/core';
import { Matrix } from './Matrix';

@Component({
  selector: 'app-circuit',
  standalone: false,
  templateUrl: './circuit.component.html',
  styleUrl: './circuit.component.css'
})
export class CircuitComponent {
  inputQubits: number;
  outputQubits: number;
  matrix: Matrix;

  constructor() {
    this.inputQubits = 3;
    this.outputQubits = 3;
    this.matrix = new Matrix(this.inputQubits, this.outputQubits);
  }

  buildMatrix() {
    console.log(this.inputQubits);
    alert(this.outputQubits);
  }
}

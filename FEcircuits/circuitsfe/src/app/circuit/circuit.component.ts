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
  manager: { token: string | null }; // Nueva propiedad para manejar el token
  circuitId: string = ''; // Variable para almacenar el ID ingresado
  retrievedCircuit: any = null; // Variable para almacenar el circuito recuperado
  creationMessage: string = ''; // Nueva propiedad para almacenar mensajes de creación
  circuitName: string = ''; // Nueva propiedad para el nombre del circuito

  constructor(private service: CircuitService) {  // Inyectar el servicio correctamente
    this.inputQubits = 3;
    this.outputQubits = 3;
    this.matrix = new Matrix(this.inputQubits, this.outputQubits);  // Asegúrate de que Matrix tenga el constructor correcto
    this.manager = { token: sessionStorage.getItem("token") }; // Inicializar el token desde sessionStorage
  }

  buildMatrix() {
    this.matrix = new Matrix(this.inputQubits, this.outputQubits); // Reconstruir la matriz
    this.showMatrix = true; // Mostrar la tabla al pulsar el botón
  }

  negate(row: number, col: number) {
    this.matrix.values[row][col] = this.matrix.values[row][col] === 0 ? 1 : 0;
  }

  generateCode() {
    let token = this.manager.token; // Usar el token desde this.manager
   
    if (!token) {
      token = null; // Si no hay token, asignar null
    }
    
    this.service.generateCode(this.outputQubits, this.matrix!, token).subscribe(
      (ok: any) => {
        console.log("Todo ha salido bien");
      },
      (error: any) => {
        console.error("Algo ha ido mal", error);
      }
    );
  }

  createCircuit() {
    const body = {
      table: this.matrix.values, // Matriz de verdad
      outputQubits: this.outputQubits, // Número de qubits de salida
      name: this.circuitName // Nombre del circuito
    };

    this.service.createCircuit(body).subscribe({
      next: (response: any) => {
        if (response && response.message) { // Verificar que la respuesta contiene el campo 'message'
          console.log("Circuit created successfully:", response.message);
          this.creationMessage = response.message; // Actualiza el mensaje
        } else {
          console.warn("Circuit created, but response does not contain a message.");
          this.creationMessage = "Circuito creado con éxito, pero no se recibió un mensaje."; // Mensaje alternativo
        }
      },
      error: (error: any) => {
        console.error("An error occurred while creating the circuit", error);
        this.creationMessage = "Error al crear el circuito. Inténtelo de nuevo."; // Mensaje de error
      }
    });
  }

  getCircuit() {
    if (!this.circuitId) {
      alert('Por favor, ingrese un ID de circuito.');
      return;
    }

    this.service.getCircuit(this.circuitId).subscribe({
      next: (circuit) => {
        this.retrievedCircuit = circuit;
        console.log('Circuito recuperado:', circuit);
      },
      error: (error) => {
        console.error('Error al recuperar el circuito:', error);
        alert('No se pudo recuperar el circuito. Verifique el ID.');
      }
    });
  }
}

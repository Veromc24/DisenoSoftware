import { Injectable, input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Matrix } from './circuit/Matrix';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {
  private baseUrl = 'http://localhost:8080/circuits'; // Ajusta la URL base según tu configuración

  constructor(private http: HttpClient) {}

  generateCode(outputQubits: number, matrix: Matrix): Observable<any> {
    const body = {
      outputQubits: outputQubits,
      table: matrix?.values,
    };

    console.log("Matrix length:", matrix.values.length);

    if (matrix.values.length > 6) {
      return new Observable(observer => {
        this.http.get(`http://localhost:8081/users/checkCredit`).subscribe({
          next: (response: any) => {
            console.log("Check credit response:", response);

            if (!(response.message === "Crédito verificado correctamente")) {
              // Usuario sin crédito suficiente
              alert("No tienes suficiente crédito para generar este circuito.");
              observer.error("Crédito insuficiente");
            } else {
              // Usuario tiene crédito, proceder con el cobro y generación
              this.http.post(`http://localhost:8081/users/payCredit`, { amount: 1 }).subscribe({
                next: (payResponse: any) => {
                  console.log("Pago realizado:", payResponse);
                  // Actualiza solo localStorage, no llames al backend desde el componente principal
                  const currentCredits = Number(localStorage.getItem('credits')) || 0;
                  localStorage.setItem('credits', (currentCredits - 1).toString());
                  // Finalmente generar el código
                  this.http.post(`${this.baseUrl}/generateCode`, body).subscribe({
                    next: (result) => {
                      observer.next(result);
                      observer.complete();
                    },
                    error: (err) => observer.error(err)
                  });
                },
                error: (err) => observer.error(err)
              });
            }
          },
          error: (err) => {
            console.error("Error al verificar crédito:", err);
            observer.error(err);
          }
        });
      });
    } else {
      // Si no supera el límite, se genera directamente
      return this.http.post(`${this.baseUrl}/generateCode`, body);
    }
  }



  createCircuit(body: { table: number[][]; outputQubits: number }): Observable<any> {
    return this.http.post(`${this.baseUrl}/createCircuit`, body);
  }

  getCircuit(id: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/getCircuit?id=${id}`);
  }
}
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
    let body = {
      outputQubits: outputQubits,
      table: matrix?.values,
      
    };
    //Print matrix.values.length
    console.log("Matrix length: ", matrix.values.length);
    if((matrix.values.length) > 6){
      this.http.get(`http://localhost:8081/users/checkCredit`).subscribe((response: any) => {
        console.log("Response: ", response);
        if (response?.credit === false) {
          alert("You don't have enough credit to generate this circuit");
        } else {
          console.log("You have enough credit");
          
        }
      }
      );
      this.http.post(`http://localhost:8081/users/payCredit`, { amount: 1 }).subscribe((response: any) => {
        console.log("Response: ", response);
      }
      );}
    return this.http.post(`${this.baseUrl}/generateCode`, body);
    
  }

  createCircuit(body: { table: number[][]; outputQubits: number }): Observable<any> {
    return this.http.post(`${this.baseUrl}/createCircuit`, body);
  }

  getCircuit(id: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/getCircuit?id=${id}`);
  }
}
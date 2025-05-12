import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Matrix } from './circuit/Matrix';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {
  private baseUrl = 'http://localhost:8080/circuits'; // Ajusta la URL base según tu configuración

  constructor(private http: HttpClient) {}

  generateCode(outputQubits: number, matrix: Matrix, token: String | null): Observable<any> {
    let body = {
      outputQubits: outputQubits,
      table: matrix?.values
    };

    let headers = token ? { 'token_generacion': token as string } : undefined;

    return this.http.post(`${this.baseUrl}/generateCode`, body, { headers: headers });
  }

  createCircuit(body: { table: number[][]; outputQubits: number }): Observable<any> {
    return this.http.post(`${this.baseUrl}/createCircuit`, body);
  }

  getCircuit(id: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/getCircuit?id=${id}`);
  }
}
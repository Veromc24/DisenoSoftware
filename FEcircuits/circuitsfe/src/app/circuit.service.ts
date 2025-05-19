import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Matrix } from './circuit/Matrix';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {
  private baseUrl = 'http://localhost:8080/circuits';

  constructor(private http: HttpClient) {}

  generateCode(outputQubits: number, matrix: Matrix): Observable<any> {
    const body = {
      outputQubits: outputQubits,
      table: matrix?.values,
    };
    const token = sessionStorage.getItem('token');
    console.log('Token:', token);
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    // Solo llamamos a circuits, que se encarga de la lógica de crédito
    return this.http.post(`${this.baseUrl}/generateCode`, body, { headers });
  }

  createCircuit(body: { table: number[][]; outputQubits: number }): Observable<any> {

    return this.http.post(`${this.baseUrl}/createCircuit`, body);
  }

  getCircuit(id: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/getCircuit?id=${id}`);
  }
}
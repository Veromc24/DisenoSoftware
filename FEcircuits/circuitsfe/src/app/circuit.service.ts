import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Matrix } from './circuit/Matrix';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {

  constructor(private http: HttpClient) { }

  generateCode(outputQubits: number, matrix: Matrix, token: String | null): Observable<any> {
    let body = {
      outputQubits: outputQubits,
      table : matrix?.values
    }
   
    let headers = {
      'Authorization': 'Bearer ' + token
    };

    return this.http.post("http://localhost:8080/circuits/generateCode", body);
  }
}
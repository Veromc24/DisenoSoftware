import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Matrix } from './circuit/Matrix';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {

  constructor(private http: HttpClient) { }

  generateCode(outputQubits: number, matrix?: Matrix): Observable<any> {
    const body = {
      outputQubits: outputQubits,
      table: matrix?.values
    };

    // Log the body to the console before sending the request
    console.log("Request Body:", JSON.stringify(body)); 

    return this.http.post("http://localhost:8080/circuits/generateCode", body);
  }
}
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentsService {

  constructor(private client: HttpClient) { }

  prepay(){
    return this.client.get<string>('http://localhost:8082/payments/prepay', { responseType: 'text' as 'json' });
  }

  addCredit(amount: number) {
  const url = 'http://localhost:8081/users/addCredit'; // Cambia la URL según tu backend
  return this.client.post(url, { amount });
}
}

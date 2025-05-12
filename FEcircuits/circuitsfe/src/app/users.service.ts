import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private baseUrl = 'http://localhost:8081/users';

  constructor(private http: HttpClient) {}

  login(name: string, password: string) {
    const body = { name, password };
    return this.http.post(`${this.baseUrl}/loginConBody`, body, { responseType: 'json' });
  }

  signup(user: { name: string; email: string; password: string }) {
    const body = {
      name: user.name,
      email: user.email,
      password: user.password
    };
    return this.http.post(`${this.baseUrl}/signup`, body);
  }
}
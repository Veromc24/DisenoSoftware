import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private baseUrl = 'http://localhost:8081/users';

  constructor(private http: HttpClient) {}

  login(name: string, password: string) {
    // Validar y sanitizar las entradas
    if (!name || !password) {
        throw new Error('El nombre de usuario y la contraseña son obligatorios.');
    }

    const sanitizedBody = {
        name: name.replace(/[^a-zA-Z0-9._-]/g, ''), // Permitir solo caracteres seguros
        password: password.replace(/[^a-zA-Z0-9@#$%^&+=]/g, '') // Permitir solo caracteres seguros
    };

    return this.http.post(`${this.baseUrl}/loginConBody`, sanitizedBody, { responseType: 'json' });
  }

  deleteToken() {
    return this.http.get(`${this.baseUrl}/deleteToken`, { responseType: 'json' });
  }
    
  getEmail(name: string): Observable<{ email: string }> {
    return this.http.get<{ email: string }>(`${this.baseUrl}/getEmail?name=${name}`);
  }
  
  signup(user: { name: string; email: string; password: string }) {
    const body = {
      name: user.name,
      email: user.email,
      password: user.password
    };
    return this.http.post(`${this.baseUrl}/signup`, body);
  }

  recoverPassword(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/recoverPassword`, { email });
  }

  sendPasswordResetEmail(email: string,token:string): Observable<any> {
    return this.http.post(`${this.baseUrl}/sendPasswordResetEmail`, { email,token });
  }
  sendVerifyToken(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/sendVerifyToken`, { email });
  }
  verifyToken(email: string, token: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/verifyToken`, { email, token });
  }

  getCredits(name: string) {
    return this.http.post<{ credits: number, username: string }>(
      `${this.baseUrl}/getCredits`,
      { name }
    );
  }
}
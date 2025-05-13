import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


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
}
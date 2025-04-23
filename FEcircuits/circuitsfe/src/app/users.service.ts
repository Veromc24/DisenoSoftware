import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private baseUrl = 'http://localhost:8081/users';

  constructor(private http: HttpClient) {}

  login(name: string, pwd: string) {
    const body = { name, pwd };
    return this.http.post<string>(`${this.baseUrl}/loginConBody`, body, { responseType: 'text' as 'json' });
  }
}
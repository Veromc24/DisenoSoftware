import { Component } from '@angular/core';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  name?: string;
  pwd?: string;

  constructor(private usersService: UsersService) {}

  login() {
    if (!this.name || !this.pwd) {
      alert('Por favor, ingrese su nombre y contraseña.');
      return;
    }

    this.usersService.login(this.name, this.pwd).subscribe(
      (token) => {
        sessionStorage.setItem('token', token);
        console.log('Login exitoso');
      },
      (error) => {
        console.error('Error en el login', error);
        alert(error);
      }
    );
  }
}

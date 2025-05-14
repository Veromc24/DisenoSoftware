import { Component, EventEmitter, Output } from '@angular/core';
import { UsersService } from '../users.service';
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  @Output() loginSuccess = new EventEmitter<void>();

  name?: string;
  pwd?: string;

  constructor(private usersService: UsersService, private manager: ManagerService) {}

  login() {
    if (!this.name || !this.pwd) {
      alert('Por favor, ingrese su nombre y contraseña.');
      return;
    }

    this.usersService.login(this.name, this.pwd).subscribe(
      (response: any) => {
        console.log('Inicio de sesión exitoso:', response);
        alert('Inicio de sesión exitoso.');
        sessionStorage.setItem('token', response.token); // Guardar el token en el almacenamiento de sesión
        this.loginSuccess.emit();
      },
      (error) => {
        console.error('Error en el login:', error);
        alert('Credenciales inválidas. Inténtelo de nuevo.');
      }
    );
  }
}

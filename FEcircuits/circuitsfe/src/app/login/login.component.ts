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
  email?: string;
  token?: string;
  showTokenField: boolean = false;
  constructor(private usersService: UsersService, private manager: ManagerService) {}

  login() {
    if (!this.name || !this.pwd) {
      alert('Por favor, ingrese su nombre y contraseña.');
      return;
    }

    if (!this.showTokenField) {
      this.showTokenField = true;
      this.usersService.getEmail(this.name).subscribe(
        (response: { email: string }) => {
          this.email = response.email;
          this.usersService.sendVerifyToken(this.email).subscribe(
            (response: any) => {
              console.log('Token enviado con éxito:', response);
              alert('Token enviado a su correo electrónico. Por favor, verifique su bandeja de entrada.');
              
            },
            (error: any) => {
              console.error('Error al enviar el token:', error);
              alert('Hubo un error al enviar el token. Inténtelo de nuevo.');
            }
          );
        },
        (error: any) => {
          console.error('Error al obtener el email:', error);
          alert('Hubo un error al obtener el email. Inténtelo de nuevo.');
        }
      );
      return; // ← IMPORTANTE: Detener aquí el flujo
    }else {

    // Segunda fase: ya se mostró el campo del token
    if (!this.name || !this.pwd || !this.token || !this.email) {
      alert('Por favor, complete todos los campos.');
      return;
    }

    this.usersService.verifyToken(this.email, this.token).subscribe(
      () => {
        alert('Token correcto.');
        this.usersService.login(this.name!, this.pwd!).subscribe(
          (response: any) => {
            localStorage.setItem('username', this.name!);
            sessionStorage.setItem('token', response.token);
            this.loginSuccess.emit();
            alert('Inicio de sesión exitoso.');
          },
          (error) => {
            console.error('Error en el login:', error);
            alert('Credenciales inválidas. Inténtelo de nuevo.');
          }
        );
      },
      (error) => {
        alert('Token equivocado.');
      }
    );
  }
}
}

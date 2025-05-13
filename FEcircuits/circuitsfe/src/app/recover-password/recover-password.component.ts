import { Component } from '@angular/core';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-recover-password',
  standalone: false,
  templateUrl: './recover-password.component.html',
  styleUrls: ['./recover-password.component.css']
})

export class RecoverPasswordComponent {
  email?: string;
  token?: string;

  constructor(private usersService: UsersService) {}

  recoverPassword() {
    if (!this.email) {
      alert('Por favor, ingrese su correo electrónico.');
      return;
    }

    this.usersService.recoverPassword(this.email).subscribe(
      (response) => {
        console.log('Correo de recuperación enviado:', response);
        alert('Se ha enviado un correo para recuperar su contraseña.');
      },
      (error) => {
        console.error('Error al enviar el correo de recuperación:', error);
        alert('Hubo un error al procesar su solicitud. Inténtelo de nuevo.');
      }
    );
  }

  sendPasswordResetEmail() {
    if (!this.email) {
      alert('Por favor, ingrese su correo electrónico.');
      return;
    }
    if (!this.token) {
      alert('Por favor, ingrese el token.');
      return;
    }

    this.usersService.sendPasswordResetEmail(this.email,this.token).subscribe(
      (response) => {
        console.log('Correo de recuperación enviado:', response);
        alert('Se ha enviado un correo para recuperar su contraseña.');
      },
      (error) => {
        console.error('Error al enviar el correo de recuperación:', error);
        alert('Hubo un error al procesar su solicitud. Inténtelo de nuevo.');
      }
    );
  }
}

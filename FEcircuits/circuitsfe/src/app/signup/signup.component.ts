import { Component } from '@angular/core';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-signup',
  standalone: false,
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  name?: string;
  email?: string;
  password?: string;

  constructor(private usersService: UsersService) {}
  passwordErrors: string[] = [];

  signup() {
    if (!this.name || !this.email || !this.password) {
      alert('Por favor, complete todos los campos.');
      return;
    }

    const user = {
      name: this.name,
      email: this.email,
      password: this.password
    };

     this.usersService.signup(user).subscribe(
      (response) => {
        console.log('Usuario registrado con éxito:', response);
        alert('Registro exitoso. Ahora puede iniciar sesión.');
      },
      (error) => {
        console.error('Error en el registro:', error);
        alert('Hubo un error al registrarse. Inténtelo de nuevo.');
      }
    );
  }

  validatePassword() {
    this.passwordErrors = [];
    if (this.password && this.password.length < 8) {
      this.passwordErrors.push('La contraseña debe tener al menos 8 caracteres.');
    }
    if (this.password && !/[A-Z]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos una letra mayúscula.');
    }
    if (this.password && !/[a-z]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos una letra minúscula.');
    }
    if (this.password && !/[0-9]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos un número.');
    }
    if (this.password && !/[!@#$%^&*]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos un carácter especial (!@#$%^&*).');
    }
  }
}

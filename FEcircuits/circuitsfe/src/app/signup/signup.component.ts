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
  token?: string;

  constructor(private usersService: UsersService) {}
  passwordErrors: string[] = [];
  passwordSuggestions: string[] = [];
  showTokenField: boolean = false;

  signup() {
    if (!this.name || !this.email || !this.password) {
      alert('Por favor, complete todos los campos.');
      return;
    }
    if (!this.email.includes('@')) {
      alert('Por favor, ingrese un correo electrónico válido.');
      return;
    }
    if(!this.showTokenField) {
      this.showTokenField = true;
      this.usersService.sendVerifyToken(this.email).subscribe(
        (response: { success: boolean; message: string }) => {
          console.log('Token enviado con éxito:', response);
          alert('Token enviado a su correo electrónico. Por favor, verifique su bandeja de entrada.');
        },
        (error: any) => {
          console.error('Error al enviar el token:', error);
          alert('Hubo un error al enviar el token. Inténtelo de nuevo.');
        });
    }else {
      if (!this.name || !this.email || !this.password|| !this.token) {
      alert('Por favor, complete todos los campos.');
      return;
      }
      this.usersService.verifyToken(this.email, this.token).subscribe(
        (isValid: boolean) => {
          if (!isValid) {
            alert('Token equivocado.');
          } else {
            alert('Token correcto.');

            const user = {
              name: this.name!,
              email: this.email!,
              password: this.password!
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
        },
        (error) => {
          console.error('Error al verificar el token:', error);
          alert('Hubo un error al verificar el token. Inténtelo de nuevo.');
        }
      );
    }
  }

  validatePassword() {
    this.passwordErrors = [];
    this.passwordSuggestions = [];
    // Validaciones
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
    // Sugerencias
    if (this.password && /^[A-Z][^A-Z]*$/.test(this.password)) {
      this.passwordSuggestions.push('La contraseña contiene solo una letra mayúscula y está al inicio.\nSe sugiere poner más mayúsculas o en otras posiciones');
    }
    if (this.password && /^[^0-9]*[0-9]$/.test(this.password)) {
      this.passwordSuggestions.push('La contraseña termina con un número y es el único número en la contraseña.');
    }
    if (this.password && /^[^!@#$%^&*]*[!@#$%^&*]$/.test(this.password)) {
      this.passwordSuggestions.push('La contraseña termina con un carácter especial y es el único carácter especial en la contraseña.');
    }
  }
}

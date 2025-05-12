import { Component } from '@angular/core';
import { UsersService } from '../users.service';
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  name?: string;
  pwd?: string;

  constructor(private usersService: UsersService, private manager: ManagerService) {}

  login() {
    if (!this.name || !this.pwd) {
      alert('Por favor, ingrese su nombre y contraseña.');
      return;
    }

    this.usersService.login(this.name, this.pwd).subscribe(
      (token) => {
       //this.manager.token = token;
      },
      (error) => {
        console.error('Error en el login', error);
        alert(error);
      }
    );
  }
}

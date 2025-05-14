import { Component } from '@angular/core';
import { UsersService } from '../users.service';
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-logout',
  standalone: false,
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent {
    
  constructor(private usersService: UsersService, private manager: ManagerService) {}


  logout() {
    this.usersService.deleteToken().subscribe({
      next: (response: any) => {
        if (response.message === "Logout successful") {
          alert("Cerrando sesión...");
          window.location.reload();
        } else {
          alert("No hay ninguna sesión activa.");
        }
      }
      , error: (error) => {
        console.error("Error al verificar la sesión:", error);
        alert("Error al verificar la sesión.");
      }})

}
}

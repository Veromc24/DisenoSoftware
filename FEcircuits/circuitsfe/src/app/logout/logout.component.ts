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
    
  constructor(private usersService: UsersService) {}


  logout() {
    
  }
}





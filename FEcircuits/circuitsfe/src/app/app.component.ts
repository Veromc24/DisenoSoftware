import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'circuitsfe';
  Username: string | null = '';
  Credits: number = 0;

  ngOnInit() {
    this.Username = localStorage.getItem('username');
    this.updateCredits();
  }

  updateCredits() {
    const credits = localStorage.getItem('credits');
    this.Credits = credits ? Number(credits) : 0;
  }

  mostrarPagos() {
    this.Username = localStorage.getItem('username');
    this.updateCredits();
    const payments = document.getElementById('Pay');
    if (payments) {
      payments.style.display = 'block';
    }
  }
}

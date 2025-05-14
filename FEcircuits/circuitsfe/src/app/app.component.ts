import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'circuitsfe';

  mostrarPagos() {
  const payments = document.getElementById('Pay');
  if (payments) {
    payments.style.display = 'block'; // o 'flex', según tu diseño
  }
}


}

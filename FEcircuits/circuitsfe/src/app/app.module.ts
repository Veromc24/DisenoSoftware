import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { CircuitComponent } from './circuit/circuit.component';
import { PaymentsComponent } from './payments/payments.component';
import { FormsModule } from '@angular/forms'; // Necesario para [(ngModel)]
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CircuitComponent,
    // PaymentsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    PaymentsComponent,
    RouterModule.forRoot([
      { path: 'payments', component: PaymentsComponent }
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

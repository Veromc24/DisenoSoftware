import { Component } from '@angular/core';
import { PaymentsService } from '../payments.service';
import { loadStripe, Stripe, StripeElements, StripeCardElement } from '@stripe/stripe-js';

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css']
})
export class PaymentsComponent {
  amount: number = 3;
  transactionId?: string;
  stripePromise = loadStripe('pk_test_51RHR37IsgNFjbH8wZzoLwgL3ZsHtSVxY4QpoRiy8XNq6bI9yIMDAkNbSLcUBwEZxQYWWbIr2AEyieO08XDAED07r00dm808gpU'); // Carga Stripe de forma asíncrona
  stripe?: Stripe;
  elements?: StripeElements;
  card?: StripeCardElement;

  constructor(private service: PaymentsService) { }

  async prepay() {
    console.log('Iniciando prepay...');
    this.service.prepay().subscribe(
      async (token: any) => {
        console.log('Client secret recibido:', token);
        this.transactionId = token;
        await this.showForm();
      },
      error => {
        console.error('Error al obtener el client_secret:', error);
      }
    );
  }

  async showForm() {
    console.log('Mostrando formulario de pago...');
    const form = document.getElementById('payment-form');
    if (form) {
      form.style.display = 'block';
      console.log('Formulario mostrado.');
    } else {
      console.error('No se encontró el formulario con id "payment-form".');
    }

    const stripe = await this.stripePromise; // Espera a que Stripe se cargue
    if (!stripe) {
      console.error('Stripe no se pudo cargar.');
      return;
    }
    this.stripe = stripe;

    this.elements = this.stripe.elements(); // Crea los elementos de Stripe
    const style = {
      base: {
        color: '#32325d',
        fontFamily: 'Arial, sans-serif',
        fontSize: '16px',
        '::placeholder': {
          color: '#aab7c4'
        }
      },
      invalid: {
        color: '#fa755a',
        iconColor: '#fa755a'
      }
    };

    this.card = this.elements.create('card', { style }); // Crea el elemento de tarjeta
    this.card.mount('#card-element'); // Monta el elemento en el DOM

    this.card.on('change', (event: any) => {
      const displayError = document.getElementById('card-error');
      if (event.error) {
        displayError!.textContent = event.error.message;
      } else {
        displayError!.textContent = '';
      }
    });

    const formElement = document.getElementById('payment-form');
    formElement!.addEventListener('submit', async (event) => {
      event.preventDefault();
      await this.payWithCard();
    });
  }

  async payWithCard() {
    if (!this.stripe || !this.card || !this.transactionId) {
      console.error('Stripe, card o transactionId no están disponibles.');
      return;
    }

    const { error, paymentIntent } = await this.stripe.confirmCardPayment(this.transactionId, {
      payment_method: {
        card: this.card
      }
    });

    if (error) {
      console.error(error.message);
      alert(error.message);
    } else if (paymentIntent && paymentIntent.status === 'succeeded') {
      alert('Pago exitoso');
      this.service.addCredit(this.amount).subscribe(
      (response) => {
        console.log('Crédito añadido exitosamente:', response);
        alert('Crédito añadido a tu cuenta.');
      },
      (error) => {
        console.error('Error al añadir crédito:', error);
        alert('Hubo un error al añadir el crédito.');
      }
    );
      // self.paymentsService.confirm().subscribe({ ... }); // Comentar esta parte según lo solicitado
    }
  }
}



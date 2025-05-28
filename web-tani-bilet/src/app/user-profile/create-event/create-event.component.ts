import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../shared/services/security/auth.service';
import { HttpClient } from '@angular/common/http';
import { Configuration, EventControllerService } from '../../apiv2';
import { SnackBarService } from '../../shared/services/snackbar.service';

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [
    {
      provide: EventControllerService,
      useFactory: (httpClient: HttpClient, authService: AuthService) => {
        const config = new Configuration({
          accessToken: () => authService.token,
        });
        return new EventControllerService(httpClient, '', config);
      },
      deps: [HttpClient, AuthService],
    },
  ],
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.scss',
})
export class CreateEventComponent implements OnInit {
  private eventService = inject(EventControllerService);
  private snackBarService = inject(SnackBarService);
  eventForm!: FormGroup;

  private fb = inject(FormBuilder);

  ngOnInit(): void {
    this.eventForm = this.fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      eventStartTimeMillis: [null, [Validators.required]],
      eventEndTimeMillis: [null, [Validators.required]],
      location: [null, [Validators.required]],
      ticketPrice: [null, [Validators.required, Validators.min(0)]],
      maxTicketCount: [null, [Validators.required, Validators.min(1)]],
      description: [null, [Validators.required, Validators.minLength(1)]],
      isBuyingTicketsTurnedOff: [false, [Validators.required]],
      eventType: 'CONCERT',
    });
  }

  onSubmit(): void {
    if (this.eventForm.valid) {
      const formValue = this.eventForm.value;

      const eventStartISO = new Date(
        formValue.eventStartTimeMillis
      ).toISOString();
      const eventEndISO = new Date(formValue.eventEndTimeMillis).toISOString();

      const payload = {
        ...formValue,
        eventStartTimeMillis: eventStartISO,
        eventEndTimeMillis: eventEndISO,
      };

      this.eventService.createEvent(payload).subscribe({
        next: () => {
          this.snackBarService.showSuccessSnackBar('Utworzono wydarzenie');
          this.eventForm.reset({
            isBuyingTicketsTurnedOff: false,
            eventType: 'CONCERT',
          });
        },
        error: (err) => {
          console.error('Błąd podczas tworzenia wydarzenia', err);
          this.snackBarService.showErrorSnackBar(
            'Błąd podczas tworzenia wydarzenia'
          );
        },
      });
    } else {
      console.log('Formularz zawiera błędy');
    }
  }
}

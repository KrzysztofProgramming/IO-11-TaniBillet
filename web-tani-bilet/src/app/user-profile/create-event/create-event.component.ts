import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { EventControllerService } from '@api/index';

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [EventControllerService],
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.scss',
})
export class CreateEventComponent implements OnInit {
  private eventService = inject(EventControllerService);
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
    });
  }

  onSubmit(): void {
    if (this.eventForm.valid) {
      this.eventService.createEvent(this.eventForm.value).subscribe();
      console.log('user form', this.eventForm);
    } else {
      console.log('Formularz zawiera błędy');
    }
  }
}

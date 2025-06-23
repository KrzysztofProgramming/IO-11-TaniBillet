import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import {
  Configuration,
  EventControllerService,
  EventDto,
} from '../../../apiv2';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../shared/services/security/auth.service';

@Component({
  selector: 'app-modal-edit-event',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule],
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
  templateUrl: './modal-edit-event.component.html',
  styleUrl: './modal-edit-event.component.scss',
})
export class ModalEditEventComponent implements OnInit {
  eventForm!: FormGroup;
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private eventService: EventControllerService,
    public dialogRef: MatDialogRef<ModalEditEventComponent>,
    @Inject(MAT_DIALOG_DATA) public data?: EventDto
  ) {
    this.isEditMode = !!data;
  }

  ngOnInit(): void {
    this.eventForm = this.fb.group({
      name: [
        this.data?.name ?? null,
        [Validators.required, Validators.minLength(3)],
      ],
      eventStartTimeMillis: [
        this.data
          ? this.formatToDatetimeLocal(this.data.eventStartTimeMillis)
          : null,
        [Validators.required],
      ],
      eventEndTimeMillis: [
        this.data
          ? this.formatToDatetimeLocal(this.data.eventEndTimeMillis)
          : null,
        [Validators.required],
      ],
      location: [this.data?.location ?? null, [Validators.required]],
      ticketPrice: [0, [Validators.required, Validators.min(0)]],
      maxTicketCount: [
        this.data?.maxTicketsCount ?? null,
        [Validators.required, Validators.min(1)],
      ],
      description: [
        this.data?.description ?? null,
        [Validators.required, Validators.minLength(1)],
      ],
      isBuyingTicketsTurnedOff: [
        this.data?.isBuyingTicketsTurnedOff ?? false,
        [Validators.required],
      ],
      eventType: [this.data?.eventType ?? null, [Validators.required]],
    });
  }

  eventTypes = [
    { value: 'CONCERT', label: 'Koncert' },
    { value: 'CONFERENCE', label: 'Konferencja' },
    { value: 'WORKSHOP', label: 'Warsztat' },
    { value: 'MEETUP', label: 'Spotkanie' },
    { value: 'FESTIVAL', label: 'Festiwal' },
  ];

  private formatToDatetimeLocal(date: string | Date): string {
    const d = typeof date === 'string' ? new Date(date) : date;
    return d.toISOString().substring(0, 16);
  }

  onSubmit(): void {
    if (this.eventForm.invalid) return;

    const formValue = this.eventForm.value;
    const payload = {
      ...formValue,
      eventStartTimeMillis: new Date(
        formValue.eventStartTimeMillis
      ).toISOString(),
      eventEndTimeMillis: new Date(formValue.eventEndTimeMillis).toISOString(),
    };

    if (this.isEditMode && this.data?.id) {
      this.eventService.updateEvent(payload, this.data.id).subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => console.error('Błąd podczas edycji wydarzenia', err),
      });
    } else {
      this.eventService.createEvent(payload).subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => console.error('Błąd podczas tworzenia wydarzenia', err),
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}

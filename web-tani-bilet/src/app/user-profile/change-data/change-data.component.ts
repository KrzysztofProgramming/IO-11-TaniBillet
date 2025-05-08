import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';

@Component({
  selector: 'app-change-data',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './change-data.component.html',
  styleUrl: './change-data.component.scss',
})
export class ChangeDataComponent {
  @Input() user: any;
  @Output() formSubmitted = new EventEmitter<any>();

  userForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      phoneNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\+?\d{2}[-]?\d{3}[-]?\d{3}[-]?\d{3}$/),
        ],
      ],
      email: ['', [Validators.required, Validators.email]],
      birthDate: ['', Validators.required],
      favoriteCategories: ['', Validators.required],
    });
  }

  ngOnChanges() {
    if (this.user) {
      this.userForm.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        phoneNumber: this.user.phoneNumber,
        email: this.user.email,
        birthDate: this.user.birthDate,
        favoriteCategories: this.user.favoriteCategories.join(', '),
      });
    }
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const formData = this.userForm.value;
      this.formSubmitted.emit({
        ...formData,
        favoriteCategories: formData.favoriteCategories
          .split(',')
          .map((cat: string) => cat.trim()),
      });
    } else {
      console.log('Formularz zawiera błędy');
    }
  }
}

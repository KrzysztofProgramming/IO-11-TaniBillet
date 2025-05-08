import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-show-data',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './show-data.component.html',
  styleUrl: './show-data.component.scss',
})
export class ShowDataComponent {
  user = {
    firstName: 'Jan',
    lastName: 'Kowalski',
    phoneNumber: '+48 123 456 789',
    email: 'jan.kowalski@example.com',
    birthDate: new Date('1990-05-15'),
    favoriteCategories: ['Muzyka', 'Teatr', 'Kino'],
  };

  get age(): number {
    const today = new Date();
    const birthDate = new Date(this.user.birthDate);
    let age = today.getFullYear() - birthDate.getFullYear();
    const m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  }
}

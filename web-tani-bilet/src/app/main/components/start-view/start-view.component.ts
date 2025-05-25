import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { EventsListComponent } from '../../../events/events-list/events-list.component';


@Component({
  selector: 'app-start-view',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatIconModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    EventsListComponent
  ],
  templateUrl: './start-view.component.html',
  styleUrl: './start-view.component.scss'
})
export class StartViewComponent {

  searchQuery: string = '';

  popularEvents = [
    {
      title: 'Poskromienie złośnicy',
      date: '12 czerwca 2025, Kraków',
      image: 'assets/images/theatre.jpg'
    },
    {
      title: 'Męskie Granie',
      date: '20 lipca 2025, Warszawa',
      image: 'assets/images/concert.avif'
    },
    {
      title: 'VNL Liga',
      date: '1 maja 2025, Wrocław',
      image: 'assets/images/sport.jpg'
    },
    {
      title: 'Metallica Tribute',
      date: '14 sierpnia 2025, Katowice',
      image: 'assets/images/rock-music.jpg'
    },
    {
      title: 'Poskromienie złośnicy',
      date: '12 czerwca 2025, Kraków',
      image: 'assets/images/theatre.jpg'
    },
    {
      title: 'Męskie Granie',
      date: '20 lipca 2025, Warszawa',
      image: 'assets/images/concert.avif'
    },
    {
      title: 'VNL Liga',
      date: '1 maja 2025, Wrocław',
      image: 'assets/images/sport.jpg'
    },
    {
      title: 'Metallica Tribute',
      date: '14 sierpnia 2025, Katowice',
      image: 'assets/images/rock-music.jpg'
    }
  ];// z be
  

  onSearch() {
    console.log('Szukam:', this.searchQuery);
  }

}

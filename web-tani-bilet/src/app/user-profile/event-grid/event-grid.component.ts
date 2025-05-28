import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import {
  DataGridComponent,
  TABLE_ACTION_KEY,
  TableAction,
} from '../../shared/components/data-grid/data-grid.component';
import { PageableDto } from '@api/model/pageableDto';
import { EventControllerService } from '@api/api/eventController.service';
import { EventDto } from '@api/model/eventDto';

@Component({
  selector: 'app-event-grid',
  standalone: true,
  imports: [CommonModule, DataGridComponent],
  providers: [EventControllerService],
  templateUrl: './event-grid.component.html',
  styleUrl: './event-grid.component.scss',
})
export class EventGridComponent implements OnInit {
  private eventService = inject(EventControllerService);
  displayedColumns: string[] = [
    'name',
    'eventStartTimeMillis',
    'eventEndTimeMillis',
    'location',
    'ticketPrice',
    'maxTicketCount',
    'description',
    'isBuyingTicketsTurnedOff',
  ];

  changedColumnHeaderNames: Record<string, string> = {
    name: 'Nazwa',
    eventStartTimeMillis: 'Start',
    eventEndTimeMillis: 'Koniec',
    location: 'Miejsce',
    ticketPrice: 'Cena biletu',
    maxTicketCount: 'Liczba biletów',
    description: 'Opis',
    isBuyingTicketsTurnedOff: 'Sprzedaż wyłączona',
  };

  rowButtonAction: TableAction<EventDto>[] = [
    {
      key: TABLE_ACTION_KEY.EDIT,
      name: 'Edytuj',
      color: 'edit-button',
      callback: (event) => this.editEvent(event),
      availabilityFn: function (el?: EventDto | undefined): boolean {
        throw new Error('Function not implemented.');
      }
    },
  ];

  data$: Observable<EventDto[]> = new Observable<EventDto[]>();

  ngOnInit(): void {
    const pageable: PageableDto = {
      pageNumber: 0,
      pageSize: 50,
    };

    this.data$ = this.eventService
      .getEvents(pageable)
      .pipe(map((response) => response.content || []));
  }

  editEvent(event: EventDto | undefined): void {
    if (!event) return;
    console.log('Edytuj wydarzenie:', event);
  }
}

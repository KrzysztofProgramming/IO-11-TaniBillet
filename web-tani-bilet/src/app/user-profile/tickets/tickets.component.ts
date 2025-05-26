import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { map, of } from 'rxjs';
import {
  DataGridComponent,
  TABLE_ACTION_KEY,
  TableAction,
} from '../../shared/components/data-grid/data-grid.component';
import {
  GetTicketDto,
  PageableDto,
  TicketControllerService,
} from '@api/index';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [CommonModule, DataGridComponent],
  providers: [TicketControllerService],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.scss',
})
export class TicketsComponent {
  private ticketService = inject(TicketControllerService);

  data$ = this.ticketService
    .getTicketsForUser({ pageNumber: 0, pageSize: 100 } as PageableDto)
    .pipe(map((res) => res.content ?? []));

  displayedColumns: string[] = [
    'id',
    'seat',
    'boughtPrice',
    'eventId',
    'qrCodeId',
  ];

  columnHeaders = {
    id: 'ID',
    seat: 'Miejsce',
    boughtPrice: 'Cena',
    eventId: 'ID wydarzenia',
    qrCodeId: 'Kod QR',
  };

  rowActions: TableAction<GetTicketDto>[] = [
    {
      key: TABLE_ACTION_KEY.EDIT,
      name: 'Szczegóły',
      color: 'neutral',
      callback: (ticket?: GetTicketDto) => {
        if (!ticket) return;

        alert(
          `Szczegóły biletu:\nID: ${ticket.id}\nWydarzenie: ${ticket.eventId}\nCena: ${ticket.boughtPrice} zł\nMiejsce: ${ticket.seat}`
        );
      },
      availabilityFn: (ticket?: GetTicketDto) => !!ticket,
    },
  ];
}

import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { map, of } from 'rxjs';
import {
  DataGridComponent,
  TableAction,
} from '../../shared/components/data-grid/data-grid.component';
import { PageableDto, TicketControllerService } from '@api/index';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, DataGridComponent],
  providers: [TicketControllerService],
  templateUrl: './transaction-history.component.html',
  styleUrl: './transaction-history.component.scss',
})
export class TransactionHistoryComponent {
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

  rowActions: TableAction<any>[] = [];
}

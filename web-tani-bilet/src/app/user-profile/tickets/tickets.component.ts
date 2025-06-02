import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { map, of } from 'rxjs';
import {
  DataGridComponent,
  TABLE_ACTION_KEY,
  TableAction,
} from '../../shared/components/data-grid/data-grid.component';
import { GetTicketDto } from '@api/index';
import { Configuration, TicketControllerService } from '../../apiv2';
import { AuthService } from '../../shared/services/security/auth.service';
import { HttpClient } from '@angular/common/http';
import { QRCodeModule } from 'angularx-qrcode';
import { MatDialog } from '@angular/material/dialog';
import { QrModuleComponentComponent } from './qr-module-component/qr-module-component.component';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [CommonModule, DataGridComponent, QRCodeModule],
  providers: [
    {
      provide: TicketControllerService,
      useFactory: (httpClient: HttpClient, authService: AuthService) => {
        const config = new Configuration({
          accessToken: () => authService.token,
        });
        return new TicketControllerService(httpClient, '', config);
      },
      deps: [HttpClient, AuthService],
    },
  ],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.scss',
})
export class TicketsComponent {
  private ticketService = inject(TicketControllerService);

  private matDialog = inject(MatDialog);
  data$ = this.ticketService.getTicketsForUser().pipe(
    map((res) => {
      const now = new Date();
      return (res ?? []).filter(
        (ticket) => new Date(ticket.eventEndTime) > now
      );
    })
  );

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

  selectedTicketForQr: GetTicketDto | null = null;

  rowActions: TableAction<GetTicketDto>[] = [
    {
      key: TABLE_ACTION_KEY.EDIT,
      name: 'Wyświetl QR',
      color: 'neutral',
      callback: (ticket?: GetTicketDto) => {
        if (!ticket) return;
        this.openQrDialog(ticket);
      },
      availabilityFn: (ticket?: GetTicketDto) => !!ticket,
    },
  ];

  openQrDialog(ticket: GetTicketDto) {
    this.matDialog.open(QrModuleComponentComponent, {
      data: {
        id: ticket.id,
        qrCodeId: ticket.qrCodeId,
      },
      width: '320px',
    });
  }
}

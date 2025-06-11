import { CommonModule, formatDate } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import {
  ColumnTypeEnum,
  DataGridComponent,
  TABLE_ACTION_KEY,
  TableAction,
  TableColumnSettings,
} from '../../shared/components/data-grid/data-grid.component';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../shared/services/security/auth.service';
import { Configuration, EventControllerService, EventDto } from '../../apiv2';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ModalEditEventComponent } from './modal-edit-event/modal-edit-event.component';
import { SnackBarService } from '../../shared/services/snackbar.service';
import { ChangedTableColumnNames, KeyOfUnion, TableColumnNames } from '../../shared/models/tableColumn.type';
import { ListViewComponent } from '../../shared/components/list-view/list-view.component';

@Component({
  selector: 'app-event-grid',
  standalone: true,
  imports: [
    CommonModule,
    DataGridComponent,
    MatDialogModule,
    ListViewComponent
  ],
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
  templateUrl: './event-grid.component.html',
  styleUrl: './event-grid.component.scss',
})
export class EventGridComponent implements OnInit {
  private eventService = inject(EventControllerService);
  private matDialog = inject(MatDialog);
  private snackBarService = inject(SnackBarService);
  displayedColumns: TableColumnNames<EventDto> = [
    'id',
    'name',
    'eventStartTimeMillis',
    'eventEndTimeMillis',
    'location',
    'description',
    'isBuyingTicketsTurnedOff',
    'maxTicketsCount',
    'ticketsSoldCount',
    'eventType',
  ];

  changedColumnHeaderNames: ChangedTableColumnNames<EventDto> = {
    id: 'ID',
    name: 'Nazwa',
    eventStartTimeMillis: 'Start',
    eventEndTimeMillis: 'Koniec',
    location: 'Miejsce',
    description: 'Opis',
    isBuyingTicketsTurnedOff: 'Sprzedaż wyłączona',
    maxTicketsCount: 'Maksymalna liczba biletów',
    ticketsSoldCount: 'Sprzedane bilety',
    eventType: 'Typ wydarzenia',
  };

  displayedColumnSettings: TableColumnSettings = {
      'eventStartTimeMillis': ColumnTypeEnum.DATE,
      'eventEndTimeMillis': ColumnTypeEnum.DATE,
      'isBuyingTicketsTurnedOff': ColumnTypeEnum.BOOLEAN
  }

  rowActions: TableAction<EventDto>[] = [
    {
      key: TABLE_ACTION_KEY.EDIT,
      name: 'Edytuj',
      color: 'edit-button',
      callback: (event?: EventDto) => {
        if (!event) return;
        this.openEditDialog(event);
      },
      availabilityFn: (el?: EventDto) => !!el,
    },
    {
      key: TABLE_ACTION_KEY.DELETE,
      name: 'Usuń',
      color: 'warn',
      callback: (event?: EventDto) => {
        if (!event || !event.id) return;
        this.deleteEvent(event.id);
      },
      availabilityFn: (el?: EventDto) => !!el?.id,
    },
  ];

  data$: Observable<EventDto[]> = this.loadEvents();

  ngOnInit(): void {

  }

  openEditDialog(event: EventDto) {
    const dialogRef = this.matDialog.open(ModalEditEventComponent, {
      data: event,
      width: '600px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadEvents();
      }
    });
  }

  private loadEvents() {
    return this.eventService.getEvents();
  }

  editEvent(event: EventDto | undefined): void {
    if (!event) return;
    console.log('Edytuj wydarzenie:', event);
  }

  deleteEvent(id: number): void {
    if (!confirm('Czy na pewno chcesz usunąć to wydarzenie?')) return;

    this.eventService.deleteEvent(id).subscribe({
      next: () => {
        console.log('Wydarzenie usunięte');
        this.loadEvents();
        this.snackBarService.showSuccessSnackBar('Wydarzenie usunięte');
      },
      error: (err) => {
        console.error('Błąd podczas usuwania wydarzenia', err);
      },
    });
  }
}

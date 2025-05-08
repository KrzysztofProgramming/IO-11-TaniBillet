import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
  ],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.scss',
})
export class TicketsComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  activeTickets = [
    {
      id: 1,
      event: 'Koncert',
      date: '2023-06-10',
      place: 'Warszawa',
      status: 'Aktywny',
    },
    {
      id: 2,
      event: 'Teatr',
      date: '2023-06-12',
      place: 'Kraków',
      status: 'Aktywny',
    },
    {
      id: 3,
      event: 'Film',
      date: '2023-06-15',
      place: 'Wrocław',
      status: 'Aktywny',
    },
    {
      id: 4,
      event: 'Konferencja',
      date: '2023-06-20',
      place: 'Poznań',
      status: 'Aktywny',
    },
  ];

  displayedColumns: string[] = [
    'id',
    'event',
    'date',
    'place',
    'status',
    'details',
  ];
  dataSource = new MatTableDataSource(this.activeTickets);

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  showDetails(ticket: any) {
    alert(
      `Szczegóły biletu:\nEvent: ${ticket.event}\nData: ${ticket.date}\nMiejsce: ${ticket.place}\nStatus: ${ticket.status}`
    );
  }
}

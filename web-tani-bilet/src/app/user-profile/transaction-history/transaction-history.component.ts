import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatSortModule],
  templateUrl: './transaction-history.component.html',
  styleUrl: './transaction-history.component.scss',
})
export class TransactionHistoryComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  transactions = [
    {
      id: 1,
      date: '2023-05-01',
      amount: 100,
      description: 'Zakup biletu na koncert',
      place: 'Warszawa',
    },
    {
      id: 2,
      date: '2023-05-03',
      amount: 200,
      description: 'Zakup biletu na teatr',
      place: 'Kraków',
    },
    {
      id: 3,
      date: '2023-05-07',
      amount: 50,
      description: 'Zakup biletu na film',
      place: 'Wrocław',
    },
    {
      id: 4,
      date: '2023-05-10',
      amount: 300,
      description: 'Zakup biletu na konferencję',
      place: 'Poznań',
    },
  ];

  displayedColumns: string[] = ['id', 'date', 'amount', 'description', 'place'];
  dataSource = new MatTableDataSource(this.transactions);

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}

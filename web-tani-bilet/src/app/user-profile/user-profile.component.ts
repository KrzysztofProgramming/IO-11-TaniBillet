import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { ShowDataComponent } from './show-data/show-data.component';
import { ChangeDataComponent } from './change-data/change-data.component';
import { SupportComponent } from './support/support.component';
import { TicketsComponent } from './tickets/tickets.component';
import { TransactionHistoryComponent } from './transaction-history/transaction-history.component';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    ShowDataComponent,
    ChangeDataComponent,
    SupportComponent,
    TicketsComponent,
    TransactionHistoryComponent
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
})
export class UserProfileComponent {
  activeComponent: string = 'showData';

  showData() {
    this.activeComponent = 'showData';
  }

  editData() {
    this.activeComponent = 'editData';
  }

  viewTransactionHistory() {
    this.activeComponent = 'transactionHistory';
  }

  viewActiveTickets() {
    this.activeComponent = 'activeTickets';
  }

  deleteAccount() {
    this.activeComponent = 'deleteAccount';
  }

  requestRefund() {
    this.activeComponent = 'requestRefund';
  }
}

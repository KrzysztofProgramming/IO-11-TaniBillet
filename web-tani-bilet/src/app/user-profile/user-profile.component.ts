import { Component, inject, OnInit } from '@angular/core';
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
import { CreateEventComponent } from './create-event/create-event.component';
import { EventsListComponent } from '../events/events-list/events-list.component';
import { EventGridComponent } from './event-grid/event-grid.component';
import { AuthService } from '../shared/services/security/auth.service';

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
    TicketsComponent,
    TransactionHistoryComponent,
    CreateEventComponent,
    EventGridComponent,
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
})
export class UserProfileComponent {
  private authService = inject(AuthService);

  get isEventCreator(): boolean {
    return this.authService.hasRole('event_creator');
  }

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

  createEvent() {
    this.activeComponent = 'createEvent';
  }
  eventList() {
    this.activeComponent = 'eventList';
  }
}

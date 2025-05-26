import { Component, Input, input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { DataGridComponent, TableAction, TABLE_ACTION_KEY } from '../../shared/components/data-grid/data-grid.component';
import { ListViewComponent } from '../../shared/components/list-view/list-view.component';
import { TableColumnNames, ChangedTableColumnNames } from '../../shared/models/tableColumn.type';
import { EventControllerService } from '@api/api/eventController.service';
import { filter, map, Observable, of } from 'rxjs';
import { EventDto } from '@api/model/eventDto';
import { AuthService } from '../../shared/services/security/auth.service';
import { TicketPurchaseOptionsModalComponent } from '../../tickets/ticket-purchase-options-modal/ticket-purchase-options-modal.component';
import { TicketPurchaseModalComponent } from '../../tickets/ticket-purchase-modal/ticket-purchase-modal.component';
@Component({
  selector: 'app-events-list',
  templateUrl: './events-list.component.html',
  styleUrls: ['./events-list.component.scss'],
  standalone: true,
  imports: [
    DataGridComponent,
    ListViewComponent,
    MatDialogModule
  ],
  providers: [
    EventControllerService,

  ]
})
export class EventsListComponent implements OnInit, OnChanges{

  @Input()
  searchQuery: string = '';

  data$: Observable<EventDto[] | undefined> = of([]);

  constructor( 
    private matDialog: MatDialog, 
    private _eventService: EventControllerService,
    private _authService: AuthService) { }

  ngOnInit(): void {
    this.data$ = this.getEvents();
    const pendingAction = localStorage.getItem('pendingAction');

    if (pendingAction === 'buy-ticket') {
      localStorage.removeItem('pendingAction');
      // this.showTicketPurchaseModal(true);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['searchQuery']){
      this.data$ = this.getEvents();
    }
  }

  private getEvents(){
    return this._eventService.getEvents({ pageSize: 100, pageNumber: 0 }).pipe(
      map(data => {
        return this.searchQuery
          ? data.content?.filter(event => event.name?.toLowerCase().includes(this.searchQuery.toLowerCase()))
          : data.content;
      })
    );  
  }

  displayedColumns: TableColumnNames<EventDto> = ['name', 'eventStartTimeMillis', 'eventEndTimeMillis', 'location'];

  changedColumnHeaderNames: ChangedTableColumnNames<EventDto> = {
    name: 'Nazwa wydarzenia',
    eventStartTimeMillis: 'Data rozpoczęcia wydarzenia',
    eventEndTimeMillis: 'Data zakończenia wydarzenia',
    location: 'Miejsce'
  }

  rowButtonAction: TableAction<EventDto>[] = [
    { key: TABLE_ACTION_KEY.ADD, name: "Kup bilet", icon: "add", color: "button-green", availabilityFn: el => !!el,  callback: (el) => this.onBuyTicketClicked(el!) },
    // { key: TABLE_ACTION_KEY.EDIT, name: "Edytuj", icon: "edit", color: "button-yellow", availabilityFn: el => !!el, callback: () => { } },
    // { key: TABLE_ACTION_KEY.DELETE, name: "Usuń", icon: "delete", color: "button-red", availabilityFn: el => el?.location != 'Krk', callback: el => this.onDeleteClicked(el!) },
  ]

  onBuyTicketClicked(event: EventDto) {
    if(this._authService.isLoggedIn){
      this.showTicketPurchaseModal(event, true);
    }else{
      const questionDialogRef = this.matDialog.open(TicketPurchaseOptionsModalComponent, {
        autoFocus: false,
        restoreFocus: false,
        disableClose: true,
      })

      questionDialogRef.afterClosed().subscribe(asGuest => {
        if(asGuest){
          this.showTicketPurchaseModal(event, !asGuest)
        }
      });
    }
  }

  private showTicketPurchaseModal(event: EventDto, isLoggedIn: boolean){
    const questionDialogRef = this.matDialog.open(TicketPurchaseModalComponent, {
      autoFocus: false,
      restoreFocus: false,
      disableClose: true,
      data: event,
      maxHeight: '300px'
    })

    // questionDialogRef.afterClosed().subscribe(result => {
    //   if (!!result) {
    //   }
    // });
  }

  onEditClicked(item: EventDto) {
    console.log(item)
  }

  onDeleteClicked(item: EventDto) {
    // this.placowkiService.deleteFacility(item.id_placowki).subscribe();
  }

}

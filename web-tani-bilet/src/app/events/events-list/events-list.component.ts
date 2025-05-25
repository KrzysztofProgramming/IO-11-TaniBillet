import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { DataGridComponent, TableAction, TABLE_ACTION_KEY } from '../../shared/components/data-grid/data-grid.component';
import { ListViewComponent } from '../../shared/components/list-view/list-view.component';
import { TableColumnNames, ChangedTableColumnNames } from '../../shared/models/tableColumn.type';
import { EventControllerService } from '@api/api/eventController.service';
import { map, Observable, of } from 'rxjs';
import { PageEventDto } from '@api/model/pageEventDto';
import { EventDto } from '@api/model/eventDto';
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
export class EventsListComponent implements OnInit{

  data$: Observable<EventDto[] | undefined> = of([]);

  constructor( private matDialog: MatDialog, private _eventService: EventControllerService) { }

  ngOnInit(): void {
    this.data$ = this._eventService.getEvents({pageSize: 100, pageNumber: 0}).pipe(map(el => el.content));
    this.data$.subscribe(el => console.log(el))
  }

  displayedColumns: TableColumnNames<EventDto> = ['name', 'eventStartTimeMillis', 'eventEndTimeMillis', 'location'];

  changedColumnHeaderNames: ChangedTableColumnNames<EventDto> = {
    name: 'Nazwa wydarzenia',
    eventStartTimeMillis: 'Data rozpoczęcia wydarzenia',
    eventEndTimeMillis: 'Data zakończenia wydarzenia',
    location: 'Miejsce'
  }

  rowButtonAction: TableAction<EventDto>[] = [
    { key: TABLE_ACTION_KEY.ADD, name: "Kup bilet", icon: "add", color: "button-green", callback: () => this.onAddClicked() },
    { key: TABLE_ACTION_KEY.EDIT, name: "Edytuj", icon: "edit", color: "button-yellow", callback: () => { } },
    { key: TABLE_ACTION_KEY.DELETE, name: "Usuń", icon: "delete", color: "button-red", callback: el => this.onDeleteClicked(el!) },
  ]

  onAddClicked() {
    // const questionDialogRef = this.matDialog.open(PlacowkiFormComponent, {
    //   autoFocus: false,
    //   restoreFocus: false,
    //   disableClose: true,
    //   maxHeight: '300px'
    // })

    // questionDialogRef.afterClosed().subscribe(result => {
    //   if (!!result) {
    //     this.data$ = this.placowkiService.getAllFacilities();
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

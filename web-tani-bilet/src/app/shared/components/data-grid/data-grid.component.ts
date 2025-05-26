import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableModule } from '@angular/material/table';
import { catchError, map, Observable, of } from 'rxjs';
import { LoadingComponent } from '../loading/loading.component';
import { LoadingService } from '../../services/loading/loading.service';

export interface TableAction<T> {
  key: TABLE_ACTION_KEY;
  name: string;
  icon?: string;
  color?: string;
  callback: (el?: T) => void;
  availabilityFn: (el?: T) => boolean;
}

export enum TABLE_ACTION_KEY {
  ADD = 'ADD',
  EDIT = 'EDIT',
  DELETE = 'DELETE'
}

@Component({
  selector: 'app-data-grid',
  templateUrl: './data-grid.component.html',
  styleUrls: ['./data-grid.component.scss'],
  standalone: true,
  imports: [
    MatRadioModule,
    MatTableModule,
    CommonModule,
    MatButtonModule,
    LoadingComponent
  ],
  providers: [LoadingService]
})
export class DataGridComponent<T> implements OnInit{

  @Input()
  displayedColumns: string[] = [];

  @Input()
  data$!: Observable<T[] | undefined>;

  @Input()
  changedColumnHeaderNames: Record<string, any> = {};

  @Input()
  rowButtonAction!: TableAction<T>[];

  selectedRow!: T;

  columnsToDisplay: string[] = [];
  dataModel$!: Observable<T[]>;

  data: T[] = []

  constructor(private _loadingService: LoadingService){}


  ngOnInit(): void {
    this.columnsToDisplay = !this.rowButtonAction.length ? this.displayedColumns : ['select', ...this.displayedColumns];

  }

  rowClicked(item: T){
    this.selectedRow = item;
  }

  rowButtonActionAvailability(action: TableAction<T> | undefined): boolean {
    if (action?.availabilityFn) {
        return action.availabilityFn(this.selectedRow);
      }
      return false;
  }

}

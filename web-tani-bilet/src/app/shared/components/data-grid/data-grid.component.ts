import { CommonModule, formatDate } from '@angular/common';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableModule } from '@angular/material/table';
import { BehaviorSubject, catchError, map, Observable, of, switchMap, tap } from 'rxjs';
import { LoadingComponent } from '../loading/loading.component';
import { LoadingService } from '../../services/loading/loading.service';
import { KeyOfUnion } from '../../models/tableColumn.type';

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

export enum ColumnTypeEnum {
  DATE = 'DATE',
  BOOLEAN = 'BOOLEAN'
}

export type TableColumnSettings = { [key: string]: ColumnTypeEnum }

export type ChangesHeaderNames<T> = Partial<Record<KeyOfUnion<T>, string>>;

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
export class DataGridComponent<T> implements OnInit, OnChanges{

  @Input()
  displayedColumns: string[] = [];

  // @Input() set data(value: T[] | undefined){
  //   this.visibleData = value;
  //   this.originalData = value;
  // };

  @Input()
  data$: Observable<T[]> = of([]);

  @Input()
  changedColumnHeaderNames: { [key: string]: string } = {};

  @Input()
  displayedColumnSettings: { [key: string]: ColumnTypeEnum } = {};

  @Input()
  rowButtonAction!: TableAction<T>[];

  @Input()
  searchQuery!: string;

  @Input()
  searchKey!: KeyOfUnion<T>;

  selectedRow!: T;
  private originalData: T[] | undefined;
  visibleData: T[] | undefined;
  columnsToDisplay: string[] = [];
  private dataSourceRefreshTrigger$ = new BehaviorSubject(true);

  readonly ColumnTypeEnum = ColumnTypeEnum;

  constructor(private _loadingService: LoadingService){}

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['searchQuery'] && !changes['searchQuery'].firstChange){
      this.visibleData = this.searchQuery ? 
        this.originalData?.filter(el => el[this.searchKey]?.toString().toLowerCase().includes(this.searchQuery.toLowerCase())) :
        this.originalData;
    }
  }


  ngOnInit(): void {
    // this.data$ = this.dataSourceRefreshTrigger$.pipe(
    //   switchMap(el => {
    //     return this.data$?.pipe(
    //       this._loadingService.showLoaderUntilCompleted(),
    //       tap(data => {
    //         this.originalData = data;
    //         this.visibleData = data;
    //       })
    //     )
    //   })
        
    // );

    this.data$ = this.data$?.pipe(
          this._loadingService.showLoaderUntilCompleted(),
          tap(data => {
            this.originalData = data;
            this.visibleData = data;
          })
        )

    // setTimeout(()=>this.refresh(),5000)

    this.columnsToDisplay = !this.rowButtonAction.length ? this.displayedColumns : ['select', ...this.displayedColumns];
  }

  refresh(){
    this.dataSourceRefreshTrigger$.next(true);
  }

  formatDate(value: Date){
    return formatDate(
      value,
      'd MMM yyyy HH:mm',
      'pl'
    )
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

//   applyFiltering(): void {
//   this.dataModel$ = this.data$.pipe(
//     map((data: T[] | undefined) => {
//       if (!data) return [];

//       const query = this.searchQuery?.trim().toLowerCase() || '';
//       const firstColumn = this.displayedColumns[0];

//       if (!query) {
//         return data;
//       }

//       return data.filter((item) => {
//         const value = (item as any)[firstColumn];
//         return value && value.toString().toLowerCase().includes(query);
//       });
//     }),
//     catchError(() => of([]))
//   );
// }

}

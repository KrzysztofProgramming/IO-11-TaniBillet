import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActionButtonsModal, ModalViewComponent } from '../../shared/components/modal-view/modal-view.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EventDto } from '@api/index';

@Component({
  selector: 'app-ticket-purchase-modal',
  standalone: true,
  imports: [
    ModalViewComponent,
    ReactiveFormsModule
  ],
  templateUrl: './ticket-purchase-modal.component.html',
  styleUrl: './ticket-purchase-modal.component.scss'
})
export class TicketPurchaseModalComponent implements OnInit {

  actionButtons: ActionButtonsModal[] = [];

  formGroup = new FormGroup({});
  eventDto: EventDto | undefined;

  constructor(
    private dialogRef: MatDialogRef<TicketPurchaseModalComponent>,
    @Inject(MAT_DIALOG_DATA) public event: EventDto
  ){
    this.eventDto = event;
    console.log(event)
  }

  ngOnInit(): void {
    this.actionButtons = this.getActionButtons();
    this.setFormGroup();
  }

  private setFormGroup(){
    // this.formGroup.addControl()
  }

  private buyTicket(){

  }

  private getActionButtons(): ActionButtonsModal[]{
    return [
      {
      primary: true,
      title: 'Kup',
      actionFn: () => this.buyTicket(),
      disabledFn: () => this.formGroup.invalid
    },
    {
      primary: false,
      title: 'Wyjdź',
      actionFn: () => this.dialogRef.close()
    }
    ]
  }

}

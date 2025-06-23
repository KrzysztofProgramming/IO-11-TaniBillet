import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActionButtonsModal, ModalViewComponent } from '../../shared/components/modal-view/modal-view.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../../shared/services/security/auth.service';
import { Configuration, EventDto, GetTicketDto, OrderTicketDto, OrderTicketUnauthenticatedDto, TicketControllerService } from '../../apiv2';
import { SnackBarService } from '../../shared/services/snackbar.service';
import { error } from 'console';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-ticket-purchase-modal',
  standalone: true,
  imports: [
    ModalViewComponent,
    ReactiveFormsModule
  ],
  templateUrl: './ticket-purchase-modal.component.html',
  styleUrl: './ticket-purchase-modal.component.scss',
  providers: [
    {
          provide: TicketControllerService,
          useFactory: (httpClient: HttpClient, authService: AuthService) => {

            const config = authService.isLoggedIn ? new Configuration({
              accessToken: () => authService.token,
            }) : new Configuration();
            return new TicketControllerService(httpClient, '', config);
          },
          deps: [HttpClient, AuthService],
        },
  ]
})
export class TicketPurchaseModalComponent implements OnInit {

  actionButtons: ActionButtonsModal[] = [];

  formGroup: FormGroup;
  eventDto: EventDto | undefined;
  isLoggedIn = false;
  waitingForResult = false;

  constructor(
    private dialogRef: MatDialogRef<TicketPurchaseModalComponent>,
    private _authService: AuthService,
    @Inject(MAT_DIALOG_DATA) public event: EventDto,
    private _ticketService: TicketControllerService,
    private _msgSnackbarService: SnackBarService
  ){
    this.isLoggedIn = this._authService.isLoggedIn
    this.eventDto = event;
    this.formGroup = new FormGroup({
      'ticketNumber': new FormControl(null, [Validators.required, Validators.min(1)])
    })
    if(!this.isLoggedIn){
      this.formGroup.addControl('email', new FormControl(null, [Validators.required]));
    }
  }

  ngOnInit(): void {
    this.actionButtons = this.getActionButtons();
  }

  private buyTicket(){
    let observable$: Observable<GetTicketDto[]>;

    if(this.isLoggedIn){
      const body: OrderTicketDto = { ticketsCount: this.formGroup.get('ticketNumber')?.value, eventId: this.eventDto?.id!}
      observable$ = this._ticketService.orderTicket(body);
    }else{
      const body: OrderTicketUnauthenticatedDto = {
        ticketsCount: this.formGroup.get('ticketNumber')?.value,
        email: this.formGroup.get('email')?.value,
        eventId: this.eventDto?.id!
      }

      observable$ = this._ticketService.orderTicket1(body);
    }
    this.waitingForResult = true
    // console
    observable$.subscribe({
        next: _ => {
          this.waitingForResult = false;
          this._msgSnackbarService.showSuccessSnackBar("Pomyślnie zakupiono bilet na " + this.eventDto?.name);
          this.dialogRef.close(true);
        },
        error: err => {
          console.log(err)
          this.waitingForResult = false;
          this._msgSnackbarService.showErrorSnackBar(JSON.stringify(err?.error?.errors) || "Wystąpił błąd")
        }
      })
  }

  private getActionButtons(): ActionButtonsModal[]{
    return [
      {
      primary: true,
      title: 'Kup',
      actionFn: () => this.buyTicket(),
      disabledFn: () => this.formGroup.invalid || this.waitingForResult
    },
    {
      primary: false,
      title: 'Wyjdź',
      actionFn: () => this.dialogRef.close()
    }
    ]
  }

}

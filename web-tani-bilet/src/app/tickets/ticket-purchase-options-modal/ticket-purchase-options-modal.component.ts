import { Component } from '@angular/core';
import { ActionButtonsModal, ModalViewComponent } from '../../shared/components/modal-view/modal-view.component';
import { MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../../shared/services/security/auth.service';

@Component({
  selector: 'app-ticket-purchase-options-modal',
  standalone: true,
  imports: [
    ModalViewComponent
  ],
  templateUrl: './ticket-purchase-options-modal.component.html',
  styleUrl: './ticket-purchase-options-modal.component.scss'
})
export class TicketPurchaseOptionsModalComponent {

  actionButtons: ActionButtonsModal[] = [
    {
      primary: true,
      title: 'Zaloguj się',
      actionFn: () => this.login()
    },
    {
      primary: false,
      title: 'Kup jako gość',
      actionFn: () => this.dialogRef.close(true)
    }
  ]

  constructor(
    private dialogRef: MatDialogRef<TicketPurchaseOptionsModalComponent>,
    private _authService: AuthService
  ){}

  private login(){
    localStorage.setItem('pendingAction', 'buy-ticket');
    this.dialogRef.close(false);
    this._authService.login();
  }

}

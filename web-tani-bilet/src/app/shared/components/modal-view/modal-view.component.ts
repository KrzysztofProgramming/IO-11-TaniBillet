import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

export interface ActionButtonsModal{
  title: string;
  primary: boolean
  actionFn: () => void;
  disabledFn?: () => boolean;
  class?: string;
}

@Component({
    selector: 'app-modal-view',
    standalone: true,
    imports: [MatButtonModule, CommonModule, MatIconModule],
    templateUrl: './modal-view.component.html',
    styleUrl: './modal-view.component.scss'
})
export class ModalViewComponent {

  @Input()
  actionButtons!: ActionButtonsModal[];

  @Input()
  title!: string;

  public isDisabled(btn: ActionButtonsModal) {
    if (btn.disabledFn) {
      return btn.disabledFn();
    }

    return !!btn.disabledFn;
  }

}

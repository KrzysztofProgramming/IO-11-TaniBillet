import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { QRCodeModule } from 'angularx-qrcode';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-qr-code-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatDialogContent,
    MatDialogActions,
    MatButtonModule,
    QRCodeModule,
  ],
  templateUrl: './qr-module-component.component.html',
  styleUrls: ['./qr-module-component.component.scss'],
})
export class QrModuleComponentComponent {
  constructor(
    public dialogRef: MatDialogRef<QrModuleComponentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { id: number; qrCodeId: string }
  ) {}

  onClose() {
    this.dialogRef.close();
  }
}

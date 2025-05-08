import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CommonModule } from '@angular/common';
import { SideMenuComponent } from "../side-menu/side-menu.component";
import { Router, RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    SideMenuComponent,
    RouterOutlet,
    MatIconModule
],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

  constructor(
    private _router: Router
  ){

  }

  goToUserProfile(){
    this._router.navigate(['user-profile']);
  }

  goToMain(){
    this._router.navigate(['']);
  }
}

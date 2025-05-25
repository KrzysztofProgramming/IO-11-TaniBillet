import { Component, inject, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CommonModule } from '@angular/common';
import { SideMenuComponent } from "../side-menu/side-menu.component";
import { Router, RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../shared/services/security/auth.service';
import { SnackBarService } from '../../../shared/services/snackbar.service';

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
export class MainComponent implements OnInit {

  private auth = inject(AuthService);

  constructor(
    private _router: Router,
    private _snackbarService: SnackBarService
  ){
  }

  ngOnInit(): void {
    const pendingAction = localStorage.getItem('logginIn');

    if (pendingAction === 'loggedIn') {
      localStorage.removeItem('logginIn');
      this._snackbarService.showSuccessSnackBar('Zalogowano pomyślnie');
    }
  }

  get loggedIn() { return this.auth.isLoggedIn; };

  login() {
    if(this.loggedIn){
      this.auth.logout();
      this._snackbarService.showSuccessSnackBar('Wylogowano pomyślnie');
    }else{
      localStorage.setItem('logginIn', 'loggedIn');
      this.auth.login();
    }
  }

  goToUserProfile(){
    this._router.navigate(['user-profile']);
  }

  goToMain(){
    this._router.navigate(['']);
  }
}

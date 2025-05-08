import { Routes } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./main/main.routes').then(m => m.MAIN_ROUTES),
  },
  {
    path: '**',
    redirectTo: '/'
  }
];

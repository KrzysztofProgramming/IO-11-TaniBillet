import { Route, Routes } from "@angular/router";
import { MainComponent } from "./components/main/main.component";
import { StartViewComponent } from "./components/start-view/start-view.component";

export const MAIN_ROUTES: Routes = [
    {
        path: '',
        component: MainComponent,
        children: [
            {
                path: 'start-view',
                component: StartViewComponent,
            },
            {
                path: 'user-profile',
                loadChildren: () => import('../user-profile/user-profile.routes').then(m => m.USER_PROFILE),
            },
            {
                path: '',
                redirectTo: 'start-view',
                pathMatch: 'full'
            },
        ]
    },
    {
        path: '**',
        redirectTo: ''
    }
]
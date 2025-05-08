import { Route } from "@angular/router";
import { UserProfileComponent } from "./user-profile.component";

export const USER_PROFILE: Route[] = [
    {
        path: '',
        children: [
            {
                path: '',
                component: UserProfileComponent
            }
        ]
    }
]
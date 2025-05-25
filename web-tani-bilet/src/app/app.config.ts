import { APP_INITIALIZER, ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { KeycloakService } from './shared/services/security/keycloak.service';
import { provideHttpClient } from '@angular/common/http';

const keycloak = new KeycloakService();

export const appConfig: ApplicationConfig = {
  providers: [
    KeycloakService,
    // {
    //   provide: APP_INITIALIZER,
    //   useFactory: (keycloak: KeycloakService) => () =>
    //     keycloak.init(),  // możesz tu też podać opcje
    //   multi: true,
    //   deps: [KeycloakService],
    // },
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(),
    provideAnimationsAsync(),
    provideHttpClient()
  ],
};
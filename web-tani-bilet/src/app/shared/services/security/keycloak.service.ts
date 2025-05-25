import { Injectable } from '@angular/core';
import Keycloak, { KeycloakInitOptions } from 'keycloak-js';

@Injectable({ providedIn: 'root' })
export class KeycloakService {
  private keycloak: Keycloak;

  constructor() {
    this.keycloak = new Keycloak({
      url: 'http://localhost:8080/auth',
      realm: 'tani-bilet',
        clientId: 'tani-bilet-app',
    });
  }

  init(options?: KeycloakInitOptions): Promise<boolean> {
    // Zapobieganie uruchomieniu w środowiskach bez 'window'
    if (typeof window === 'undefined') {
      console.warn('Keycloak init skipped – no window object');
      return Promise.resolve(false);
    }

    this.keycloak = new Keycloak({
      url: 'http://localhost:8080/auth',
      realm: 'tani-bilet',
      clientId: 'tani-bilet-realm',
    });

    return this.keycloak
      .init({
        onLoad: 'check-sso',
        checkLoginIframe: false,
        ...options,
      })
      .then(authenticated => {
        console.log('Authenticated:', authenticated);
        return authenticated;
      })
      .catch(err => {
        console.error('Keycloak init failed', err);
        return false;
      });
  }

  login() {
    this.keycloak.login();
  }

  logout() {
    this.keycloak.logout();
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

//   getUsername(): string | undefined {
//     return this.keycloak.tokenParsed?.preferred_username;
//   }

  isLoggedIn(): boolean {
    return !!this.keycloak.token;
  }
}

// import { KeycloakService } from 'keycloak-angular';

// export function initializeKeycloak(keycloak: KeycloakService) {
//   return () =>
//     keycloak.init({
//       config: {
//         url: 'http://localhost:8080/auth',
//         realm: 'tani-bilet',
//         clientId: 'tani-bilet-app',
//       },
//       initOptions: {
//         onLoad: 'check-sso',
//         checkLoginIframe: false,
//       },
//       enableBearerInterceptor: true,
//       bearerPrefix: 'Bearer',
//       bearerExcludedUrls: ['/assets'],
//     });
// }
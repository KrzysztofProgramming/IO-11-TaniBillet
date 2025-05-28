import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private oauthService: OAuthService) {}

  login(): void {
    this.oauthService.initLoginFlow();
  }

  logout(): void {
    this.oauthService.logOut();
  }

  get identity(): any {
    return this.oauthService.getIdentityClaims();
  }

  get token(): string {
    return this.oauthService.getAccessToken();
  }

  get isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  get decodedToken(): any {
    try {
      return jwtDecode(this.token);
    } catch (e) {
      console.error('Invalid token', e);
      return null;
    }
  }

  get roles(): string[] {
    const decoded = this.decodedToken;
    return decoded?.resource_access?.['tani-bilet-app']?.roles || [];
  }

  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }
}

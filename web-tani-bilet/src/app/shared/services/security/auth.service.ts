import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

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
    console.log(this.oauthService.getIdentityClaims());
    return this.oauthService.getIdentityClaims();
  }

  get token(): string {
    return this.oauthService.getAccessToken();
  }

  get isLoggedIn(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  hasRole(role: string): boolean {
    const claims: any = this.oauthService.getIdentityClaims();
    const clientRoles =
      claims?.resource_access?.['tani-bilet-app']?.roles || [];
    return clientRoles.includes(role);
  }
}

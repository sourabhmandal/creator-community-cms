import { Injectable } from '@angular/core';
import Keycloak from "keycloak-js";
import {UserProfile} from "./user-profile";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;

  constructor() {}

  get keycloak() {
    if(!this._keycloak) {
      return this._keycloak = new Keycloak({
        url: "http://localhost:9090",
        realm: "supareel",
        clientId: "supareel-local"
      });
    }
    return this._keycloak;
  }

  get userProfile() {
    return this._profile;
  }

  async init(): Promise<void> {
    console.log('=== Authenticating User ===');
    const authenticated = await this.keycloak.init({
      onLoad: "login-required",
    })

    if (authenticated) {
      console.log('===== User Authenticated ===');
      this._profile = await this.keycloak.loadUserProfile() as UserProfile;
    }
  }

  login(): Promise<void> {
    return this.keycloak.login();
  }

  logout(): Promise<void> {
    return this.keycloak.logout({redirectUri: "http://localhost:4200"});
  }
}

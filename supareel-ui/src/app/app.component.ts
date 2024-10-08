import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {KeycloakService} from "./services/keycloak/keycloak.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  constructor(private keycloakService: KeycloakService) {}

  title = 'supareel-ui';
  ngOnInit(): void {
    this.keycloakService.init()
      .then(() => console.log('Keycloak Initialized'))
      .catch(err => console.error('Keycloak Init Failed', err));
  }
}

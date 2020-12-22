import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {KeycloakProfile} from 'keycloak-js';
import {KeycloakService} from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'angular-material-tab-router';
  navLinks: any[];
  activeLinkIndex = -1;
  userDetails: KeycloakProfile;
  constructor(private router: Router,private keycloakService: KeycloakService) {
    this.navLinks = [
      {
        label: 'Tab 1',
        link: './tab1',
        index: 0
      },
      {
        label: 'Tab 2',
        link: './tab2',
        index: 1
      },
      {
        label: 'Tab 3',
        link: './tab3',
        index: 2
      },
    ];
  }

  async ngOnInit() {
    if (await this.keycloakService.isLoggedIn()) {
      this.userDetails = await this.keycloakService.loadUserProfile();
    }
  }

  async doLogout() {
    await this.keycloakService.logout();
  }

}

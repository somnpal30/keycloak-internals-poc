import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {KeycloakService} from 'keycloak-angular';

@Component({
  selector: 'app-topnav',
  templateUrl: './topnav.component.html',
  styleUrls: ['./topnav.component.css']
})
export class TopnavComponent implements OnInit {

  loggedUser;

  constructor(private route: ActivatedRoute,
              private router: Router, private keycloakService: KeycloakService) { }

  ngOnInit(): void {
    console.log(this.keycloakService.getUsername());
    this.loggedUser = this.keycloakService.getUsername();
   /* this.keycloakService.loadUserProfile().then(user => {
      console.log(user);
      //this.loggedUser = user;
    });*/
  /*  this.keycloakService.isLoggedIn()
      .then(result => {
            console.log(result);
        }
      ).catch(error => {
        console.error(error);
    });*/
  }

  logout = () => {
    this.keycloakService.logout();
  }

}

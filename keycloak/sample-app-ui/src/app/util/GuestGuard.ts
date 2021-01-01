import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable()
export class GuestGuard implements CanActivate {

  constructor(private authService: KeycloakService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (! this.authService.isLoggedIn()) {
      this.router.navigate(['otp']);
      return false;
    }
    return  true;
  }

}



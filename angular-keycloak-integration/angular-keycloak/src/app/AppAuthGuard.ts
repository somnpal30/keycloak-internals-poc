import {Injectable} from '@angular/core';
import {KeycloakAuthGuard, KeycloakService} from 'keycloak-angular';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from '@angular/router';

@Injectable()
export class AppAuthGuard extends KeycloakAuthGuard {
  constructor(protected readonly router: Router, protected readonly keycloakService: KeycloakService) {
    super(router, keycloakService);
  }

  public async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean > {
    if (!this.authenticated) {
      await this.keycloakService.login({
        redirectUri: window.location.origin + state.url,
      });
    }
    // Get the roles required from the route.
    const requiredRoles = route.data.roles;

    // Allow the user to to proceed if no additional roles are required to access the route.
    if (!(requiredRoles instanceof Array) || requiredRoles.length === 0) {
      return true;
    }

    // Allow the user to proceed if all the required roles are present.
    return requiredRoles.every((role) => this.roles.includes(role));
  }


}

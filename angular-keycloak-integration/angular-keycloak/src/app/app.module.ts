import { BrowserModule } from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FlowComponent } from './component/flow/flow.component';
import { HeaderComponent } from './component/header/header.component';
import { NavComponent } from './component/nav/nav.component';
import { TopnavComponent } from './component/topnav/topnav.component';
import {KeycloakAngularModule, KeycloakService} from 'keycloak-angular';

@NgModule({
  declarations: [
    AppComponent,
    FlowComponent,
    HeaderComponent,
    NavComponent,
    TopnavComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    KeycloakAngularModule
  ],
  providers: [
    {
      provide : APP_INITIALIZER,
      useFactory : initializeKeycloak,
      multi: true,
      deps: [KeycloakService],


    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }


function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8181/auth',
        realm: 'DEMOO',
        clientId: 'UI-APP',
      },
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe : false
       /* silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html',*/
      },
      enableBearerInterceptor: true,
      loadUserProfileAtStartUp: true,
      bearerExcludedUrls: ['/assets']
    });
}

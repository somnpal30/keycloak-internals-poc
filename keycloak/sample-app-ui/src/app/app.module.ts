import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import {AppRoutingModule, routingComponent} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSliderModule} from '@angular/material/slider';
import {MatTabsModule} from '@angular/material/tabs';
import {MatToolbarModule} from '@angular/material/toolbar';
import {KeycloakAngularModule, KeycloakService} from 'keycloak-angular';
import {HttpClientModule} from '@angular/common/http';
import {initializer} from './service/initializer.service';


const keycloakService: KeycloakService = new KeycloakService();


@NgModule({
  declarations: [
    AppComponent,
    routingComponent
  ],
  imports: [
    KeycloakAngularModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSliderModule,
    MatTabsModule,
    MatToolbarModule,
    HttpClientModule,
  ],
  providers: [
   /* {
      provide: KeycloakService,
      useValue: keycloakService
    },*/
    {
      provide: APP_INITIALIZER,
      useFactory: initializer,
      deps: [KeycloakService],
      multi: true
    }/*,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor
    }*/
  ],
  bootstrap: [AppComponent]
})
export class AppModule/* implements DoBootstrap*/ {
  /*  async ngDoBootstrap(app) {
      const {keycloakConfig} = environment;

      try {
        await keycloakService.init({config: keycloakConfig});
        app.bootstrap(AppComponent);
      } catch (error) {
        console.error('Keycloak init failed', error);
      }
    }*/
}

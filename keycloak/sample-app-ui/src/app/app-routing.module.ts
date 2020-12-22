import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {Tab1Component} from './component/tab1/tab1.component';
import {Tab2Component} from './component/tab2/tab2.component';
import {Tab3Component} from './component/tab3/tab3.component';
import {TabgroupComponent} from './component/tabgroup/tabgroup.component';
import {AppAuthGuard} from './AppAuthGuard';


const routes: Routes = [
  { path: '', redirectTo: '/tab1', pathMatch: 'full' },
  { path : 'tab1' , component: Tab1Component , canActivate: [AppAuthGuard] , data: { roles: ['tab1'] } },
  { path : 'tab2' , component: Tab2Component , canActivate: [AppAuthGuard] , data: { roles: ['tab2'] }},
  { path : 'tab3' , component: Tab3Component , canActivate: [AppAuthGuard] , data: { roles: ['tab3'] }}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AppAuthGuard]
})
export class AppRoutingModule { }

export const  routingComponent = [ Tab1Component, Tab2Component, Tab3Component , TabgroupComponent ];

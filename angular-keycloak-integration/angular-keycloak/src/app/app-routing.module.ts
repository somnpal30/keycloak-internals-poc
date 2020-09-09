import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlowComponent} from './component/flow/flow.component';
import {AppAuthGuard} from './AppAuthGuard';

const routes: Routes = [
  {
    path: '', redirectTo: '/flow', pathMatch: 'full'
  },
  {
    path: 'flow', component: FlowComponent, canActivate: [AppAuthGuard], data: {roles: ['UI-APP-ROLE', 'manage-account']}
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AppAuthGuard]
})
export class AppRoutingModule {
}

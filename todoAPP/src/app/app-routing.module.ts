import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShowAllComponent } from './components/show-all/show-all.component';
import { UpdateComponent } from './components/update/update.component';
import { UserSubscriptionComponent } from './components/user-subscription/user-subscription.component';
import { authenticationGuard } from './auth/authentication.guard';

const routes: Routes = [
  { path: 'todo', component: ShowAllComponent, canActivate: [authenticationGuard]},
  { path: 'todo/update/:id', component: UpdateComponent, canActivate: [authenticationGuard]},
  { path: '', component: UserSubscriptionComponent},
  { path: '**', redirectTo: 'todo', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MainComponent } from './components/main/main.component';
import { AlertComponent } from './components/alert/alert.component';
import { HeaderComponent } from './components/header/header.component';
import { CategoryListComponent } from './components/categorylist/category-list.component'
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AddUpdateComponent } from './components/addupdate/addupdate.component';

import { AuthGuard } from './guards/auth.guard';

import { CategoryFilterPipe } from './pipes/categoryfilter.pipe';

import { AlertService } from './services/alert.service';
import { UserService } from './services/user.service';
import { MapsService } from './services/maps.service';
import { ChoService } from './services/cho.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot([
      {
        path: 'categories',
        component: CategoryListComponent
      },
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'register',
        component: RegisterComponent
      },
      {
        path: "addupdate",
        component: AddUpdateComponent,
        canActivate: [AuthGuard]
      }
    ])
  ],
  declarations: [MainComponent, AlertComponent, CategoryListComponent, LoginComponent, HeaderComponent, RegisterComponent, AddUpdateComponent, CategoryFilterPipe],
  bootstrap: [MainComponent],
  providers: [AlertService, UserService, AuthGuard, MapsService, ChoService]
})
export class HerodotAppModule { }


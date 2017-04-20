import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpModule } from '@angular/http';

import { MainComponent } from './components/main/main.component';
import { AlertComponent } from './components/alert/alert.component';
import { HeaderComponent } from './components/header/header.component';
import { CategoryListComponent } from './components/categorylist/category-list.component'
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { AnnotationComponent } from './components/annotation/annotation.component';

import { AlertService } from './services/alert.service';
import { UserService } from './services/user.service';
import { AnnotationService } from './services/annotation.service';
import { AnnotationResolverService } from './services/annotation.resolver.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
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
        path: 'home',
        component: HomeComponent,
        canActivate: [
          UserService
        ],
        resolve: {
          annotations: AnnotationResolverService
        }
      }
    ])
  ],
  declarations: [
    MainComponent,
    AlertComponent,
    CategoryListComponent,
    LoginComponent,
    HeaderComponent,
    RegisterComponent,
    HomeComponent,
    AnnotationComponent],
  bootstrap: [MainComponent],
  providers: [
    AlertService,
    UserService,
    AnnotationService,
    AnnotationResolverService
  ]
})
export class HerodotAppModule { }


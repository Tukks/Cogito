import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BoardComponent} from "./page/board/board.component";
import {HandleShareComponent} from "./page/handle-share/handle-share.component";
import {LoginComponent} from "./page/login/login.component";
import {RedirectOAuth2Component} from "./page/redirect-oauth2/redirect-oauth2.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'board',
    component: BoardComponent,
    // loadChildren: () => import('./folder/folder.module').then(m => m.FolderPageModule)
  },
  {
    path: 'handle-share',
    component: HandleShareComponent,
    // loadChildren: () => import('./folder/folder.module').then(m => m.FolderPageModule)
  },
  {
    path: 'oauth2/authorization/google',
    component: RedirectOAuth2Component,
    // loadChildren: () => import('./folder/folder.module').then(m => m.FolderPageModule)
  },
  {
    path: 'login',
    component: LoginComponent,
    // loadChildren: () => import('./folder/folder.module').then(m => m.FolderPageModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

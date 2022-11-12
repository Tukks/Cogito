import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BoardComponent} from "./page/board/board.component";
import {HandleShareComponent} from "./page/handle-share/handle-share.component";
import {LoginComponent} from "./page/login/login.component";
import {AuthGuard} from "./guards/auth-guard.guard";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'board',
    component: BoardComponent,
    canActivate: [AuthGuard]
    // loadChildren: () => import('./folder/folder.module').then(m => m.FolderPageModule)
  },
  {
    path: 'handle-share',
    component: HandleShareComponent,
    canActivate: [AuthGuard]
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

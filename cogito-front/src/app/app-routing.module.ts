import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BoardComponent} from "./page/board/board.component";
import {HandleShareComponent} from "./page/handle-share/handle-share.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'board',
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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { BoardComponent } from "./page/board/board.component";
import { HandleShareComponent } from "./page/handle-share/handle-share.component";
import { LoginComponent } from "./page/login/login.component";
import { RegisterComponent } from "./page/register/register.component";
import { RoutableEditorComponent } from "./components/routable-editor/routable-editor.component";
import { HomeComponent } from "./page/home/home.component";

const routes: Routes = [
  {
    path: "login",
    component: LoginComponent,
    pathMatch: "full"
  },
  {
    path: "register",
    component: RegisterComponent,
    pathMatch: "full"
  },
  {
    path: "",
    component: HomeComponent,
    pathMatch: "prefix",
    children: [
      {
        path: "",
        component: BoardComponent
      },
      {
        path: "handle-share",
        component: HandleShareComponent

      },
      {
        path: ":id",
        component: RoutableEditorComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

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
    path: "",
    component: HomeComponent,
    pathMatch: "full",
    children: [
      {
        path: "",
        component: BoardComponent,
        pathMatch: "full"
      },
      {
        path: "handle-share",
        component: HandleShareComponent,
        pathMatch: "full"
      },
      {
        path: ":id",
        component: RoutableEditorComponent,
        pathMatch: "full"
      }
    ]
  },

  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "register",
    component: RegisterComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

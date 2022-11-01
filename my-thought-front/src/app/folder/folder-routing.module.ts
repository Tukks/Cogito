import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {FolderPage} from './folder.page';
import {TwitterCardComponent} from "../cards/twitter-card/twitter-card.component";
import {MarkdownCardComponent} from "../cards/markdown-card/markdown-card.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

const routes: Routes = [
  {
    path: '',
    component: FolderPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes), ReactiveFormsModule, FormsModule],
  exports: [RouterModule, TwitterCardComponent, MarkdownCardComponent],
  declarations: [
    TwitterCardComponent,
    MarkdownCardComponent
  ]
})
export class FolderPageRoutingModule {
}

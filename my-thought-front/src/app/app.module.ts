import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BoardComponent} from './board/board.component';
import {CardComponent} from './card/card.component';
import {ScrollingModule} from "@angular/cdk/scrolling";
import {HttpClientModule} from "@angular/common/http";
import {MarkdownCardComponent} from "./editors/markdown-card/markdown-card.component";
import {FormsModule} from "@angular/forms";
import {ModalEditorComponent} from './editors/modal-editor/modal-editor.component';
import {DialogModule} from "@angular/cdk/dialog";

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    CardComponent,
    MarkdownCardComponent,
    ModalEditorComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ScrollingModule,
    HttpClientModule,
    FormsModule,
    DialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}

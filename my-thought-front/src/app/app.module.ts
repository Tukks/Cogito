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

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    CardComponent,
    MarkdownCardComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ScrollingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}

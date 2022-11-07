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
import {fr_FR, NZ_I18N} from 'ng-zorro-antd/i18n';
import {registerLocaleData} from '@angular/common';
import fr from '@angular/common/locales/fr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NzInputModule} from "ng-zorro-antd/input";
import {NzButtonModule} from "ng-zorro-antd/button";
import {CustomTagsInput} from "./components/custom-tags-input";
import {NzTagModule} from "ng-zorro-antd/tag";
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzNoAnimationModule} from "ng-zorro-antd/core/no-animation";
import {NzTypographyModule} from "ng-zorro-antd/typography";
import {NzFormModule} from "ng-zorro-antd/form";

registerLocaleData(fr);

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    CardComponent,
    MarkdownCardComponent,
    ModalEditorComponent,
    CustomTagsInput
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ScrollingModule,
    HttpClientModule,
    FormsModule,
    DialogModule,
    BrowserAnimationsModule,
    NzInputModule,
    NzButtonModule,
    NzTagModule,
    NzIconModule,
    NzNoAnimationModule,
    NzTypographyModule,
    NzFormModule
  ],
  providers: [
    {provide: NZ_I18N, useValue: fr_FR}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

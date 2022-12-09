import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { BoardComponent } from "./page/board/board.component";
import { CardComponent } from "./components/card/card.component";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { MarkdownCardComponent } from "./components/markdown-card/markdown-card.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ModalEditorComponent } from "./components/modal-editor/modal-editor.component";
import { DialogModule } from "@angular/cdk/dialog";
import { fr_FR, NZ_I18N } from "ng-zorro-antd/i18n";
import { registerLocaleData } from "@angular/common";
import fr from "@angular/common/locales/fr";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NzInputModule } from "ng-zorro-antd/input";
import { NzButtonModule } from "ng-zorro-antd/button";
import { CustomTagsInput } from "./components/custom-tags-input";
import { NzTagModule } from "ng-zorro-antd/tag";
import { NzIconModule } from "ng-zorro-antd/icon";
import { NzNoAnimationModule } from "ng-zorro-antd/core/no-animation";
import { NzTypographyModule } from "ng-zorro-antd/typography";
import { NzFormModule } from "ng-zorro-antd/form";
import { LayoutModule } from "@angular/cdk/layout";
import { ServiceWorkerModule } from "@angular/service-worker";
import { environment } from "../environments/environment";
import { HandleShareComponent } from "./page/handle-share/handle-share.component";
import { HttpErrorInterceptor } from "./http-error.interceptor";
import { NzMessageModule } from "ng-zorro-antd/message";
import { IntersectionObserverElementDirective } from "./internal-service/intersection-observser/intersection-observer-element.directive";
import { IntersectionObserverGridDirective } from "./internal-service/intersection-observser/intersection-observer-grid.directive";
import { LoginComponent } from "./page/login/login.component";
import { NzDropDownModule } from "ng-zorro-antd/dropdown";
import { ClipboardModule } from "@angular/cdk/clipboard";
import { RegisterComponent } from "./page/register/register.component";
import { NzAffixModule } from "ng-zorro-antd/affix";
import { NzRadioModule } from "ng-zorro-antd/radio";

registerLocaleData(fr);

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    CardComponent,
    MarkdownCardComponent,
    ModalEditorComponent,
    CustomTagsInput,
    HandleShareComponent,
    LoginComponent,
    IntersectionObserverGridDirective,
    IntersectionObserverElementDirective,
    RegisterComponent,
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
    NzFormModule,
    NzMessageModule,
    LayoutModule,
    ClipboardModule,
    ReactiveFormsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000',
    }),
    NzDropDownModule,
    NzAffixModule,
    NzRadioModule,
  ],
  providers: [
    { provide: NZ_I18N, useValue: fr_FR },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

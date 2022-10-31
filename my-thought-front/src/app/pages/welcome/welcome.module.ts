import { NgModule } from '@angular/core';

import { WelcomeRoutingModule } from 'src/app/pages/welcome/welcome-routing.module';

import { WelcomeComponent } from 'src/app/pages/welcome/welcome.component';
import {NzCardModule} from "ng-zorro-antd/card";
import {NgForOf} from "@angular/common";


@NgModule({
  imports: [WelcomeRoutingModule, NzCardModule, NgForOf],
  declarations: [WelcomeComponent],
  exports: [WelcomeComponent]
})
export class WelcomeModule { }

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {IonicModule} from '@ionic/angular';

import {FolderPageRoutingModule} from './folder-routing.module';

import {FolderPage} from './folder.page';
import {ScrollingModule} from "@angular/cdk/scrolling";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    FolderPageRoutingModule,
    ReactiveFormsModule,
    ScrollingModule,
  ],
  declarations: [FolderPage],
})
export class FolderPageModule {
}

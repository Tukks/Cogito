import {Inject, Injectable} from '@angular/core';
import {Options} from "@angular/cli/src/command-builder/command-module";
import {EventManager} from "@angular/platform-browser";
import {DOCUMENT} from "@angular/common";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HotkeysService {

  defaults: Partial<Options<any>> = {
    element: this.document
  }

  constructor(private eventManager: EventManager,
              @Inject(DOCUMENT) private document: Document) {
  }

  addShortcut(options: Partial<Options<any>>) {
    const merged = {...this.defaults, ...options};
    const event = `keydown.${merged['keys']}`;

    return new Observable(observer => {
      const handler = (e: Event) => {
        e.preventDefault()
        observer.next(e);
      };

      const dispose = this.eventManager.addEventListener(
        merged['element'], event, handler
      );

      return () => {
        dispose();
      };
    })
  }
}

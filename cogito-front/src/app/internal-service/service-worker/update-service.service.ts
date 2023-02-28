import { Injectable } from "@angular/core";
import { SwUpdate } from "@angular/service-worker";
import { filter } from "rxjs";

@Injectable({
  providedIn: "root"
})
export class AppUpdateService {
  constructor(private readonly updates: SwUpdate) {

    this.updates.versionUpdates.pipe(filter(value => value.type === "VERSION_READY"))
      .subscribe(() => {
        this.updates.activateUpdate().then(() => document.location.reload());
      });
  }

}

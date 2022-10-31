import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class StoreService {

  constructor() { }

  public getState(): any{
    JSON.parse(localStorage.getItem("store")!);
  }

  public save(state: any) {
    localStorage.setItem("store", JSON.stringify(state));
  }
}

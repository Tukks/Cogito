import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { CogitoStoreService } from "../store/cogito-store.service";
import { WebSocketService } from "../../http-service/web-socket.service";
import { LoginService } from "../../http-service/login.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router,
              private cogitoStoreService: CogitoStoreService,
              private webSocketService: WebSocketService, private loginService: LoginService) {
  }



  public logout() {
    this.webSocketService.unsubscribe();
    this.loginService.logout().subscribe();
    this.cogitoStoreService.cleanStore();
    this.router.navigateByUrl(`/login`).then();
  }
}

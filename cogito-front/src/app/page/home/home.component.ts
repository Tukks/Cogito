import { Component } from "@angular/core";
import { WebSocketService } from "../../http-service/web-socket.service";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.less"]
})
export class HomeComponent {

  constructor(private webSocketService: WebSocketService) {
    this.webSocketService.connectWebsocket();

    if (localStorage.getItem("theme")) {
      document.body.classList.add(localStorage.getItem("theme")!);
    }

  }


}

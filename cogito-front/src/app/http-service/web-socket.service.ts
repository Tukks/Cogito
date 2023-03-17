import { Injectable } from "@angular/core";
import { webSocket, WebSocketSubject } from "rxjs/webSocket";
import { CardType } from "../types/cards-link";
import { retry, Subscription } from "rxjs";
import { CogitoStoreService } from "../internal-service/store/cogito-store.service";

@Injectable({
  providedIn: "root"
})
export class WebSocketService {

  private ws: WebSocketSubject<{ actionType: "DELETE" | "ADD" | "EDIT"; ids: string[]; cards: CardType[]; }> | undefined;
  private ws_subscription: Subscription | undefined;

  constructor(private cogitoStoreService: CogitoStoreService) {
  }


  public connectWebsocket() {
    if (this.ws) {
      console.log("reconnecting websocket");
      if (!this.ws.closed) {
        this.ws.unsubscribe();
        this.ws_subscription?.unsubscribe();
      }
      this.ws = this.createWebSocket();
    }
    if (this.ws === undefined) {
      console.log("creating websocket");
      this.ws = this.createWebSocket();
    }
    if (!this.ws.closed) {

      this.ws_subscription = this.ws.pipe(
        retry({ count: 99, delay: 1000, resetOnSuccess: true })
      ).subscribe({
        next: value => {
          this.cogitoStoreService.websocketStatus(true, null);
          if (value.actionType === "DELETE") {
            this.cogitoStoreService.removeCards(value.ids);
          } else if (value.actionType === "ADD") {
            this.cogitoStoreService.addCards(value.cards);
          } else if (value.actionType === "EDIT") {
            this.cogitoStoreService.editCard(value.cards);
          }
        },
        error: err => {
          this.cogitoStoreService.websocketStatus(false, err);
        },
        complete: () => {
          this.cogitoStoreService.websocketStatus(false, null);
        }
      });

    }
  }

  public unsubscribe() {
    this.ws_subscription?.unsubscribe();
    this.ws?.unsubscribe();
  }

  private createWebSocket(): WebSocketSubject<{ actionType: "DELETE" | "ADD" | "EDIT"; ids: string[]; cards: CardType[]; }> {
    return webSocket<{
      actionType: "DELETE" | "ADD" | "EDIT",
      ids: string[],
      cards: CardType[]
    }>({
        url: ((window.location.protocol === "https:") ? "wss://" : "ws://") + window.location.host + "/ws/cards",
        openObserver: {
          next: (val) => {
            this.cogitoStoreService.websocketStatus(true, null);
          }
        },
        closeObserver: {
          next: (val) => {
            this.cogitoStoreService.websocketStatus(false, val);
          }
        }
      }
    );

  }

}

import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { lastValueFrom, Observable, retry, Subscription, tap } from "rxjs";
import { CardType, Tag } from "../types/cards-link";
import { CogitoStoreService } from "../internal-service/store/cogito-store.service";
import { map } from "rxjs/operators";
import { webSocket, WebSocketSubject } from "rxjs/webSocket";

@Injectable({
  providedIn: "root"
})
export class ThoughtsService {
  private ws: WebSocketSubject<{ actionType: "DELETE" | "ADD" | "EDIT"; id: string; card: CardType; }> | undefined;
  private ws_subscription: Subscription | undefined;
  constructor(
    private httpClient: HttpClient,
    private cogitoStoreService: CogitoStoreService) {

  }


  public getAllthougts(): Observable<CardType[]> {
    return this.httpClient.get<CardType[]>("/api/thoughts").pipe(
      tap((values) => {
        this.cogitoStoreService.addCards(values);
      })
    );
  }

  public getThought(id: string): Observable<CardType> {
    return this.httpClient.get<CardType>("/api/thoughts/" + id);
  }

  public getTagsStartWith(val: string): Observable<string[]> {
    return this.httpClient.get<string[]>("/api/tags?startWith=" + val);
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
            this.cogitoStoreService.removeCard(value.id);
          } else if (value.actionType === "ADD") {
            this.cogitoStoreService.addCard(value.card);
          } else if (value.actionType === "EDIT") {
            this.cogitoStoreService.editCard(value.card);
          }
        },
        error: err => {
          this.cogitoStoreService.websocketStatus(false, err);
        }, // Called if at any point WebSocket API signals some kind of error.
        complete: () => {
          this.cogitoStoreService.websocketStatus(false, null);
        }// Called when connection is closed (for whatever reason).
      });

    }
  }

  public createWebSocket(): WebSocketSubject<{ actionType: "DELETE" | "ADD" | "EDIT"; id: string; card: CardType }> {
    return webSocket<{
      actionType: "DELETE" | "ADD" | "EDIT",
      id: string,
      card: CardType
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
            this.connectWebsocket();

          }
        }
      }
    );

  }

  public save(thingRequest: {
    note?: string;
    tags?: Tag[];
  }): Observable<CardType> {
    return this.httpClient.post<CardType>("/api/save", thingRequest);
  }

  public delete(id: string): Observable<number> {
    return this.httpClient.delete<number>(`/api/${id}`);
  }

  public editThing(
    id: string,
    thingRequest: {
      title?: string;
      note?: string;
      tags?: Tag[];
      comment?: string;
    }
  ): Observable<CardType> {
    return this.httpClient.patch<CardType>(`/api/${id}`, {
      title: thingRequest.title,
      note: thingRequest.note,
      tags: thingRequest.tags?.map(value => {
        return { tag: value.tag, hidden: value.hidden };
      }),
      comment: thingRequest.comment
    });
  }

  public uploadImage(data: FormData): Promise<any> {
    return lastValueFrom(
      this.httpClient.post("/api/image", data)
        .pipe(map(value => `/api/image/${value}`)));
  }

}

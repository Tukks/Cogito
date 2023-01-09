import { Injectable, NgZone } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { lastValueFrom, Observable, retry, tap } from "rxjs";
import { CardType, Tag } from "../types/cards-link";
import { CogitoStoreService } from "../internal-service/store/cogito-store.service";
import { map } from "rxjs/operators";
import { webSocket } from "rxjs/webSocket";

@Injectable({
  providedIn: "root"
})
export class ThoughtsService {
  constructor(
    private httpClient: HttpClient,
    private cogitoStoreService: CogitoStoreService,
    private zone: NgZone) {

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

  public connectWebSocket(): void {
    const ws = webSocket<{
      actionType: "DELETE" | "ADD" | "EDIT",
      id: string,
      card: CardType
    }>(((window.location.protocol === "https:") ? "wss://" : "ws://") + window.location.host + "/ws/cards").pipe(
      retry({ count: 4, delay: 4000 })
    ).subscribe({
      next: value => {
        if (value.actionType === "DELETE") {
          this.cogitoStoreService.removeCard(value.id);
        } else if (value.actionType === "ADD") {
          this.cogitoStoreService.addCard(value.card);
        } else if (value.actionType === "EDIT") {
          this.cogitoStoreService.editCard(value.card);
        }
      },
      error: err => console.log(err), // Called if at any point WebSocket API signals some kind of error.
      complete: () => console.log("complete") // Called when connection is closed (for whatever reason).
    });

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

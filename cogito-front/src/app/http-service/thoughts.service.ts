import { Injectable, NgZone } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { lastValueFrom, Observable, tap } from "rxjs";
import { CardType, Tag } from "../types/cards-link";
import { CogitoStoreService } from "../internal-service/store/cogito-store.service";
import { map } from "rxjs/operators";

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

  public testSSE(): Observable<any> {
    return new Observable(observer => {
      const test = new EventSource("/api/thought-event");
      test.onmessage = ev => {
        this.zone.run(() => {
          console.log(ev);
          observer.next(JSON.parse(ev.data));
        });
      };

      test.onerror = ev => {
        this.zone.run(() => {
          console.log("ERROR" + ev);
        });
      };
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
    return this.httpClient.patch<CardType>(`/api/${id}`, thingRequest);
  }

  public uploadImage(data: FormData): Promise<any> {
    return lastValueFrom(
      this.httpClient.post("/api/image", data)
        .pipe(map(value => `/api/image/${value}`)));
  }

}

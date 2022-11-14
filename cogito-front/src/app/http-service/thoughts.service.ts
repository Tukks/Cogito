import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, Subject, tap} from "rxjs";
import {CardType, Tag} from "../types/cards-link";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ThoughtsService {
  public subject = new Subject<CardType[]>();

  constructor(private httpClient: HttpClient) {
  }

  public getAllthougts(): Observable<CardType[]> {
    return this.httpClient.get<CardType[]>("/api/thoughts", {withCredentials: true}).pipe(map(values => {
      this.subject.next(values);
      return values;
    }));
  }

  public save(note: string): Observable<any> {
    return this.httpClient.post("/api/save", note).pipe(tap(() => {
      this.getAllthougts().subscribe(value => this.subject.next(value));
    }))
  }

  public delete(id: string): Observable<any> {
    return this.httpClient.delete(`/api/${id}`).pipe(tap(() => {
      this.getAllthougts().subscribe(value => this.subject.next(value));
    }))
  }

  public editThing(id: string, thingRequest: { title?: string, note?: string, tags?: Tag[], comment?: string }): Observable<any> {
    return this.httpClient.patch(`/api/${id}`, thingRequest).pipe(tap(() => {
      this.getAllthougts().subscribe(value => this.subject.next(value));
    }))
  }

}

import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, Subject, tap} from "rxjs";
import {CardsLink} from "../types/cards-link";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ThoughtsService {
  public subject = new Subject<CardsLink[]>();

  constructor(private httpClient: HttpClient) {
  }

  public getAllthougts(): Observable<CardsLink[]> {
    return this.httpClient.get<CardsLink[]>("/api/thoughts").pipe(map(values => {
      this.subject.next(values);
      return values;
    }));
  }

  public save(note: string): Observable<any> {
    return this.httpClient.post("/api/save", note).pipe(tap(() => {
      this.getAllthougts().subscribe(value => this.subject.next(value));
    }))
  }

}

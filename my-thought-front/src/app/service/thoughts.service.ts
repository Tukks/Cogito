import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CardsLink, CardsType} from "../types/cards-link";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ThoughtsService {
  REGEX_TWITTER: RegExp = /((https?):\/\/)?(www.)?twitter\.com(\/@?(\w){1,15})\/status\/[0-9]{19}\?/;

  constructor(private httpClient: HttpClient) {
  }

  public getAllthougts(): Observable<CardsLink[]> {
    return this.httpClient.get<CardsLink[]>("/api/thoughts").pipe(map(values => {
      values.map(value => {
        if (this.REGEX_TWITTER.test(value.url) === true) {
          console.log("TWEET");
          value.type = CardsType.TWEET;
        } else if (value.url === null) {
          value.type = CardsType.MARKDOWN;
        } else {
          value.type = CardsType.LINK;
        }

      });
      return values;
    }));
  }

  public saveLink(url: string): any {
    return this.httpClient.post("/api/thoughts/link", {url})
  }

}

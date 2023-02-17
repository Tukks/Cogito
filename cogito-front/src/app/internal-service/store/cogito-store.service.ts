import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { CardType, Tag } from "../../types/cards-link";
import { map } from "rxjs/operators";

// TODO add offline support
@Injectable({
  providedIn: "root"
})
export class CogitoStoreService {
  private readonly _websocketStatus = new BehaviorSubject<boolean>(false);

  private readonly _cards = new BehaviorSubject<CardType[]>([]);
  private readonly filters = new BehaviorSubject<string>("");

  private readonly _tags = new BehaviorSubject<Tag[]>([]);

  private readonly isLoggedIn = new BehaviorSubject<boolean>(true);

  readonly isLoggedIn$ = this.isLoggedIn.asObservable();
  readonly filters$ = this.filters.asObservable();
  readonly websocketStatus$ = this._websocketStatus.asObservable();

  readonly cards$ = this._cards.asObservable().pipe(
    map((data) =>
      data.sort((a, b) => {
        if (a.created < b.created) {
          return 1;
        }
        if (a.created > b.created) {
          return -1;
        }
        return 0;
      })
    )
  );

  constructor() {
  }



  get cards(): CardType[] {
    return this._cards.getValue();
  }

  private set cards(val: CardType[]) {
    this._cards.next(val);
  }

  setLoggedIn(isLoggedInd: boolean) {
    this.isLoggedIn.next(isLoggedInd);
  }
  setFilter(val: string) {
    this.filters.next(val);
  }

  addCards(cards: CardType[]) {
    this.cards = [...this.cards, ...cards];
  }

  editCard(cardModified: CardType[]) {
    let newArray = this.cards.map((val) => {
      const elementFind = cardModified.find(value => value.id === val.id);
      if (elementFind) {
        return elementFind;
      }
      return val;
    });

    this.cards = [...newArray];
  }

  removeCards(ids: string[]) {
    this.cards = this.cards.filter((card) => !ids.includes(card.id));
  }

  websocketStatus(isConnected: boolean, err: any) {
    console.log(err);
    console.log(isConnected);
    this._websocketStatus.next(isConnected);
  }
}

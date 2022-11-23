import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {CardType} from "../../types/cards-link";
import {map} from "rxjs/operators";

// TODO add offline support
@Injectable({
  providedIn: 'root'
})
export class CogitoStoreService {
  private readonly _cards = new BehaviorSubject<CardType[]>([]);

  readonly cards$ = this._cards.asObservable().pipe(map(data => data.sort((a, b) => {
    if (a.modified < b.modified) {
      return 1;
    }
    if (a.modified > b.modified) {
      return -1;
    }
    return 0;
  })));

  constructor() {
  }

  get cards(): CardType[] {
    return this._cards.getValue();
  }

  private set cards(val: CardType[]) {
    this._cards.next(val);
  }

  addCards(cards: CardType[]) {
    this.cards = cards;
  }

  addCard(card: CardType) {
    this.cards = [
      ...this.cards,
      card
    ];
  }

  editCard(cardModified: CardType) {
    let newArray = this.cards.map((val) => {
      if (val.id === cardModified.id) {
        return cardModified;
      }
      return val;
    });

    this.cards = [
      ...newArray
    ];
  }

  removeCard(id: string) {
    this.cards = this.cards.filter(card => card.id !== id);
  }
}

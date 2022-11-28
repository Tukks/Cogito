import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ThoughtsService } from '../../http-service/thoughts.service';
import { CardType } from '../../types/cards-link';
import FlexSearch from 'flexsearch';
import { HotkeysService } from '../../internal-service/hotkeys/hotkeys.service';
import { CogitoStoreService } from '../../internal-service/store/cogito-store.service';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.less'],
})
export class BoardComponent implements OnInit {
  public originalResult: CardType[] = [];

  public filteredResult: CardType[] = [];
  public searchValue: string = '';
  public index: any;
  @ViewChild('searchInput') searchInput!: ElementRef;

  constructor(
    private thoughtsService: ThoughtsService,
    private hotkeys: HotkeysService,
    private cogitoStoreService: CogitoStoreService
  ) {}

  trackByFn(index: number, item: CardType) {
    return item.id && item.modified; // or item.id
  }

  ngOnInit(): void {
    this.hotkeys.addShortcut({ keys: 'shift.f' }).subscribe(() => {
      this.searchInput.nativeElement.blur();
      this.searchInput.nativeElement.focus();
    });
    // @ts-ignore
    this.index = new FlexSearch.Document<CardType>({
      preset: 'match',
      resolution: 1,
      tokenize: 'forward',
      language: 'fr',
      document: {
        id: 'id',
        index: ['markdown', 'title', 'url', 'desc', 'thingType', 'tags[]:tag'],
      },
      store: true,
    });
    this.thoughtsService.getAllthougts().subscribe();
    this.cogitoStoreService.cards$.subscribe((val) => {
      val.forEach((v) => {
        this.index.remove(v.id);
        this.index.add(v);
      });
      this.originalResult = val;
      this.filteredResult = val;
    });
  }

  search($event: any) {
    if ($event !== '') {
      const searched = this.index.search($event);
      let ids: string[] = [];
      searched.forEach((res: { field: string; result: string[] }) => {
        ids = ids.concat(res.result);
      });
      this.filteredResult = this.originalResult.filter((card) =>
        ids.includes(card.id)
      );
    } else {
      this.filteredResult = this.originalResult;
    }
  }
}

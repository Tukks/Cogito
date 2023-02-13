import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { ThoughtsService } from "../../http-service/thoughts.service";
import { CardType } from "../../types/cards-link";
import { HotkeysService } from "../../internal-service/hotkeys/hotkeys.service";
import { CogitoStoreService } from "../../internal-service/store/cogito-store.service";
import Document from "flexsearch/dist/module/document";

@Component({
  selector: "app-board",
  templateUrl: "./board.component.html",
  styleUrls: ["./board.component.less"]
})
export class BoardComponent implements OnInit {

  selectedElement: Set<string> = new Set<string>();
  public originalResult: CardType[] = [];

  public filteredResult: CardType[] = [];
  public searchValue: string = "";

  public tagValue: string = "";
  public index: any;

  public websocketConnect: boolean = false;
  @ViewChild("searchInput") searchInput!: ElementRef;

  constructor(
    private thoughtsService: ThoughtsService,
    private hotkeys: HotkeysService,
    private cogitoStoreService: CogitoStoreService
  ) {
  }

  trackByFn(index: number, item: CardType) {
    return item.id && item.modified;
  }

  ngOnInit(): void {
    this.cogitoStoreService.websocketStatus$.subscribe(value => this.websocketConnect = value);
    // this.thoughtsService.test();
    this.hotkeys.addShortcut({ keys: "shift.f" }).subscribe(() => {
      this.searchInput.nativeElement.blur();
      this.searchInput.nativeElement.focus();
    });

    this.index = new Document({
      preset: "match",
      resolution: 1,
      tokenize: "forward",
      language: "fr",
      document: {
        id: "id",
        index: ["markdown", "title", "url", "desc", "thingType", "tags[]:tag"]
      },
      store: true
    });
    this.thoughtsService.getAllthougts().subscribe();
    this.cogitoStoreService.filters$.subscribe((val) => {
      this.searchValue = val;
      this.search(val);
    });
    this.cogitoStoreService.cards$.subscribe((val) => {
      val.forEach((v) => {
        this.index.remove(v.id);
        this.index.add(v);
      });
      this.originalResult = val;
      this.search(this.searchValue);
    });
  }

  search($event: any) {
    if ($event !== "") {
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

  setSearchToStore($event: any) {
    this.cogitoStoreService.setFilter($event);
  }

  toggleSelected(cardId: string, $event: MouseEvent) {
    if ($event.shiftKey) {
      if (!this.selectedElement.has(cardId)) {
        $event.preventDefault();
        $event.stopPropagation();
        this.selectedElement.add(cardId);
      } else {
        this.selectedElement.delete(cardId);
      }

    }
  }

  isSelectedElement(cardId: string) {
    return this.selectedElement.has(cardId);
  }

  removeAll() {
    this.thoughtsService.batchDelete(this.selectedElement).subscribe(() => this.selectedElement.clear());
  }

  addtagsToAll() {
    this.thoughtsService.batchAddTag(this.selectedElement, this.tagValue).subscribe(() => {
      this.selectedElement.clear();
      this.tagValue = "";
    });


  }
}

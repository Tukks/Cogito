import {Component, OnInit} from '@angular/core';
import {ThoughtsService} from "../../service/thoughts.service";
import {CardsLink, CardsType} from "../../types/cards-link";
import * as FlexSearch from "flexsearch";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {
  public originalResult: CardsLink[] = [];

  public filteredResult: CardsLink[] = [];
  public cardsType: typeof CardsType = CardsType;
  public searchValue: string = "";
  public index: any;

  constructor(private thoughtsService: ThoughtsService) {
  }

  ngOnInit(): void {
    // @ts-ignore
    this.index = new FlexSearch.Document<CardsLink>({
      preset: "match",
      resolution: 1,
      tokenize: "forward",
      language: "fr",
      document: {
        id: "id",
        index: ["markdown", "title", "url", "desc", "thingType", "tags[]:tag"],
      },
      store: true
    });
    this.thoughtsService.getAllthougts().subscribe();
    this.thoughtsService.subject.subscribe((val: CardsLink[]) => {
      val.forEach(v => {
        this.index.remove(v.id);
        this.index.add(v);
      });
      this.originalResult = val;
      this.filteredResult = val;
    })
  }

  search($event: any) {
    if ($event !== "") {
      const searched = this.index.search($event);
      let ids: number[] = [];
      searched.forEach((res: { field: string, result: number[] }) => {
        ids = ids.concat(res.result);
      })
      this.filteredResult = this.originalResult.filter(card => ids.includes(card.id));

    } else {
      this.filteredResult = this.originalResult;
    }


  }
}

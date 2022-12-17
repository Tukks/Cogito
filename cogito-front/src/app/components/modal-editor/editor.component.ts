import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { CardsType, CardType, Tag } from "../../types/cards-link";
import { ThoughtsService } from "../../http-service/thoughts.service";
import { MarkdownCardComponent } from "../markdown-card/markdown-card.component";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.less'],
})
export class EditorComponent implements OnInit {
  @ViewChild(MarkdownCardComponent)
  markdownCardComponent!: MarkdownCardComponent;

  public cardsType: typeof CardsType = CardsType;

  title: string | undefined;
  comment: string | undefined;
  tags: string[] = [];

  isSaving: boolean = false;

  @Input()
  public card!: CardType;

  @Output()
  public onClose = new EventEmitter<any>;
  constructor(
    public thoughtService: ThoughtsService
  ) {}

  ngOnInit(): void {
    this.title = this.card.title;
    this.comment = this.card.comment;
    this.tags = this.card.tags
      ?.filter((tag) => !tag.hidden)
      .map((tag) => tag.tag);
  }


  updateAndClose(): void {
    this.update();
    this.close();
  }

  private update() {
    this.isSaving = true;
    let customTag: Tag[] = this.tags.map((tag) => {
      return { tag, hidden: false };
    });
    customTag.push(...this.card.tags?.filter((tag) => tag.hidden));
    if (this.card.thingType === this.cardsType.MARKDOWN) {
      this.thoughtService
        .editThing(this.card.id, {
          note: this.markdownCardComponent.stackEditor?.content!,
          comment: this.comment,
          title: this.title,
          tags: customTag
        })
        .subscribe(() => this.isSaving = false);
    } else {
      this.thoughtService
        .editThing(this.card.id, {
          comment: this.comment,
          title: this.title,
          tags: customTag
        })
        .subscribe(() => this.isSaving = false);
    }
  }

  delete(id: string): void {
    this.thoughtService.delete(id).subscribe();
    this.close();
  }

  close(): void {
    this.onClose.emit();
  }

  saveAuto($event: boolean) {
    if ($event) {
      this.update();
    }
  }
}

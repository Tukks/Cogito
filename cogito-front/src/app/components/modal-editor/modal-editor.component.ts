import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {CardsType, CardType, Tag} from "../../types/cards-link";
import {DIALOG_DATA, DialogRef} from "@angular/cdk/dialog";
import {ThoughtsService} from "../../service/thoughts.service";
import {MarkdownCardComponent} from "../markdown-card/markdown-card.component";

@Component({
  selector: 'app-modal-editor',
  templateUrl: './modal-editor.component.html',
  styleUrls: ['./modal-editor.component.less']
})
export class ModalEditorComponent implements OnInit {
  @ViewChild(MarkdownCardComponent) markdownCardComponent!: MarkdownCardComponent;

  public cardsType: typeof CardsType = CardsType;

  title: string | undefined;
  comment: string | undefined;
  tags: string[] = [];

  constructor(@Inject(DIALOG_DATA) public data: { card: CardType },
              public dialogRef: DialogRef<string>, public thoughtService: ThoughtsService) {
  }

  ngOnInit(): void {
    this.title = this.data.card.title;
    this.comment = this.data.card.comment;
    this.tags = this.data.card.tags.map(tag => tag.tag);
  }

  update(): void {
    let customTag: Tag[] = this.tags.map(tag => {
      return {tag}
    });

    if (this.data.card.thingType === this.cardsType.MARKDOWN) {
      this.thoughtService.editThing(this.data.card.id, {
        note: this.markdownCardComponent.stackEditor?.content!,
        comment: this.comment,
        title: this.title,
        tags: customTag
      }).subscribe();
      this.close();
    } else {
      this.thoughtService.editThing(this.data.card.id, {
          comment: this.comment,
          title: this.title,
          tags: customTag
        }
      ).subscribe();
      this.close();
    }
  }

  delete(id: number): void {
    this.thoughtService.delete(id).subscribe();
    this.dialogRef.close();
  }

  close(): void {
    this.dialogRef.close()
  }
}

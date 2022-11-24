import {Component, HostListener, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CardsType, CardType, Tag} from "../../types/cards-link";
import {DIALOG_DATA, DialogRef} from "@angular/cdk/dialog";
import {ThoughtsService} from "../../http-service/thoughts.service";
import {MarkdownCardComponent} from "../markdown-card/markdown-card.component";

@Component({
  selector: 'app-modal-editor',
  templateUrl: './modal-editor.component.html',
  styleUrls: ['./modal-editor.component.less']
})
export class ModalEditorComponent implements OnInit, OnDestroy {
  @ViewChild(MarkdownCardComponent) markdownCardComponent!: MarkdownCardComponent;

  public cardsType: typeof CardsType = CardsType;

  title: string | undefined;
  comment: string | undefined;
  tags: string[] = [];

  @HostListener('window:popstate', ['$event'])
  dismissModal() {
    this.dialogRef.close();
  }

  constructor(@Inject(DIALOG_DATA) public data: { card: CardType },
              public dialogRef: DialogRef<string>, public thoughtService: ThoughtsService) {
  }


  ngOnInit(): void {
    this.title = this.data.card.title;
    this.comment = this.data.card.comment;
    this.tags = this.data.card.tags.filter(tag => !tag.hidden).map(tag => tag.tag);
    const modalState = {
      modal: true,
      desc: 'fake state for our modal'
    };
    history.pushState(modalState, "");
  }

  ngOnDestroy() {
    if (window.history.state.modal) {
      history.back();
    }
  }

  update(): void {
    let customTag: Tag[] = this.tags.map(tag => {
      return {tag, hidden: false}
    });
    customTag.push(...this.data.card.tags.filter(tag => tag.hidden));
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

  delete(id: string): void {
    this.thoughtService.delete(id).subscribe();
    this.dialogRef.close();
  }

  close(): void {
    this.dialogRef.close()
  }
}

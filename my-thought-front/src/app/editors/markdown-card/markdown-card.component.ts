import {AfterViewInit, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {EditorType, StacksEditor} from "@stackoverflow/stacks-editor";
import "@stackoverflow/stacks";
import {ThoughtsService} from "../../../service/thoughts.service";

@Component({
  selector: 'app-markdown-card',
  templateUrl: './markdown-card.component.html',
  styleUrls: ['./markdown-card.component.scss'],
})
export class MarkdownCardComponent implements AfterViewInit {

  @Input()
  readonly: boolean = false;

  @Input()
  content: string = "";

  @Input()
  cardMode: boolean = true;

  stackEditor: StacksEditor | undefined;

  @ViewChild('editorContainer') editorContainer!: ElementRef;

  /**
   * TODO image uploader
   */
  constructor(private service: ThoughtsService) {
  }

  ngAfterViewInit() {
    console.log(this.editorContainer)
    this.stackEditor = new StacksEditor(
      // @ts-ignore
      this.editorContainer?.nativeElement!,
      this.content, {placeholderText: 'enter your markdown here or a link', classList: ['md-size'], defaultView: EditorType.RichText}
    );

    if (this.readonly) {
      this.stackEditor.disable();
    }
  }

  save() {
    this.service.save(this.stackEditor?.content!).subscribe();
  }
}

import {AfterViewInit, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {EditorType, StacksEditor} from "@stackoverflow/stacks-editor";
import "@stackoverflow/stacks";
import {ThoughtsService} from "../../http-service/thoughts.service";
import {HotkeysService} from "../../internal-service/hotkeys/hotkeys.service";

// TODO replace with tiptap editor https://tiptap.dev
@Component({
  selector: 'app-markdown-card',
  templateUrl: './markdown-card.component.html',
  styleUrls: ['./markdown-card.component.scss'],
})
export class MarkdownCardComponent implements AfterViewInit {

  @Input()
  id: string | undefined;

  @Input()
  readonly: boolean = true;

  @Input()
  content: string = "";

  @Input()
  cardMode: boolean = true;

  public stackEditor: StacksEditor | undefined;

  @ViewChild('editorContainer') editorContainer!: ElementRef;

  /**
   * TODO image uploader
   */
  constructor(private service: ThoughtsService, private hotkeys: HotkeysService) {
  }


  ngAfterViewInit() {
    this.stackEditor = new StacksEditor(
      // @ts-ignore
      this.editorContainer?.nativeElement!,
      this.content, {placeholderText: 'enter your markdown here or a link', classList: ['md-size'], defaultView: EditorType.RichText}
    );
    if (this.readonly) {
      this.stackEditor.disable();
    }
    this.hotkeys.addShortcut({keys: 'shift.n'}).subscribe(() => {
      this.stackEditor?.focus();
    });
  }

  create() {
    this.service.save(this.stackEditor?.content!).subscribe();
    this.stackEditor!.content = "";
  }
}

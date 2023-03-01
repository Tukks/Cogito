import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { EditorType, StacksEditor } from "@stackoverflow/stacks-editor";
import "@stackoverflow/stacks";
import { ThoughtsService } from "../../http-service/thoughts.service";
import { HotkeysService } from "../../internal-service/hotkeys/hotkeys.service";
import { finalize } from "rxjs";

// TODO replace with tiptap editor https://tiptap.dev
@Component({
  selector: "app-markdown-card",
  templateUrl: "./markdown-card.component.html",
  styleUrls: ["./markdown-card.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MarkdownCardComponent implements AfterViewInit {

  public isSaving = false;

  @Input()
  id: string | undefined;

  @Input()
  isTodo: boolean = false;

  @Input()
  readonly: boolean = true;

  @Input()
  content: string = "";

  @Input()
  cardMode: boolean = true;

  @Output()
  needSave: EventEmitter<boolean> = new EventEmitter<boolean>();

  @Output()
  markAsDone: EventEmitter<MouseEvent> = new EventEmitter<MouseEvent>();
  private timeout?: number;
  public stackEditor: StacksEditor | undefined;

  @ViewChild("editorContainer") editorContainer!: ElementRef;

  constructor(
    private service: ThoughtsService,
    private hotkeys: HotkeysService,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngAfterViewInit() {
    this.stackEditor = new StacksEditor(
      // @ts-ignore
      this.editorContainer?.nativeElement!,
      this.content,
      {
        placeholderText: "enter your markdown here or a link",
        classList: ["md-size"],
        defaultView: EditorType.RichText,
        imageUpload: {
          allowExternalUrls: false,
          wrapImagesInLinks: false,
          embedImagesAsLinks: false,
          handler: (file: File | string): Promise<any> => {
            const data: FormData = new FormData();
            data.append("image", file);
            return this.service.uploadImage(data);

          }
        }
      }
    );
    if (this.readonly) {
      this.stackEditor.disable();
    }
    this.hotkeys.addShortcut({ keys: "shift.n" }).subscribe(() => {
      this.stackEditor?.dom.scroll();

      this.stackEditor?.focus();
    });
  }

  create() {
    this.isSaving = true;
    this.service.save({ note: this.stackEditor?.content! }).pipe(finalize(() => {
      this.isSaving = false;
      this.cdr.markForCheck();
    })).subscribe();
    this.stackEditor!.content = "";
  }

  saveOnType() {
    this.needSave.emit(false);
    window.clearTimeout(this.timeout);
    this.timeout = window.setTimeout(() => this.needSave.emit(true), 300);
  }
}

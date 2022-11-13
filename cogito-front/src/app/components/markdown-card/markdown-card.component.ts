import {AfterViewInit, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {EditorType, StacksEditor} from "@stackoverflow/stacks-editor";
import "@stackoverflow/stacks";
import {ThoughtsService} from "../../service/thoughts.service";
import {ImagesService} from "../../service/images.service";

// TODO replace with tiptap editor https://tiptap.dev
@Component({
  selector: 'app-markdown-card',
  templateUrl: './markdown-card.component.html',
  styleUrls: ['./markdown-card.component.scss'],
})
export class MarkdownCardComponent implements AfterViewInit {

  @Input()
  id: number | undefined;

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
  constructor(private service: ThoughtsService, private imageService: ImagesService) {
  }

  ngAfterViewInit() {
    this.stackEditor = new StacksEditor(
      // @ts-ignore
      this.editorContainer?.nativeElement!,
      this.content, {
        placeholderText: 'enter your markdown here or a link',
        classList: ['md-size'],
        defaultView: EditorType.RichText,
        // imageUpload: {
        //   handler: (file) => {
        //     const image$ = this.imageService.saveImage(file as File)
        //       .pipe(map(val => this.imageService.buildImageUrl(val.id)));
        //     return firstValueFrom(image$);
        //   },
        //   embedImagesAsLinks: false,
        //   allowExternalUrls: true
        // }
      }
    );
    if (this.readonly) {
      this.stackEditor.disable();
    }
  }

  create() {
    this.service.save(this.stackEditor?.content!).subscribe();
    this.stackEditor!.content = "";
  }
}

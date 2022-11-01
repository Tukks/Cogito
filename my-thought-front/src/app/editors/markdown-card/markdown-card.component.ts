import {Component, Input, OnInit} from '@angular/core';
import {StacksEditor} from "@stackoverflow/stacks-editor";
import "@stackoverflow/stacks";

@Component({
  selector: 'app-markdown-card',
  templateUrl: './markdown-card.component.html',
  styleUrls: ['./markdown-card.component.scss'],
})
export class MarkdownCardComponent implements OnInit {

  @Input()
  readonly: boolean = false;

  @Input()
  content: string = "";

  stackEditor: StacksEditor;

  /**
   * TODO image uploader
   */
  constructor() {
  }

  ngOnInit() {
    this.stackEditor = new StacksEditor(
      document.querySelector("#editor-container"),
      this.content, {placeholderText: 'enter your markdown here'}
    );

    if (this.readonly) {
      this.stackEditor.disable();
    }
  }

  save() {
    console.log(this.stackEditor.content);
  }
}

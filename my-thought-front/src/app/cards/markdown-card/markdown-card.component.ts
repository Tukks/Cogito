import {Component, OnInit} from '@angular/core';
import {StacksEditor} from "@stackoverflow/stacks-editor";

@Component({
  selector: 'app-markdown-card',
  templateUrl: './markdown-card.component.html',
  styleUrls: ['./markdown-card.component.scss'],
})
export class MarkdownCardComponent implements OnInit {
  stackEditor: any;
  name: any = "coucou";

  constructor() {
  }

  ngOnInit() {

    this.stackEditor = new StacksEditor(
      document.querySelector("#editor-container"),
      "*Your* **markdown** here", {}
    );
    console.log(this.stackEditor);
  }

}

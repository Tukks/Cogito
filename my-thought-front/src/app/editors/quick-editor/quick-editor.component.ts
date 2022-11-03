import {Component, OnInit} from '@angular/core';
import {ThoughtsService} from "../../../service/thoughts.service";

@Component({
  selector: 'app-quick-editor',
  templateUrl: './quick-editor.component.html',
  styleUrls: ['./quick-editor.component.scss']
})
export class QuickEditorComponent implements OnInit {

  public note: string = '';

  constructor(private service: ThoughtsService) {
  }

  ngOnInit(): void {
  }

  save() {
    this.service.save(this.note).subscribe();
  }
}

import { Component, OnInit } from "@angular/core";
import { ThoughtsService } from "../../http-service/thoughts.service";
import { ActivatedRoute, Router } from "@angular/router";
import { CardType } from "../../types/cards-link";
import { Title } from "@angular/platform-browser";

@Component({
  selector: 'app-routable-editor',
  templateUrl: './routable-editor.component.html',
  styleUrls: ['./routable-editor.component.less']
})
export class RoutableEditorComponent implements OnInit {

  public card: CardType | undefined;
  constructor(public thoughtService: ThoughtsService,
              public activatedRoute: ActivatedRoute,
              private router: Router, private titleService: Title) { }

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];
    this.thoughtService.getThought(id).subscribe(value => {
      this.card = value;
      this.titleService.setTitle(value.title);
    });
  }

  close() {
    this.router.navigateByUrl('/board');
  }
}

import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  public appPages = [
    {title: 'thoughts', url: '/folder/thoughts', icon: 'mail'},
  ];
  public tags = ['Family', 'Friends', 'Notes', 'Work', 'Travel', 'Reminders'];

  constructor() {
  }
}

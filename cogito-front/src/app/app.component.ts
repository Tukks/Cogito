import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  darkModeMenu: string = 'Dark theme';

  constructor() {
  }

  ngOnInit(): void {
    document.body.classList.add(localStorage.getItem('theme')!);

  }

  changeTheme(): void {
    document.body.classList.toggle("dark-mode");
    if (document.body.classList.contains("dark-mode")) {
      localStorage.setItem('theme', "dark-mode");
      this.darkModeMenu = 'White Theme';
    } else {
      localStorage.setItem('theme', "");
      this.darkModeMenu = 'Dark theme';

    }
  }
}

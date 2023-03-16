import { Component } from "@angular/core";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.less"]
})
export class HomeComponent {
  // When user close tab


  constructor() {
    if (localStorage.getItem("theme")) {
      document.body.classList.add(localStorage.getItem("theme")!);
    }

  }


}

import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {IonicModule} from '@ionic/angular';

import {MarkdownCardComponent} from './markdown-card.component';

describe('MarkdownCardComponent', () => {
  let component: MarkdownCardComponent;
  let fixture: ComponentFixture<MarkdownCardComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [MarkdownCardComponent],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(MarkdownCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
    const REGEX_TWITTER: RegExp = /^https?:\/\/twitter\.com\/(?:#!\/)?(\w+)\/status(es)?\/(\d+)/;
    expect(REGEX_TWITTER.test("https://twitter.com/Foone/status/1587144406096052226")).toBeTruthy();
  });

});

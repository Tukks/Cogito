import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RedirectOAuth2Component} from './redirect-oauth2.component';

describe('RedirectOAuth2Component', () => {
  let component: RedirectOAuth2Component;
  let fixture: ComponentFixture<RedirectOAuth2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RedirectOAuth2Component]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RedirectOAuth2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

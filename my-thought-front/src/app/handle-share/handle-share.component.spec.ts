import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HandleShareComponent} from './handle-share.component';

describe('HandleShareComponent', () => {
  let component: HandleShareComponent;
  let fixture: ComponentFixture<HandleShareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HandleShareComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(HandleShareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

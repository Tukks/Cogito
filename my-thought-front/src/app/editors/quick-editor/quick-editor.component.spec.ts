import {ComponentFixture, TestBed} from '@angular/core/testing';

import {QuickEditorComponent} from './quick-editor.component';

describe('QuickEditorComponent', () => {
  let component: QuickEditorComponent;
  let fixture: ComponentFixture<QuickEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [QuickEditorComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(QuickEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

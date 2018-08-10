import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeBarComponent } from './resume-bar.component';

describe('ResumeBarComponent', () => {
  let component: ResumeBarComponent;
  let fixture: ComponentFixture<ResumeBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResumeBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResumeBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should emit click Save', () => {
    let calls = 0;
    component.returnSave.subscribe(() => calls++);
    component.onClickSave();
    expect(calls).toBe(1);
  });

  it('Should emit click Issue', () => {
    let calls = 0;
    component.returnIssue.subscribe(() => calls++);
    component.OnClickIssue();
    expect(calls).toBe(1);
  });
});

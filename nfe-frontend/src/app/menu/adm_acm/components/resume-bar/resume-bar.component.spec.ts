import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeBarComponent } from './resume-bar.component';

xdescribe('ResumeBarComponent', () => {
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
});

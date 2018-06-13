import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmAcmComponent } from './adm-acm.component';

xdescribe('AdmAcmComponent', () => {
  let component: AdmAcmComponent;
  let fixture: ComponentFixture<AdmAcmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdmAcmComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmAcmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

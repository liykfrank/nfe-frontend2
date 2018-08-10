import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterCrumbsComponent } from './filter-crumbs.component';
import { SharedModule } from '../../shared.module';

xdescribe('FilterCrumbsComponent', () => {
  let component: FilterCrumbsComponent;
  let fixture: ComponentFixture<FilterCrumbsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [  ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterCrumbsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

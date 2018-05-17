import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTabsComponent } from './show-tabs.component';
import { SharedModule } from '../../../shared/shared.module';

describe('ShowTabsComponent', () => {
  let component: ShowTabsComponent;
  let fixture: ComponentFixture<ShowTabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[SharedModule],
      declarations: [ ShowTabsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

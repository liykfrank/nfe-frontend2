import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JqxNwGridComponent } from './jqx-nw-grid.component';
import { SharedModule } from '../../shared.module';

describe('JqxNwGridComponent', () => {
  let component: JqxNwGridComponent;
  let fixture: ComponentFixture<JqxNwGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [  ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JqxNwGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

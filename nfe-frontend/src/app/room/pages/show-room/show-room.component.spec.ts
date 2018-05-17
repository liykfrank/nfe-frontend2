import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowRoomComponent } from './show-room.component';
import { SharedModule } from '../../../shared/shared.module';

xdescribe('ShowRoomComponent', () => {
  let component: ShowRoomComponent;
  let fixture: ComponentFixture<ShowRoomComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[SharedModule],
      declarations: [ ShowRoomComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ScrollPanelModule } from 'primeng/primeng';

import { CommentsComponent } from './comments.component';

describe('CommentsComponent', () => {
  let component: CommentsComponent;
  let fixture: ComponentFixture<CommentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ScrollPanelModule, FormsModule],
      declarations: [CommentsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('saveCommentWithClick 1', () => {
    let aux;
    component.saveComment.subscribe((data) => aux = data);

    component.saveCommentWithClick();
    expect(aux).toBeUndefined();
  });

  it('saveCommentWithClick 2', () => {
    let aux;
    component.saveComment.subscribe((data) => aux = data);

    component.text = 'TEST';
    component.saveCommentWithClick();
    expect(aux).toBe('TEST');
  });

  it('saveCommentWithEnter 1', () => {
    let aux;
    component.saveComment.subscribe((data) => aux = data);

    const event = {keyCode: 13};

    component.saveCommentWithEnter(event);
    expect(aux).toBeUndefined();

    event.keyCode = 1;

    component.saveCommentWithEnter(event);
    expect(aux).toBeUndefined();
  });

  it('saveCommentWithEnter 2', () => {
    let aux;
    component.saveComment.subscribe((data) => aux = data);

    const event = {keyCode: 13};

    component.text = 'TEST';
    component.saveCommentWithEnter(event);
    expect(aux).toBe('TEST');
  });

});

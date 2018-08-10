import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'bspl-resume-bar',
  templateUrl: './resume-bar.component.html',
  styleUrls: ['./resume-bar.component.scss']
})

export class ResumeBarComponent implements OnInit {

  @Input() elementsResumeBar: Object[];
  @Output() returnSave: EventEmitter<null> = new EventEmitter();
  @Output() returnIssue: EventEmitter<null> = new EventEmitter();

  constructor() { }

  ngOnInit() { }

  onClickSave() {
    this.returnSave.emit();
  }

  OnClickIssue() {
    this.returnIssue.emit();
  }

}

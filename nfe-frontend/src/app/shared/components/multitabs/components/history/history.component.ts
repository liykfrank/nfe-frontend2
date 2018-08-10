import { Component, Injector, Input, OnInit } from '@angular/core';;
import { HistoryModel } from '../../models/history.model';

@Component({
  selector: 'bspl-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent  implements OnInit {
  @Input() history: HistoryModel[] = [];
  @Input() listExcludes: string[] = [];

  checkFilter = true;

  private immutableList: HistoryModel[] = [];

  constructor() {
  }

  ngOnInit() {
    this.immutableList = this.history;
  }

  filter() {
    this.history = this.checkFilter
      ? this.immutableList
      : this.immutableList.filter(
          elem => this.listExcludes.indexOf(elem.action) == -1
        );
  }
}

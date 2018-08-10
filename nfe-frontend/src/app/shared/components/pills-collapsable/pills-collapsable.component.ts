import { Component, HostListener, Input, EventEmitter, Output } from '@angular/core';
import { GLOBALS } from '../../constants/globals';
import { Pill } from '../../models/pill.model';

@Component({
  selector: 'bspl-pills-collapsable',
  templateUrl: './pills-collapsable.component.html',
  styleUrls: ['./pills-collapsable.component.scss']
})
export class PillsCollapsableComponent {

  public collapseAll = true;

  @Input()
  pills: Pill[];
  @Input()
  idReference: string;

  @Output()
  returnRefreshPills = new EventEmitter();

  constructor() {}

  clickPill(pillClicked) {
    this.pills.map( pill => {
      if (pillClicked.id == pill.id) {
        pill.active = true;
        pill.collapsable = true;
      } else {
        pill.active = false;
      }
    });
    this.collapseAll = true;
    this.returnRefreshPills.emit(this.pills);

    const id_referencia = document.getElementById(this.idReference);
    const id_pill_clicked = document.getElementById(pillClicked.id);

    const extra = id_referencia ? id_referencia.offsetTop : 0;
    const distance = id_pill_clicked ? id_pill_clicked.offsetTop : 0;
    scrollTo(0, distance + extra - 18);
  }

  clickCollapse() {
    this.collapseAll = !this.collapseAll;
    this.pills.map(pill => pill.collapsable = this.collapseAll);
    this.returnRefreshPills.emit(this.pills);
  }

  getCollapseImgSrc() {
    let aux = false;
    this.pills.map(pill => {
      if (pill.collapsable) {
        aux = true;
      }
    });
    this.collapseAll = aux;
    return this.collapseAll ? GLOBALS.ICONS.COLLAPSE_SRC : GLOBALS.ICONS.EXPAND_SRC;
  }

}

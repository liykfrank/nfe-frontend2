import { DetailsService } from './../../services/details.service';
import { Component, OnInit, Input } from '@angular/core';
import { jqxTabsComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtabs';
import { ScreenType } from './../../../../shared/models/screen-type.enum';

@Component({
  selector: 'app-multitabs',
  templateUrl: './multitabs.component.html',
  styleUrls: ['./multitabs.component.scss']
})
export class MultitabsComponent implements OnInit {

  @Input() modelView: ScreenType;

  tab_selected: number = 1;

  badge_val_tab1 = 0;
  badge_val_tab2 = 0;
  badge_val_tab3 = 0;
  badge_val_tab4 = 0;
  badge_val_tab5 = 0;

  enable_tab1 = true;
  enable_tab2 = true;
  enable_tab3 = true;
  enable_tab4 = true;
  enable_tab5 = false;

  constructor(private _DetailsService: DetailsService) {
    this._DetailsService.getRelatedTicketDocuments().subscribe(data => {
      if (data.length > 0) {
        this.badge_val_tab2++;
      }
    });
  }

  ngOnInit() {
    this.checkModelView();
  }

  checkModelView() {
    if (this.modelView === ScreenType.CREATE) {
      this.enable_tab1 = false;
      this.enable_tab2 = true;
      this.enable_tab3 = true;
      this.enable_tab4 = true;

      this.tab_selected = 2;
    }
  }

  colorTab(index: string) {
    let color = 'blue';
    if (this.modelView === ScreenType.CREATE) {
      switch (index) {
        case 'history':
          color = this.enable_tab1 ? 'blue' : 'grey';
        break;

        case 'documents':
          color = this.enable_tab2 ? 'blue' : 'grey';
        break;

        case 'files':
          color = this.enable_tab3 ? 'blue' : 'grey';
        break;

        case 'comments':
          color = this.enable_tab4 ? 'blue' : 'grey';
        break;

        case 'chat':
          color = this.enable_tab5 ? 'blue' : 'grey';
        break;
      }
    }

    return color;
  }

  selectTab(index: number, enable_tab: boolean) {
    this.tab_selected = enable_tab ? index : this.tab_selected;
  }

  isSelected(index: number) {
    if (this.tab_selected === index && index === 2) {
      this.badge_val_tab2 = 0;
    }

    return this.tab_selected === index;
  }

}

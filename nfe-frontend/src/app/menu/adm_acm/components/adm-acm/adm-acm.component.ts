import { DetailsService } from './../../services/details.service';
import { ScreenType } from './../../../../shared/models/screen-type.enum';
import { AdmAcmService } from './../../services/adm-acm.service';
import { BasicInfoModel } from './../../models/basic-info.model';

import { Component, OnInit, HostListener, AfterViewInit } from '@angular/core';

import { DivCollapsableComponent } from './../../../../shared/components/div-collapsable/div-collapsable.component';
import { Country } from '../../models/country.model';
import { Configuration } from '../../models/configuration.model';

import { MessageService } from 'primeng/components/common/messageservice';
import { TicketDocument } from '../../models/ticket-document.model';

@Component({
  selector: 'app-adm-acm',
  templateUrl: './adm-acm.component.html',
  styleUrls: ['./adm-acm.component.scss']
})
export class AdmAcmComponent implements OnInit, AfterViewInit {

  public isAdm: boolean;
  public modelView: ScreenType;

  public collapseBasicInfo: boolean = true;
  public collapseAmount: boolean = true;
  public collapseDetail: boolean = true;

  public collapseAll: boolean = false;

  basicInfo: BasicInfoModel = new BasicInfoModel();

  resume_bar: string;
  tabs_container: string;
  link_bar: string;

  sticky_bar: number;
  sticky_tabs: number;
  sticky_links: number;
  isSticky: boolean = false;

  display: boolean = false;

  constructor(
    private _AdmAcmService: AdmAcmService,
    private _MessageService: MessageService,
    private _DetailsService: DetailsService
  ) {

    this._DetailsService.getTicket().subscribe(data => {
      this.display = data != null;
      this.isSticky = data != null ? false : this.isSticky;
    });
  }

  ngOnInit() {
    this.generateStickyIDs();
  }

  ngAfterViewInit () {
    this.sticky_bar = document.getElementById(this.resume_bar).offsetTop;
    this.sticky_tabs = document.getElementById(this.tabs_container).offsetTop;
    this.sticky_links = document.getElementById(this.link_bar).offsetTop;
    this.onWindowScroll();
  }

  generateStickyIDs() {
    const adm_id_chunk = this.isAdm ? 'ADM' : 'ACM';
    const model_view_chunk = this.modelView == ScreenType.CREATE ? 'create' : 'detail';

    this.resume_bar =         'resume-bar-' + adm_id_chunk + '-' + model_view_chunk;
    this.tabs_container =     'tabs-container-' + adm_id_chunk + '-' + model_view_chunk;
    this.link_bar = 'link-bar-' + adm_id_chunk + '-' + model_view_chunk;
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isSticky = this.display ? false : window.pageYOffset >= this.sticky_bar;
  }

  scrollTo(id: string): void {
    const elem = document.getElementById(id);
    elem.scrollIntoView(true);

    switch (id) {
      case 'basicInfo':
        this.collapseBasicInfo = true;
        break;
      case 'amount':
        this.collapseAmount = true;
        break;
      case 'detail':
        this.collapseDetail = true;
        break;
    }
  }

  clickCollapse() {
    this.collapseBasicInfo = this.collapseAll;
    this.collapseAmount = this.collapseAll;
    this.collapseDetail = this.collapseAll;
    this.collapseAll = !this.collapseAll;
  }

  hide() {
    this.isSticky = true;
    this.display = false;
  }

}

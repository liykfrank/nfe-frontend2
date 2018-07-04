import { ScreenType } from './../../../../shared/models/screen-type.enum';
import { AdmAcmService } from './../../services/adm-acm.service';
import { BasicInfoService } from './../../services/basic-info.service';
import { AmountService } from './../../services/amount.service';
import { DetailsService } from './../../services/details.service';
import { CommentsService } from './../../services/comments.service';
import { FilesService } from './../../services/files.service';

import { AcdmsService } from './../../services/resources/acdms.service';
import { AgentService } from './../../services/resources/agent.service';
import { CommentService } from './../../services/resources/comment.service';
import { CompanyService } from './../../services/resources/company.service';
import { ConfigurationService } from './../../services/resources/configuration.service';
import { CountryService } from './../../services/resources/country.service';
import { CurrencyService } from './../../services/resources/currency.service';
import { FileService } from './../../services/resources/file.service';
import { PeriodService } from './../../services/resources/period.service';
import { ReasonService } from './../../services/resources/reason.service';
import { TocaService } from './../../services/resources/toca.service';

import { BasicInfoModel } from './../../models/basic-info.model';

import { Component, OnInit, HostListener, AfterViewInit } from '@angular/core';

import { DivCollapsableComponent } from './../../../../shared/components/div-collapsable/div-collapsable.component';
import { Country } from '../../models/country.model';
import { Configuration } from '../../models/configuration.model';

import { AlertsService } from './../../../../core/services/alerts.service';
import { TicketDocument } from '../../models/ticket-document.model';

@Component({
    selector: 'app-adm-acm',
    templateUrl: './adm-acm.component.html',
    styleUrls: ['./adm-acm.component.scss'],
    providers: [
        AdmAcmService,
        DetailsService,
        BasicInfoService,
        AmountService,
        CommentsService,
        FilesService,
        AcdmsService,
        AgentService,
        CommentService,
        CompanyService,
        ConfigurationService,
        CountryService,
        CurrencyService,
        FileService,
        PeriodService,
        ReasonService,
        TocaService
   ]
})
export class AdmAcmComponent implements OnInit, AfterViewInit {

    public isAdm: boolean;
    public modelView: ScreenType;

    public collapseBasicInfo = true;
    public collapseAmount = true;
    public collapseDetail = true;

    public collapseAll = false;

    basicInfo: BasicInfoModel = new BasicInfoModel();

    resume_bar: string;
    tabs_container: string;
    link_bar: string;

    collapsable_basic_info: string;
    collapsable_amount: string;
    collapsable_details: string;

    sticky_bar: number;
    sticky_tabs: number;
    sticky_links: number;
    isSticky = false;

    display = false;

    pillSelected = '';

    constructor(
        private _AdmAcmService: AdmAcmService,
        private _DetailsService: DetailsService,
        private _AlertsService: AlertsService
    ) {

        this._DetailsService.getTicket().subscribe(data => {
            this.display = data != null;
            this.isSticky = data != null ? false : this.isSticky;
        });
    }

    ngOnInit() {
        this.generateStickyIDs();
    }

    ngAfterViewInit() {
        this.sticky_bar =   document.getElementById(this.resume_bar).offsetTop;
        this.sticky_tabs =  document.getElementById(this.tabs_container).offsetTop;
        this.sticky_links = document.getElementById(this.link_bar).offsetTop;
        this.onWindowScroll();
    }

    generateStickyIDs() {
        const adm_id_chunk = this.isAdm ? 'ADM' : 'ACM';
        const model_view_chunk = this.modelView == ScreenType.CREATE ? 'create' : 'detail';

        this.resume_bar =       'resume-bar-' + adm_id_chunk + '-' + model_view_chunk;
        this.tabs_container =   'tabs-container-' + adm_id_chunk + '-' + model_view_chunk;
        this.link_bar =         'link-bar-' + adm_id_chunk + '-' + model_view_chunk;

        this.collapsable_basic_info =   'basicInfo-' + adm_id_chunk + '-' + model_view_chunk;
        this.collapsable_amount =       'amount-' + adm_id_chunk + '-' + model_view_chunk;
        this.collapsable_details =      'details-' + adm_id_chunk + '-' + model_view_chunk;
    }

    @HostListener('window:scroll', [])
    onWindowScroll() {
        this.isSticky = this.display ? false : window.pageYOffset >= this.sticky_bar - 20;
    }

    checkPillSelected(id) {
        return this.pillSelected === id;
    }

    scrollTo(id: string) {
        const adm_id_chunk = this.isAdm ? 'ADM' : 'ACM';
        const model_view_chunk = this.modelView == ScreenType.CREATE ? 'create' : 'detail';

        const distance = document
            .getElementById(id + '-' + adm_id_chunk + '-' + model_view_chunk).offsetTop;
        const extra = 140;

        this.pillSelected = id;

        switch (id) {
            case 'basicInfo':
                this.collapseBasicInfo = true;
                scrollTo(0, 0);
                break;

            case 'amount':
                this.collapseAmount = true;
                scrollTo(0, distance - extra);
                break;

            case 'details':
                this.collapseDetail = true;
                scrollTo(0, distance);
                break;
        }
        this.collapseAll = false;
    }

    clickCollapse() {
        this.collapseBasicInfo = this.collapseAll;
        this.collapseAmount = this.collapseAll;
        this.collapseDetail = this.collapseAll;
    }

    checkAllCollapsed () {
        if (!this.collapseAmount
         && !this.collapseBasicInfo
         && !this.collapseDetail) {
            this.collapseAll = true;
        } else {
            this.collapseAll = false;
        }
        return this.collapseAll;
    }

    hide() {
        this.isSticky = true;
        this.display = false;
    }

    issueACDM() {
        this._AdmAcmService.issueACDM();
    }
}

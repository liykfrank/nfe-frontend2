import { Component, HostListener, OnInit } from '@angular/core';

import { Pill } from '../../shared/models/pill.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ROUTES } from '../../shared/constants/routes';
import { AcdmConfigurationService } from './services/adm-acm-configuration.service';

@Component({
    selector: 'bspl-adm-acm',
    templateUrl: './adm-acm.component.html',
    styleUrls: ['./adm-acm.component.scss'],
    providers: []
})

export class AdmAcmComponent {

    private acdmConfiguration;
    private elementsResumeBar: Object[];
    private pills: Pill[];

    url = null;

    private isSticky = false;

    private id_acdm = 'acdm-master-container';

    private isAdm: boolean;

    constructor(
        protected router: Router,
        private acdmConfigurationService: AcdmConfigurationService,
    ) {
        this.isAdm = router.routerState.snapshot.url ===  ROUTES.ADM_ISSUE.URL;
        this.elementsResumeBar = this._initializeResumeBar();
        this.pills = this._initializePills();
    }

    checkClassSticky() {
        return this.isSticky ? 'sticky' : '';
    }

    checkSticky() {
        return this.isSticky;
    }

    onReturnRefreshPills(pills: Pill[]) {
        this.pills = pills;
    }

    collapsablePill($event, index: number) {
        this.pills[index].collapsable = $event;
    }

    @HostListener('window:scroll', [])
    onWindowScroll() {
        this.isSticky =
            window.pageYOffset >=
            document.getElementById(this.id_acdm).offsetTop;
    }

    private _initializeResumeBar() {
        return this.elementsResumeBar = [
            { title: 'ADM_ACM.RESUME.status', value: null },
            { title: 'ADM_ACM.RESUME.bsp', value: null },
            { title: 'ADM_ACM.RESUME.agent_code', value: null },
            { title: 'ADM_ACM.RESUME.for', value: null },
            { title: 'ADM_ACM.RESUME.issue_date', value: null },
            { title: 'ADM_ACM.RESUME.amount', value: '0,000,000.00 EUR' }
        ];
    }

    private _initializePills() {
        return this.pills = [
            new Pill('REFUNDS.BASIC_INFO.title', 'REFUNDS.BASIC_INFO.title'),
            new Pill('REFUNDS.AMOUNT.title', 'REFUNDS.AMOUNT.title'),
            new Pill('REFUNDS.DETAILS.title', 'REFUNDS.DETAILS.title')
        ];
    }
}

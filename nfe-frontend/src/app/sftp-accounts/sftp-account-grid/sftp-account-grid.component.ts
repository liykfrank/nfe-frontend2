import { Component, ViewChild, Output, EventEmitter, Input, AfterViewInit, OnInit } from '@angular/core';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';
import { SftpAccount } from '../models/sftp-account';

@Component({
    selector: 'app-sftp-account-grid',
    templateUrl: './sftp-account-grid.component.html',
    styleUrls: ['./sftp-account-grid.component.scss']
})
export class SftpAccountGridComponent implements OnInit, AfterViewInit {
    @ViewChild("grid") grid: jqxGridComponent;
    @Output() select: EventEmitter<SftpAccount> = new EventEmitter();
    @Input() accounts: SftpAccount[];
    @Input() includeDates: boolean;

    source: any =
        {
            datafields: [
                { name: 'login', type: 'string' },
                { name: 'mode', type: 'string' },
                { name: 'status', type: 'string' },
                { name: 'publicKey', type: 'string' }
            ]
        };

    dataAdapter: any;

    columns: any[] =
        [
            {
                text: 'Login',
                datafield: 'login'
            },
            {
                text: 'Mode',
                datafield: 'mode'
            },
            {
                text: 'Status',
                datafield: 'status'
            },
            {
                text: 'Public Key',
                datafield: 'publicKey'
            }
        ];

    ngOnInit() {
        if (this.includeDates) {
            this.source.datafields.push({
                name: 'creationTime',
                type: 'date'
            });
            this.source.datafields.push({
                name: 'updatedTime',
                type: 'date'
            });
            this.columns.push({
                text: 'Creation Time',
                cellsformat: 'yyyy-MM-dd hh:mm:ss',
                datafield: 'creationTime'
            });
            this.columns.push({
                text: 'Updated Time',
                datafield: 'yyyy-MM-dd hh:mm:ss'
            });
        }
        this.dataAdapter = new jqx.dataAdapter(this.source);
    }

    ngAfterViewInit(): void {
        this.source.localdata = this.accounts;
        this.grid.updatebounddata();
    }

    rowselect(event) {
        this.select.next(event.args.row as SftpAccount);
    }

    unselectRow() {
        this.grid.unselectrow(this.grid.getselectedrowindex());
    }
}

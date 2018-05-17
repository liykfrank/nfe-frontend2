import { Component, Input, ViewChild, OnChanges, SimpleChanges, ChangeDetectorRef, AfterViewChecked, AfterViewInit, Output, EventEmitter } from '@angular/core';
import { SftpAccount } from '../models/sftp-account';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';

@Component({
  selector: 'app-sftp-account',
  templateUrl: './sftp-account.component.html',
  styleUrls: ['./sftp-account.component.scss'],
})
export class SftpAccountComponent implements AfterViewInit, AfterViewChecked, OnChanges {
  @Input() public sftpAccount: SftpAccount;
  @Input() public isNew: boolean;

  @Output() onRegister: EventEmitter<SftpAccount> = new EventEmitter();
  @Output() onCancel: EventEmitter<SftpAccount> = new EventEmitter();
  @Output() onDelete: EventEmitter<SftpAccount> = new EventEmitter();

  @ViewChild("mode") private mode: jqxDropDownListComponent;
  @ViewChild("status") private status: jqxDropDownListComponent;

  sftpForm: FormGroup;
  accountModes: any[];
  accountStatus: any[];

  constructor(
    private formBuilder: FormBuilder,
    private changeDetector: ChangeDetectorRef) {
    this.accountModes = ['', 'RO', 'RW'];
    this.accountStatus = ['', 'ENABLED', 'DISABLED', 'EXPIRED'];
    this.createForm();
  }

  private createForm() {
    this.sftpForm = this.formBuilder.group({
      login: ['', [Validators.required]],
      mode: ['', [Validators.required]],
      status: ['', [Validators.required]],
      publicKey: ['']
    });
  }

  ngAfterViewChecked() {
    this.changeDetector.detectChanges();
  }

  ngAfterViewInit() {
    this.initValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.sftpForm.setValue(this.sftpAccount);
    if (this.status.host) {
      this.initValues();
    }
  }

  private initValues(): void {
    this.mode.selectItem(this.sftpAccount.mode as any);
    this.status.selectItem(this.sftpAccount.status as any);
  }

  register() {
    this.onRegister.next(this.sftpForm.value);
    this.sftpForm.disable();
  }

  cancel() {
    this.onCancel.next(this.sftpForm.value);
  }

  delete() {
    this.onDelete.next(this.sftpForm.value);
    this.sftpForm.disable();
  }
}
import { HeaderComponent } from './../../../core/components/header/header.component';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Contact } from '../../models/contact.model';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  @Input('style') style: string = '';

  @Input('name')name: string;
  @Input('telephone')telephone: string;
  @Input('email')email: string;

  @Input('disabled')disabled: boolean = true;

  // tslint:disable-next-line:no-output-on-prefix
  @Output() onChange: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() { }

  change() {
    const contact: Contact = new Contact();
    contact.contactName = this.name;
    contact.phoneFaxNumber = this.telephone;
    contact.email = this.email;
    this.onChange.emit(contact);
  }


}

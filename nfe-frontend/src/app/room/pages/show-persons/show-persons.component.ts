import { PersonsService } from './../../resources/persons.service';
import { Component, OnInit } from '@angular/core';
import { Person } from '../../models/person';

@Component({
  selector: 'app-show-persons',
  templateUrl: './show-persons.component.html',
  styleUrls: ['./show-persons.component.scss']
})
export class ShowPersonsComponent implements OnInit {

  persons: Person[];
  constructor(private personsService: PersonsService) { }

  ngOnInit() {
    this.personsService.get().subscribe(
      persons => this.persons = persons
    )

  }

}

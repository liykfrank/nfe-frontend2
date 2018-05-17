import { Component, OnInit } from '@angular/core';
interface City {
  name: string,
  code: string
}
@Component({
  selector: 'app-show-primes',
  templateUrl: './show-primes.component.html',
  styleUrls: ['./show-primes.component.scss']
})
export class ShowPrimesComponent implements OnInit {
  cities: City[];
  selectedCity: City;

  constructor() {
    this.cities = [
      {name: 'New York', code: 'NY'},
      {name: 'Rome', code: 'RM'},
      {name: 'London', code: 'LDN'},
      {name: 'Istanbul', code: 'IST'},
      {name: 'Paris', code: 'PRS'}
  ];
   }


  ngOnInit() {
  }

  selectMod(){
    this.selectedCity= {name: 'Istanbul', code: 'IST'};
  }
}

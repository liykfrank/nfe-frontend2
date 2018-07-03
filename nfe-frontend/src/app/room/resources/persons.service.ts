import { Injector } from "@angular/core";
import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { NwRepositoryAbstract } from "../../shared/base/nwe-repository.abstract";
import { Person } from "../models/person";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class PersonsService extends NwRepositoryAbstract<Person[], Object> {
  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.basePath,
      injector
    );
  }
}

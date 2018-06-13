# BSPlink Agency Memo

provides REST API for ACM and ADM issuances

includes enpoints for:

- maintenance ofTax On Commission Type Codes
- file attach
- add comments to acdm
- query of Agent Details (Address)
- query of Airline Details (Address)

The API consumes the following APIs:

- Agent Maintenance
- Airline Maintenance

## Application Configuration

Parameters:

feign.agents.url: URL of Agent Maintenance API

feign.airlines.url: URL of Airline Maintenance API

## Mocked services

The service depends of some external REST services that should be available in order to the service
to work:

- bsplink-agent-management.
- bsplink-airline-management.

these services are mocked in the package `org.iata.bsplink.agencymemo.fake.restclient` and
they are loaded when the `mock` profile is active.

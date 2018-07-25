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



export const environment = {
  environment: 'dev',
  token: 'eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4ekszUGt4YnY0UmtzTEtwNVlwR2FvVGloblFJNUNBbWVZQm4zbXVMTThzIn0.eyJqdGkiOiI2YTIyNDc2Mi1hNTgwLTQ2NDMtOGY5OS00N2UzYmM0NjRmMjQiLCJleHAiOjE2NDcyNjE3MjUsIm5iZiI6MCwiaWF0IjoxNTM0OTQxNzI1LCJpc3MiOiJodHRwOi8va2V5Y2xvYWstZGV2Lm5mZWRldi5hY2NlbHlhLmNvbS9hdXRoL3JlYWxtcy9ORkUiLCJhdWQiOiJic3BsaW5rLXVzZXItbWFuYWdlbWVudCIsInN1YiI6ImY4NjM1ZDM2LTEzMmEtNDRlMi1iNjI1LTk1YTkyNjkyMzMyMCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImJzcGxpbmstdXNlci1tYW5hZ2VtZW50IiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiODIyNGQ1MzMtOGJlYi00ZjMzLTkxZjItM2M4MGFhNDI0ZGI4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiQURNSU4iLCJVU0VSIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0In0.ZqLsZe0TQe4rtbNOxgWNLTmYWYbrRMrPfkiLpab7snEzfvZP00H2lyIXU7TAMZgX5rrUnV6WsINrvG3T6djF2a4_cqZVD1bP5kIDHfyYWrdTD69XVSHRIShNRgmkTbpPN_EI7BYmrwD7jIBE7n0-lduK7-neZmho58_TJlulqp51KS9_6TlXrOlA1n4vrrGxmgDZKYr9TdtpK8tAUfsTBOuH2FHylYyXkyF_dDYBjQbcdnoSJmA8qv8AVODPvj5POHFo_GHzgElo2Z1JUcZHEDgc229475vzKZFgF1KTFMa57fgrmKt9UjZLyOGdM7g_otz7joJDZguzilo9oNFAvQ',
  basePath: 'http://servicesdev.nfedev.accelya.com/',
  production: false,
  api: {
    masterData: {
      agent: 'masterdata/agent/v1/agents/'
    },
    refund: {
      agent: 'refund/v1/agents/', // {agentCode}
      airline: 'refund/v1/airlines/', // /{isoCountryCode}/{airlineCode}
      configurations: 'refund/v1/configurations',
      issuePermission: 'refund/v1/refunds/indirects/permissions',
      refund_indirect: 'refund/v1/refunds/indirects/',
      issuePermissionController: 'refund/v1/refunds/indirects/permissions',
      reasons_indirects: 'refund/v1/reasons/indirects',
      currency: 'refund/v1/currencies/', // {isoc}
    },
    adm_acm: {
      configuration: 'agencymemo/v1/configurations',
      airline: 'agencymemo/v1/airlines', // /{isoCountryCode}/{airlineCode}
      agent: 'agencymemo/v1/agents/', // {agentCode}
      acdm: 'agencymemo/v1/acdms', // {id} | /{id}/files
      country: 'agencymemo/v1/countries',
      toca: 'agencymemo/v1/tctps', // {isoc} | {isoc}/{code}: remove
      currency: 'agencymemo/v1/general-info/currencies/', // {isoc}
      period: 'agencymemo/v1/general-info/periods', // {isoc}
      reasons: 'agencymemo/v1/reasons'
    },
    user: {
      user: '/assets/mocks/user.json',
      template: 'assets/mocks/templates.json',
      countryTerritory: 'assets/mocks/countries.json'
    }
  },
  files: {
    api: {
      listFiles: '/v1/files',
      downloadFile: '/v1/files',
      downloadFiles: '/v1/files/zip',
      removeFile: '/v1/files',
      removeFiles: '/v1/files',
      apiConfiguration: '/v1/configurations',
      apiUpload: '/v1/files'
    },
    basePath: 'files'
  },
  sftAccount: {
    api: {
      create: '?u=register&p=register&path=/WebInterface/jQuery/messageForm.html&registration_username={1}&registration_email={2}',
      modify: '?command=request_reset&reset_username_email={1}&currentURL=http://ftp.nfedev.accelya.com/WebInterface/jQuery/reset.html'
    },
    basePath: 'http://ftp.nfedev.accelya.com/'
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};

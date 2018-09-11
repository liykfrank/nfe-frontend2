export const environment = {
  environment: 'qai',
  token: '"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyeEt5X3dPbDZFMlNtSGM0ei1WTnBvNUVlMER0bUl4VjZYMEluQzJVakdrIn0.eyJqdGkiOiI0YWUwMzdkZi1iMDZiLTQ2YmUtOTFjZC05NzIyMThlZDZhMzAiLCJleHAiOjE2NDg5OTc2MDUsIm5iZiI6MCwiaWF0IjoxNTM2Njc3NjA1LCJpc3MiOiJodHRwOi8va2V5Y2xvYWstcWFpLm5mZWRldi5hY2NlbHlhLmNvbS9hdXRoL3JlYWxtcy9ORkUiLCJhdWQiOiJic3BsaW5rLWxvZ2luLW1hbmFnZW1lbnQiLCJzdWIiOiI5ZTNhMDNkNy1mM2E3LTQ1ZWYtYjg2MS1kNDE3YTU0M2Q4ZGMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJic3BsaW5rLWxvZ2luLW1hbmFnZW1lbnQiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJmNTg4Mjg4YS1hZDFhLTQwYmQtOTUwNi0zYWM2NzIyODJlZTYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImltcGVyc29uYXRpb24iLCJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwiQURNSU4iLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlVTRVIiLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJhZG1pbi1jbGkiOnsicm9sZXMiOlsidW1hX3Byb3RlY3Rpb24iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdCIsImVtYWlsIjoiZGFuaWxvLnJvZGFzQGFjY2VseWEuY29tIn0.WQvok7_-OH2c5dJ-OJLNCy1Yr-LAn2KunfJmx4lCMwIEg0Kf4nEGSK388TgNZmFIDetxT09Kp9WnLkylMlTEhmjIhc9220-9k7lIBTU4BPcahZi_PL5oaFPj7Sahr9xrenqFbfs2MoGBAu9r0tCO7Zdy7MejZRpV6nAhIzqVYh-FqCEsJbRg5QbO3j98yBfVaiz0OS0zv4fbP0UyFlmb4tK1yJHODMwjce21vrF9Sb8_QDTBEnZXd8hTCR6qVF0ucs2CvxQ4l7Ien0JeYIVcaWjewDBH97noqT1LotllieVVpDEwlx_2Azeq1W9AY09bJZ3PJOYoEuVoCEIBJKnmMA',
  basePath: 'http://servicesqai.nfedev.accelya.com/',
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
    files: {
      configuration: 'files/v1/configurations',
      files: 'files/v1/files'
    },
    user: {
      user: '/assets/mocks/user.json',
      userMaintenance: 'user/v1/users',
      createUser: 'user/v1/users',
      template: 'assets/mocks/templates.json',
      countryTerritory: 'assets/mocks/countries.json',
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

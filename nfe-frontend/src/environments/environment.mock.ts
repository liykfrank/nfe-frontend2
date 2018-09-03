export const environment = {
  environment: 'mock',
  token: '',
  basePath: 'http://localhost:3000/',
  production: false,
  api: {
    masterData: {
      agent: 'masterdata/agent/v1/agents/'
    },
    refund: {
      agent: 'agent/', // {agentCode}
      airline: 'airline/', // /{isoCountryCode}/{airlineCode}
      configurations: 'refund-configuration',
      refund_indirect: 'refund-indirect',
      issuePermission: 'refund/v1/refunds/indirects/permissions',
      reasons_indirects: 'reasons',
      currency: 'currencies/', // {isoc}
    },
    adm_acm: {
      configuration: 'adm-acm-configuration',
      airline: 'airline/', // /{isoCountryCode}/{airlineCode}
      agent: 'agent/', // {agentCode}
      acdm: 'acdm', // {id} | /{id}/files
      country: 'country',
      toca: 'tctps', // {isoc} | {isoc}/{code}: remove
      currency: 'currencies/', // {isoc}
      period: 'periods', // {isoc}
      reasons: 'reasons',
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

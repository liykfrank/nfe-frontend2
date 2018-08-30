
export const environment = {
  environment: 'qai',
  token: '',
  basePath: 'http://servicesqai.nfedev.accelya.com',
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

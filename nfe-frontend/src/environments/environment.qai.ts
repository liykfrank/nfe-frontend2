
export const environment = {
  environment: 'qai',
  basePath: 'http://servicesqai.nfedev.accelya.com',
  ionic: false,
  production: false,
  mock: false,
  api: {
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
      user: '/assets/mocks/user.json'
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
    basePath: '/files'
  },
  sftAccount: {
    api: {
      create: '?u=register&p=register&path=/WebInterface/jQuery/messageForm.html&registration_username={1}&registration_email={2}',
      modify: '?command=request_reset&reset_username_email={1}&currentURL=http://sftp.nfedev.accelya.com:8444/WebInterface/jQuery/reset.html'
    },
    basePath: 'http://sftp.nfedev.accelya.com:8444/'
  },
  adm_acm: {
    api: {
      acdm: '/v1/acdms', // post: save | /{id} | /{id}/files
      configuration: '/v1/configurations',
      company: '/v1/airlines', // /{isoCountryCode}/{airlineCode}
      agent: '/v1/agents', // {code}
      country: '/v1/countries', // creo v1/configurations/isocs ¿?¿?
      toca: '/v1/tctps', // {isoc} | {isoc}/{code}: remove
      currency: '/v1/general-info/currencies', // {isoc}
      period: '/v1/general-info/periods', // {isoc}
      reasons: '/v1/reasons'
    },
    basePath: '/agencymemo'
  },
  refunds: {
    api: {
      agent: '/v1/agents',
      company: '/v1/airlines', // /{isoCountryCode}/{airlineCode}
    },
    basePath: '/refund'
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};

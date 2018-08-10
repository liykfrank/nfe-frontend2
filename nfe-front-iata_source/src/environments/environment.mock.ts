export const environment = {
  ionic: false,
  basePath: 'http://localhost:4200/',
  production: false,
  mock: true,
  api: {
    refund: {
      agent: 'refund/v1/agents', // {agentCode}
      airline: 'refund/v1/airlines', // /{isoCountryCode}/{airlineCode}
      configurations: 'refund/v1/configurations'
    },
    adm_acm: {
      configuration: 'agencymemo/v1/configurations',
      airline: 'agencymemo/v1/airlines', // /{isoCountryCode}/{airlineCode}
      agent: 'agencymemo/v1/agents', // {agentCode}
      acdm: 'agencymemo/v1/acdms', // {id} | /{id}/files
      country: 'agencymemo/v1/countries',
      toca: 'agencymemo/v1/tctps', // {isoc} | {isoc}/{code}: remove
      currency: 'agencymemo/v1/general-info/currencies', // {isoc}
      period: 'agencymemo/v1/general-info/periods', // {isoc}
      reasons: 'agencymemo/v1/reasons'
    },
    user: {
      user: '/assets/mocks/user.json'
    }
  },
  user: {
    api: {
      getUser: 'user.json'
    },
    basePath: 'assets/mocks/'
  },
  files: {
    api: {
      listFiles: 'listfiles.json',
      downloadFile: 'down/foto.png',
      downloadFiles: 'down/Downloads.zip',
      removeFile: 'remfile.json',
      removeFiles: 'remfiles.json',
      apiConfiguration: 'v1/configurations',
      apiUpload: 'v1/files'
    },
    basePath: 'assets/mocks/'
  },
  accounts: {
    api: {
      accounts: ''
    },
    basePath: ''
  },
  sftAccount: {
    api: {
      create: '?u=register&p=register&path=/WebInterface/jQuery/messageForm.html&registration_username={1}&registration_email={2}',
      modify: '?command=request_reset&reset_username_email={1}&currentURL=http://ftp.nfedev.accelya.com/WebInterface/jQuery/reset.html'
    },
    basePath: 'http://ftp.nfedev.accelya.com/'
  },
  adm_acm: {
    api: {
      acdm: '',
      configuration: 'configuration.json',
      company: 'airline.json',
      agent: 'agent.json',
      country: 'countries.json',
      toca: 'toca.json',
      currency: 'currency.json',
      period: 'billingPeriod.json',
      reasons: 'reasons.json'
    },
    basePath: 'assets/mocks/adcm/'
  },
  refunds: {
    api: {
      agent: '',
      company: '', // /{isoCountryCode}/{airlineCode}
    },
    basePath: 'assets/mocks/refund/'
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};

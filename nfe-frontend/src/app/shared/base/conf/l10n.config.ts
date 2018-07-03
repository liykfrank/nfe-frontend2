import { L10nConfig, ProviderType, StorageStrategy } from 'angular-l10n';

export const l10nConfig: L10nConfig = {
  locale: {
    languages: [
        { code: 'en', dir: 'ltr' },
        { code: 'it', dir: 'ltr' },
      ],
      defaultLocale: { languageCode: 'en', countryCode: 'US' },
      currency: 'USD'
      /* ,
      storage: StorageStrategy.Cookie */
  },
  translation: {
      providers: [
          { type: ProviderType.Static, prefix: './assets/i18n/locale-'}
      ],
      caching: true,
      composedKeySeparator: '.',
      missingValue: 'No key'
  }
};

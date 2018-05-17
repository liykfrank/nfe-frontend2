import { L10nConfig, ProviderType, StorageStrategy } from 'angular-l10n';

export const l10nConfig: L10nConfig = {
  locale: {
    languages: [
        { code: 'en', dir: 'ltr' },
        { code: 'it', dir: 'ltr' },
    ],
    language: 'en'
   // storage: StorageStrategy.Cookie
},
translation: {
    providers: [
        { type: ProviderType.Static, prefix: './assets/i18n/locale-' }
    ],
    caching: true,
    missingValue: 'No key'
}
};

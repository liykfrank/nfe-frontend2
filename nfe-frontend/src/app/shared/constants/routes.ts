import { UserViews } from '../enums/users.enum';

export const ROUTES = {
    DASHBOARD: { URL: '/dashboard', LABEL: 'dashboard', PATH: 'dashboard' },
    ADM_ISSUE: { URL: '/issue/adm', LABEL: 'adm', PATH: 'adm' },
    ACM_ISSUE: { URL: '/issue/acm', LABEL: 'acm', PATH: 'acm' },
    REFUNDS: { URL: '/refunds', LABEL: 'refunds', PATH: 'refunds' },
    QUERY_FILES: { URL: '/files/query-files', LABEL: 'query-files', PATH: 'query-files' },
    QUERY_FILES_READ_ONLY: { URL: '/files/query-files-read-only', LABEL: 'query-files-read-only', PATH: 'query-files-read-only' },
    UPLOAD_FILES: { URL: '/files/upload-files', LABEL: 'upload-files', PATH: 'upload-files' },
    CREATE_ACCOUNT: { URL: '/create-account', LABEL: 'create-account', PATH: 'create-account' },
    TICKETING: { URL: '/ticketing', LABEL: 'ticketing', PATH: 'ticketing' },
    GENERAL_INFO: { URL: '/general-info', LABEL: 'general-info', PATH: 'general-info' },
    SELF_SERVICE: { URL: '/self-service', LABEL: 'self-service', PATH: 'self-service' },
    MONITOR: { URL: '/monitor', LABEL: 'monitor', PATH: 'monitor' },
    NEW_USER: { URL: `/users/${UserViews.NEW_USER}`, LABEL: UserViews.NEW_USER, PATH: UserViews.NEW_USER },
    NEW_SUB_USER: { URL: `/users/${UserViews.NEW_SUB_USER}`, LABEL: UserViews.NEW_SUB_USER, PATH: UserViews.NEW_SUB_USER },
    MOD_USER: { URL: `/users/${UserViews.MOD_USER}`, LABEL: UserViews.MOD_USER, PATH: UserViews.MOD_USER },
    MOD_SUB_USER: { URL: `/users/${UserViews.MOD_SUB_USER}`, LABEL: UserViews.MOD_SUB_USER, PATH: UserViews.MOD_SUB_USER },
};

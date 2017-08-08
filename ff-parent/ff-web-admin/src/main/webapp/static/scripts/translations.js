function config($translateProvider) {
	$translateProvider
		.translations('hr', {
			LANGUAGE: 'Jezik',
			ENGLISH: 'Engleski',
			CROATIAN: 'Hrvatski',
			
			LOGOUT: 'Odjava',
			
			MENU_ACTIVITIES: 'Sektori',
			MENU_ALGORITHM_ITEMS: 'Stavke algoritma',
			MENU_ARTICLES: 'Članci',
			MENU_BUSINESS_RELATIONSHIP_MANAGERS: 'Voditelji poslovnog odnosa',
			MENU_COMPANY_ITEMS: 'Stavke poduzeća',
			MENU_CONTACTS: 'Kontakti',
			MENU_DASHBOARD: 'Naslovnica',
			MENU_DEBUGGING: 'Debugging',
			MENU_INVESTMENTS: 'Investicije',
			MENU_ORG_UNITS: 'Organizacijske jedinice',
			MENU_ROLES: 'Korisničke uloge',
			MENU_SETTINGS: 'Postavke',
			MENU_STATISTICS: 'Statistike',
			MENU_SUBDIVISIONS1: 'Županije',
			MENU_SUBDIVISIONS2: 'Općine / gradovi',
			MENU_TENDERS: 'Natječaji',
			MENU_TENDER_ITEMS: 'Stavke natječaja',
			MENU_USERS: 'Korisnici',
			MENU_USER_GROUPS: 'Korisničke grupe',
			MENU_EMAIL: 'E-mail',

			HEADER_ACTIVITY_ADD: 'Novi sektor',
			HEADER_ACTIVITY_EDIT: 'Uredi sektor',
			HEADER_ALGORITHM_ITEM_ADD: 'Nova stavka algoritma',
			HEADER_ALGORITHM_ITEM_EDIT: 'Uredi stavku algoritma',
			HEADER_ARTICLE_ADD: 'Novi članak',
			HEADER_ARTICLE_DETAILS: 'Detalji članka',
			HEADER_ARTICLE_EDIT: 'Uredi članak',
			HEADER_BRM_ADD: 'Novi VPO',
			HEADER_BRM_EDIT: 'Uredi podatke o VPO-u',
			HEADER_COMPANY_ITEM_ADD: 'Nova stavka poduzeća',
			HEADER_COMPANY_ITEM_DETAILS: 'Detalji stavke poduzeća',
			HEADER_COMPANY_ITEM_EDIT: 'Uredi stavku poduzeća',
			HEADER_CONTACT_DETAILS: 'Detalji kontakta',
			HEADER_EMAIL_HISTORY: 'Povijest',
			HEADER_EMAIL_SEND: 'Pošalji e-mail',
			HEADER_INVESTMENT_ADD: 'Nova investicija',
			HEADER_INVESTMENT_DETAILS: 'Detalji investicije',
			HEADER_INVESTMENT_EDIT: 'Uredi investiciju',
			HEADER_MAIN: 'Administracijska konzola',
			HEADER_ORG_UNIT_ADD: 'Nova organizacijska jedinica',
			HEADER_ORG_UNIT_EDIT: 'Uredi organizacijsku jedinicu',
			HEADER_ROLE_ADD: 'Nova uloga',
			HEADER_ROLE_EDIT: 'Uredi ulogu',
			HEADER_SUBDIVISION1_ADD: 'Nova županija',
			HEADER_SUBDIVISION1_EDIT: 'Uredi županiju',
			HEADER_SUBDIVISION2_ADD: 'Nova općina / grad',
			HEADER_SUBDIVISION2_EDIT: 'Uredi općinu / grad',
			HEADER_TENDER_ADD: 'Novi natječaj',
			HEADER_TENDER_DETAILS: 'Detalji natječaja',
			HEADER_TENDER_EDIT: 'Uredi natječaj',
			HEADER_TENDER_ITEM_ADD: 'Nova stavka natječaja',
			HEADER_TENDER_ITEM_DETAILS: 'Detalji stavke natječaja',
			HEADER_TENDER_ITEM_EDIT: 'Uredi stavku natječaja',
			HEADER_USER_DETAILS: 'Detalji o korisniku',
			HEADER_USER_GROUP_ADD: 'Nova korisnička grupa',
			HEADER_USER_GROUP_EDIT: 'Uredi korisničku grupu',
			
			TAB_BASIC_DATA: 'Osnovni podaci',
			TAB_BRM: 'Voditelji poslovnog odnosa',
			TAB_COMMON_ITEMS: 'Zajedničke stavke',
			TAB_COMPANY_DATA: 'Podaci o poduzeću',
			TAB_CUSTOM_ITEMS: 'Zasebne stavke',
			TAB_EMAIL: 'E-mail',
			TAB_ITEM_INFO: 'Info',
			TAB_PROJECTS: 'Projekti',
			TAB_MATCHING_TENDERS: 'Prihvatljivi natječaji',
			TAB_MATCHING_USERS: 'Prihvatljivi korisnici',
			TAB_PREVIEW: 'Pregled',
			TAB_STATISTICS: 'Statistika',
			TAB_USER_DATA: 'Korisnički podaci',
			
			TAB_STATS_COMPANIES_BY_COUNTIES: 'Poduzeća po županijama',
			TAB_STATS_COMPANIES_BY_REVENUES: 'Poduzeća po prihodima',
			TAB_STATS_COMPANIES_BY_SIZE: 'Poduzeća po veličini',
			TAB_STATS_INVESTMENTS_BY_COUNTIES: 'Ulaganja po županijama',
			TAB_STATS_INVESTMENTS_BY_ACTIVITIES: 'Ulaganja po sektorima',
			
			LABEL_ACTIVITY: 'Sektor',
			LABEL_ACTIVITY_NAME: 'Naziv sektora',
			LABEL_ALL_TENDERS: 'Svi natječaji',
			LABEL_AREA: 'Area',
			LABEL_CODE: 'Oznaka',
			LABEL_COMPANY_CODE: 'OIB',
			LABEL_COMPANY_ITEM: 'Stavka poduzeća',
			LABEL_COMPANY_NAME: 'Ime',
			LABEL_CONDITIONAL_ITEM: 'Uvjetna stavka',
			LABEL_CREATION_DATE: 'Datum registracije',
			LABEL_DESCRIPTION: 'Opis',
			LABEL_DEVELOPMENT_INDEX: 'Indeks razvijenosti',
			LABEL_DISPLAY: 'PRIKAŽI',
			LABEL_DISPLAY_POSTSCRIPT: 'Prikaz kakav će biti vidljiv korisnicima',
			LABEL_EDIT: 'UREDI',
			LABEL_EDIT_POSTSCRIPT: 'Prikaz pri uređivanju (npr. Uredi natječaj)',
			LABEL_EMAIL: 'E-mail',
			LABEL_EMAIL2: 'E-mail (sekundarni)',
			LABEL_EMPHASIZE: 'Naglašena stavka',
			LABEL_ENTITY_ATTRIBUTES: 'Atributi entiteta',
			LABEL_ENTITY_TYPE: 'Tip entiteta',
			LABEL_EXPORTING: 'IZVOZIM',
			LABEL_FIRST_NAME: 'Ime',
			LABEL_PROJECTS_4_TENDER: 'Projekti prihvatljivi za ovaj natječaj',
			LABEL_HELP: 'Pomoć',
			LABEL_ID: 'ID',
			LABEL_IMAGE: 'Slika',
			LABEL_INVESTMENT: 'Investicija',
			LABEL_LAST_LOGIN_DATE: 'Zadnja prijava',
			LABEL_LAST_GFI_SYNC_DATE: 'Zadnja GFI sinkronizacija',
			LABEL_LAST_MODIFIED: 'Zadnja promjena',
			LABEL_LAST_MODIFIED_DATE: 'Last modified',
			LABEL_LAST_NAME: 'Prezime',
			LABEL_MAIN: 'Primarni',
			LABEL_MANDATORY: 'Obvezno',
			LABEL_MANDATORY_ITEMS_ONLY: 'Samo obvezna polja',
			LABEL_MESSAGE: 'Poruka',
			LABEL_METATAG: 'Meta tag',
			LABEL_MOBILE: 'Mobitel',
			LABEL_NAME: 'Naziv',
			LABEL_OPERATOR: 'Operator',
			LABEL_OPTIONS: 'Opcije',
			LABEL_ORG_UNIT: 'Organizacijska jedinica',
			LABEL_PERMISSIONS: 'Dopuštenja',
			LABEL_PHONE: 'Telefon',
			LABEL_PLEASE_WAIT: 'MOLIMO PRIČEKAJTE',
			LABEL_POSITION: 'Pozicija',
			LABEL_READ_MORE: 'Pročitaj više',
			LABEL_REGISTERED_USERS: 'Registrirani korisnici',
			LABEL_REGISTRATION_TYPE: 'Tip registracije',
			LABEL_SECTOR: 'Sektor',
			LABEL_SECTOR_NAME: 'Ime sektora',
			LABEL_SEND_EMAIL_SUBJECT: 'Naslov',
			LABEL_SEND_EMAIL_TEXT: 'Tekst',
			LABEL_SEND_EMAIL_TO: 'Za',
			LABEL_SEND_EMAIL_TO_PLACEHOLDER: 'Molimo odaberite...',
			LABEL_SUBDIVISION1: 'Županija',
			LABEL_SUBSTITUTE: 'Sekundarni',
			LABEL_SUMMARY_ITEM: 'Stavka sažetaka',
			LABEL_STATISTICS_FOR: 'Statistika za',
			LABEL_TENDERS_4_PROJECT: 'Natječaji za koje je ovaj projekt prihvatljiv',
			LABEL_TENDER_ITEM: 'Stavka natječaja',
			LABEL_TEXT: 'Tekst',
			LABEL_TOTAL: 'Ukupno',
			LABEL_TYPE: 'Tip',
			LABEL_UNIQUE_VISITS: 'Jedinstveni posjeti',
			LABEL_USER: 'Korisnik',
			LABEL_USERS: 'Korisnici',
			LABEL_VISITS: 'Posjeti',
			LABEL_WIDGET_ITEM: 'Stavka widgeta',
			
			COLUMN_ACTIVITY: 'Sektori',
			COLUMN_ACTIVITY_NAME: 'Naziv sektora',
			COLUMN_AREA: 'Area',
			COLUMN_CODE: 'Oznaka',
			COLUMN_COMPANY: 'Poduzeće',
			COLUMN_COMPANY_CODE: 'OIB',
			COLUMN_COMPANY_ITEM_CODE: 'Kod stavke poduzeća',
			COLUMN_COMPANY_ITEM_TEXT: 'Tekst stavke poduzeća',
			COLUMN_CONTACT_LOCATION: 'Poslovnica',
			COLUMN_CREATION_DATE: 'Datum kreiranja',
			COLUMN_DEVELOPMENT_INDEX: 'Indeks razvijenosti',
			COLUMN_EMAIL: 'E-mail',
			COLUMN_FIRST_NAME: 'Ime',
			COLUMN_FIRST_AND_LAST_NAME: 'Ime i prezime',
			COLUMN_ID: 'ID',
			COLUMN_INVESTMENT: 'Investicija',
			COLUMN_INVESTMENT_VALUE_TOTAL: 'Vrijednost investicije',
			COLUMN_LAST_MODIFIED_DATE: 'Datum zadnje promjene',
			COLUMN_LAST_NAME: 'Prezime',
			COLUMN_MANDATORY: 'Obvezno',
			COLUMN_METATAG: 'Meta tag',
			COLUMN_MOBILE: 'Mobitel',
			COLUMN_NAME: 'Naziv',
			COLUMN_NO_OF_COMPANIES: 'Broj poduzeća',
			COLUMN_NO_OF_INVESTMENTS: 'Broj investicija',
			COLUMN_OPERATOR: 'Operator',
			COLUMN_ORG_UNIT: 'Organizacijska jedinica',
			COLUMN_PERCENTAGE: 'Postotak',
			COLUMN_PHONE: 'Telefon',
			COLUMN_POSITION: 'Pozicija',
			COLUMN_REGISTRATION_TYPE: 'Tip registracije',
			COLUMN_REVENUE: 'Prihod',
			COLUMN_SECTOR: 'Sektor',
			COLUMN_SECTOR_NAME: 'Naziv sektora',
			COLUMN_SEND_DATE: 'Datum slanja',
			COLUMN_SIZE: 'Veličina',
			COLUMN_SUMMARY_ITEM: 'Stavka sažetka',
			COLUMN_STATUS: 'Status',
			COLUMN_SUBDIVISION1: 'Županija',
			COLUMN_SUBJECT: 'Naslov',
			COLUMN_TENDER_ITEM_CODE: 'Kod stavke natječaja',
			COLUMN_TENDER_ITEM_TEXT: 'Tekst stavke natječaja',
			COLUMN_TEXT: 'Tekst',
			COLUMN_TO: 'Za',
			COLUMN_TYPE: 'Tip',
			COLUMN_WIDGET_ITEM: 'Stavka widgeta',

			BUTTON_ADD: 'Dodaj',
			BUTTON_BACK: 'Natrag',
			BUTTON_CLOSE: 'Zatvori',
			BUTTON_DEBUG: 'Debug',
			BUTTON_DELETE: 'Izbriši',
			BUTTON_EDIT: 'Uredi',
			BUTTON_NO: 'Ne',
			BUTTON_RESET: 'Poništi',
			BUTTON_SAVE: 'Spremi',
			BUTTON_SELECT: 'Odaberi...',
			BUTTON_SEND: 'Pošalji',
			BUTTON_YES: 'Da',
			BUTTON_EXPORT: 'Izvoz',
			BUTTON_EXPORT_ALL: 'Izvezi sve',
			BUTTON_EXPORT_SELECTED: 'Izvezi odabrane',
			BUTTON_IMPORT: 'Uvoz',
			BUTTON_GFI_SYNC: 'GFI sinkronizacija',
			BUTTON_EXPORT_COMPANY_DATA: 'Izvezi podatke o poduzećima',
			BUTTON_EXPORT_PROJECT_DATA: 'Izvezi podatke o projektima',
			
			BUTTON_DATE_CURRENT: 'Danas',
			BUTTON_DATE_CLEAR: 'Izbriši',
			BUTTON_DATE_CLOSE: 'Zatvori',

			TO_CSV: 'u CSV',
			TO_PDF: 'u PDF',
			TO_JSON: 'u JSON',
			FROM_JSON: 'iz JSON',
			
			DATETIMEPICKER_TODAY: 'Danas',
			DATETIMEPICKER_YESTERDAY: 'Jučer',
			DATETIMEPICKER_LAST_7_DAYS: 'Posljednih 7 dana',
			DATETIMEPICKER_LAST_30_DAYS: 'Posljednih 30 dana',
			DATETIMEPICKER_THIS_MONTH: 'Ovaj mjesec',
			DATETIMEPICKER_LAST_MONTH: 'Prošli mjesec',
			DATETIMEPICKER_CUSTOM: 'Proizvoljni period',
			DATETIMEPICKER_FROM: 'Od',
			DATETIMEPICKER_TO: 'Do',
			DATETIMEPICKER_APPLY: 'Primjeni',
			DATETIMEPICKER_CANCEL: 'Poništi',
			
			STATISTICS_PERIOD_LAST_7_DAYS: 'Posljednih 7 dana',
			STATISTICS_PERIOD_LAST_6_MONTHS: 'Posljednih 6 mjeseci',
			
			PERIOD_TODAY: 'Danas',
			PERIOD_LAST_7_DAYS: 'U posljednjih 7 dana',
			PERIOD_LAST_6_MONTHS: 'U posljednih 6 mjeseci',
			
			STATUS_ACTIVE: 'Aktivan',
			STATUS_INACTIVE: 'Neaktivan',
			STATUS_INCOMPLETE: 'Nepotpun',
			STATUS_WAITING_CONFIRMATION: 'Čekanje potvrde',
			
			REGISTRATION_TYPE_INTERNAL: 'Registracijski obrazac',
			REGISTRATION_TYPE_EXTERNAL: 'e-ZaBa',
			
			ITEM_TYPE_ACTIVITY: 'Activity',
			ITEM_TYPE_ACTIVITY_DESCRIPTION: 'Rendered as select component where none or one option can be selected. Options are defined in Settings - Activities.',
			ITEM_TYPE_ACTIVITIES: 'Activities',
			ITEM_TYPE_ACTIVITIES_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are defined in Settings - Activities.',
			ITEM_TYPE_CHECKBOX: 'Check box',
			ITEM_TYPE_CHECKBOX_DESCRIPTION: 'Rendered as checkbox(es) where none, one or many option(s) can be selected. Options can be defined below.',
			ITEM_TYPE_CURRENCY: 'Currency',
			ITEM_TYPE_CURRENCY_DESCRIPTION: 'Rendered as combination of input field that accepts numbers only and select where currency can be selected.',
			ITEM_TYPE_DATE: 'Date',
			ITEM_TYPE_DATE_DESCRIPTION: 'Rendered as date picker component.',
			ITEM_TYPE_DATE_TIME: 'Date time',
			ITEM_TYPE_HYPERLINK: 'Hyperlink',
			ITEM_TYPE_HYPERLINK_DESCRIPTION: 'Rendered as clickable hyperlink that will open entered URL in new browser window.',
			ITEM_TYPE_INVESTMENTS_PRIMARY: 'Primary investments',
			ITEM_TYPE_INVESTMENTS_SECONDARY: 'Secondary investments',
			ITEM_TYPE_INVESTMENTS_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Only active investments are displayed as option(s).',
			ITEM_TYPE_MULTISELECT: 'Multiselect',
			ITEM_TYPE_MULTISELECT_DESCRIPTION: 'Rendered as select component where none (optional), one or many (mandatory) option(s) can be selected. Options can be defined below.',
			ITEM_TYPE_NUMBER: 'Number',
			ITEM_TYPE_NUMBER_DESCRIPTION: 'Rendered as input field that accepts numbers only.',
			ITEM_TYPE_PERCENTAGE: 'Percentage',
			ITEM_TYPE_PERCENTAGE_DESCRIPTION: 'Rendered as input field that accepts numbers in range 0-100.',
			ITEM_TYPE_RADIO: 'Radio',
			ITEM_TYPE_RADIO_DESCRIPTION: 'Rendered as radio button group where none (optional) or one (mandatory) option can be selected. Options can be defined below.',
			ITEM_TYPE_SELECT: 'Select',
			ITEM_TYPE_SELECT_DESCRIPTION: 'Rendered as select component where none (optional) or one (mandatory) option can be selected. Options can be defined below.',
			ITEM_TYPE_SUBDIVISION1: 'County',
			ITEM_TYPE_SUBDIVISION1_DESCRIPTION: 'Rendered as select component where none or one option can be selected. Options are defined in Settings - Counties.',
			ITEM_TYPE_SUBDIVISION2: 'Municipality / city',
			ITEM_TYPE_SUBDIVISION2_DESCRIPTION: 'Rendered as select component where none or one option can be selected. Options are defined in Settings - Municipalities / cities.',
			ITEM_TYPE_SUBDIVISIONS1: 'Counties',
			ITEM_TYPE_SUBDIVISIONS1_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are defined in Settings - Counties.',
			ITEM_TYPE_SUBDIVISIONS2: 'Municipalities / cities',
			ITEM_TYPE_SUBDIVISIONS2_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are defined in Settings - Municipalities / cities.',
			ITEM_TYPE_TEXT: 'Text',
			ITEM_TYPE_TEXT_AREA: 'Text area',
			ITEM_TYPE_TEXT_AREA_DESCRIPTION: 'Rendered as text area that enables some primitive formatting features (e.g. new line, new paragraph).',
			ITEM_TYPE_TEXT_DESCRIPTION: 'Rendered as input text field. Maximum length is 512 characters.',
			ITEM_TYPE_TEXT_EDITOR: 'Text editor',
			
			ALGORITHM_ITEM_TYPE_MANDATORY: 'Obavezna',
			ALGORITHM_ITEM_TYPE_OPTIONAL: 'Opcionalna',
			ALGORITHM_ITEM_TYPE_CONDITIONAL: 'Uvjetna',
			
			OPERATOR_EQUAL: 'Jednako',
			OPERATOR_GREATER_OR_EQUAL: 'Veće ili jednako',
			OPERATOR_LESS_OR_EQUAL: 'Manje ili jednako',
			OPERATOR_IN: 'Unutar',
			
			BOOLEAN_TRUE: 'Da',
			BOOLEAN_FALSE: 'Ne',
			
			USER_GROUP_METATAG_MATCHING_USERS: 'Prihvatljivi korisnici',
			
			ACTION_TOOLTIP_ACTIVATE: 'Aktiviraj',
			ACTION_TOOLTIP_DEACTIVATE: 'Deaktiviraj',
			ACTION_TOOLTIP_EDIT: 'Uredi',
			ACTION_TOOLTIP_EXPORT: 'Izvezi',
			ACTION_TOOLTIP_DELETE: 'Izbriši',
			ACTION_TOOLTIP_DETAILS: 'Detalji',
			ACTION_TOOLTIP_EXPAND: 'Proširi',
			ACTION_TOOLTIP_COLLAPSE: 'Sažmi',
			
			TOOLTIP_TOTAL_IMPRESSIONS: 'Ukupni pregledi',
			TOOLTIP_UNIQUE_IMPRESSIONS: 'Jedinstveni pregledi',
			TOOLTIP_CHART_VIEW: 'Grafikon',
			TOOLTIP_TABLE_VIEW: 'Tablica',
			
			ACTION_ACTIVATE_SUCCESS_MESSAGE: 'Aktivacija je uspješno izvršena',
			ACTION_ACTIVATE_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom aktivacije',
			ACTION_DEACTIVATE_SUCCESS_MESSAGE: 'Deaktivacija je uspješno izvršena',
			ACTION_DEACTIVATE_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom deaktivacije',
			ACTION_SAVE_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom spremanje podataka',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Spremanje podataka je uspješno izvršeno',
			ACTION_DELETE_SUCCESS_MESSAGE: 'Zapis uspješno izbrisan',
			ACTION_DELETE_FAILURE_MESSAGE: 'Brisanje zapisa nije uspjelo',
			ACTION_LOAD_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom učitavanje podatka',
			ACTION_SEND_EMAIL_SUCCESS_MESSAGE: 'E-mail uspješno poslan',
			ACTION_SEND_EMAIL_FAILURE_MESSAGE: 'Slanje e-maila nije uspjelo',
			ACTION_SEND_EMAIL_HTTP_STATUS_202_MESSAGE: 'Slanje e-maila nije uspjelo jer nisu pronađeni prihvatljivi primatelji',
			ACTION_IMPORT_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom uvoza podataka',
			ACTION_IMPORT_SUCCESS_MESSAGE: 'Uvoz podataka je uspješan izvršen',
			ACTION_EXPORT_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom izvoza podataka',
			ACTION_EXPORT_SUCCESS_MESSAGE: 'Izvoz podataka je uspješno izvršen',
			ACTION_GFI_SYNC_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom GFI sinkronizacije',
			ACTION_GFI_SYNC_SUCCESS_MESSAGE: 'GFI sinkronizacija je uspješno izvršena',
			
			DIALOG_DELETE_HEADER: 'Izbriši zapis',
			DIALOG_DELETE_MESSAGE: 'Da li doista želite izbrisati zapis?',
			
			DIALOG_GFI_SYNC_HEADER: 'GFI sinkronizacija',
			DIALOG_GFI_SYNC_HEADER2: 'Rezultati GFI sinkronizacije',
			DIALOG_GFI_SYNC_MESSAGE: 'Jeste li sigurni da želite pokrenuti ažuriranje profila SVIH korisnika koji su registrirani putem e-zabaPS?<br><br><small><u>Napomena</u>: Pokretanje ažuriranja će promijeniti sve podatke u Profilu poduzeća SVIH korisnika, koji su registrirani e-zabaPS sustavom, u najnovije podatke kojima Banka raspolaže.</small>',
			DIALOG_GFI_SYNC_MESSAGE2: 'Jeste li sigurni da želite pokrenuti ažuriranje profila ovog korisnika?<br><br><small><u>Napomena</u>: Pokretanje ažuriranja će promijeniti sve podatke u Profilu poduzeća korisnika u najnovije podatke kojima Banka raspolaže.</small>',
			DIALOG_GFI_SYNC_MESSAGE3: 'GFI sinkronizacija je provedena za ukupno {{cntTotal}} korisnika, od čega je bila uspješna u {{cntOK}}, a neuspješna u {{cntNOK}} slučajeva.',
			DIALOG_GFI_SYNC_MESSAGE4: 'Sinkronizacija nije uspjela za sljedeće korisnike:',
			
			POSTSCRIPT_CODE: 'Jedinstveni identifikator',
			POSTSCRIPT_ITEM_POSITION: 'Vrijednost mora biti između 1 i 1000',
			
			VALIDATION_FAILED_HEADER: 'Neuspješna validacija',
			
			ERR_MSG_001: 'Stavka poduzeća s meta tagom {{metatag}} nije pronađena',
			ERR_MSG_002: 'Meta tag {{metatag}} pronađen u više od jedne stavke poduzeća',
			
			UI_GRID_LOADING_DATA: 'Učitavam podatake...',
			UI_GRID_NO_DATA: 'Nema podataka',
			
			PLACEHOLDER_VALUE: 'Vrijednost',
			PLACEHOLDER_URL: 'URL',
			
			ACCESS_DENIED_HEADER: 'Pristup odbijen',
			ACCESS_DENIED_MESSAGE: 'Nemate ovlaštenja za pristup odabranom resursu',
			
			DUALLIST_FILTER_CLEAR: 'Poništi',
			DUALLIST_FILTER_PLACEHOLDER: 'Filter',
			DUALLIST_MOVE_ALL: 'Premjesti sve',
			DUALLIST_MOVE_SELECTED: 'Premjesti odabrane',
			DUALLIST_REMOVE_ALL: 'Ukloni sve',
			DUALLIST_REMOVE_SELECTED: 'Ukloni odabrane',
			DUALLIST_SELECTED_LIST_LABEL: 'Dodijeljena',
			DUALLIST_UNSELECTED_LIST_LABEL: 'Nedodijeljena',
			DUALLIST_ACCESS_NOT_ALLOWED_LIST_LABEL: 'Pristup nije dopušten',
			DUALLIST_ACCESS_ALLOWED_LIST_LABEL: 'Pristup dopušten',
			DUALLIST_INFO_ALL: 'Prikazujem ukupno {0}',
			DUALLIST_INFO_EMPTY: 'Lista prazna',
			DUALLIST_INFO_FILTERED: '<span class="label label-warning">Filtered</span> {0} from {1}',
			
			DASHBOARD_USERS: 'Korisnici',
			DASHBOARD_TENDERS: 'Natječaji',
			DASHBOARD_VISITS: 'Posjeti',
			DASHBOARD_PROJECTS: 'Projekti',
			DASHBOARD_TOTAL: 'Ukupno',
			DASHBOARD_UNIQUE: 'Jedinstveni',
			DASHBOARD_ACTIVE: 'Aktivni',
			DASHBOARD_INACTIVE: 'Neaktivni',
			DASHBOARD_CHART_DIMENSION: 'Dimenzija',
			DASHBOARD_CHART_PERIOD: 'Period',
			DASHBOARD_REGISTRATION_TYPE_INTERNAL: 'Registrirani kroz MojEUfond',
			DASHBOARD_REGISTRATION_TYPE_EXTERNAL: 'Registrirani kroz e-ZaBa-u',
			DASHBOARD_TOTAL_VALUE: 'Ukupna vrijednost investicija',
			DASHBOARD_AVERAGE_VALUE: 'Prosječna vrijednost investicije',
		});
	
	$translateProvider.preferredLanguage("hr");
};

angular.module('FundFinder').config(config);

function config($translateProvider) {
	$translateProvider
		
		// ===========================================================================
		//	C R O A T I A N
		// ===========================================================================
		.translations('hr', {
			LANGUAGE: 'Jezik',
			ENGLISH: 'Engleski',
			CROATIAN: 'Hrvatski',
			
			LOGOUT: 'Odjava',
			SETTINGS: 'Postavke',
			
			HEADER_MAIN: 'Fund Finder',
			HEADER_PERSONAL_DATA: 'Osobni podaci',
			HEADER_EMAIL: 'E-mail',
			HEADER_CHANGE_PASSWORD: 'Promjena zaporke',
			HEADER_PROJECT_ADD: 'Dodaj projekt',
			HEADER_PROJECT_EDIT: 'Uredi projekt',
			HEADER_PROJECTS_4_TENDER: 'Projekti za koje je ovaj natječaj prikladan',
			
			HEADER_DASHBOARD_ARTICLE: 'Najnoviji članci',
			HEADER_DASHBOARD_TENDERS: 'Najnoviji natječaji',
			HEADER_DASHBOARD_TENDERS_BADGE: 'Objavljeni u zadnjih 30 dana',
			HEADER_DASHBOARD_TENDERS_CHART: 'Broj objavljenih natječaja',
			HEADER_DASHBOARD_TENDERS_CHART_BADGE: 'Zadnjih 12 mjeseci',
			HEADER_DASHBOARD_TENDERS1_CNT: 'Natječaji',
			HEADER_DASHBOARD_TENDERS1_CNT_BADGE: 'Ukupno',
			HEADER_DASHBOARD_TENDERS2_CNT: 'Otvoreni natječaji',
			HEADER_DASHBOARD_TENDERS2_CNT_BADGE: 'Ukupno',
			HEADER_DASHBOARD_TENDERS3_CNT: 'Natječaji za vas',
			HEADER_DASHBOARD_TENDERS3_CNT_BADGE: 'Ukupno',
			HEADER_DASHBOARD_PROJECTS_CNT: 'Vaši projekti',
			HEADER_DASHBOARD_PROJECTS_CNT_BADGE: 'Ukupno',
			HEADER_DASHBOARD_ARTICLES_CNT: 'Članci',
			HEADER_DASHBOARD_ARTICLES_CNT_BADGE: 'Ukupno',
			HEADER_DASHBOARD_PROFILE_CNT: 'Popunjenost profila',
			HEADER_DASHBOARD_PROFILE_CNT_BADGE: 'Popunjenost',
			
			MENU_TENDERS: 'Moji natječaji',
			MENU_PROJECTS: 'Moji projekti',
			MENU_ARTICLES: 'Članci',
			MENU_COMPANY: 'Profil tvrtke',
			MENU_CONTACT: 'Kontaktirajte nas',
			MENU_DASHBOARD: 'Naslovnica',
			
			BUTTON_SAVE: 'Spremi',
			BUTTON_BACK: 'Nazad',
			BUTTON_ADD: 'Dodaj',
			BUTTON_YES: 'Da',
			BUTTON_NO: 'Ne',
			BUTTON_SEND: 'Send',
			BUTTON_DISABLE: 'Deaktiviraj',
			BUTTON_ENABLE: 'Aktiviraj',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom učitavanja podataka',
			ACTION_SAVE_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom spremanja podataka',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Podaci uspješno spremljeni',
			ACTION_DELETE_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom brisanja zapisa',
			ACTION_DELETE_SUCCESS_MESSAGE: 'Zapis uspješno obrisan',
			ACTION_SEND_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom slanja poruke',
			ACTION_SEND_SUCCESS_MESSAGE: 'Poruka uspješno poslana',
			ACTION_DEACTIVATE_FAILURE_MESSAGE: 'Došlo je do pogreške prilikom deaktivacije profila',
			
			LABEL_LAST_MODIFIED: 'Zadnja promjena',
			LABEL_READ_MORE: 'Pročitajte više',
			LABEL_COMPANY_NAME: 'Naziv tvrtke',
			LABEL_COMPANY_CODE: 'OIB tvrtke',
			LABEL_FIRST_NAME: 'Ime',
			LABEL_LAST_NAME: 'Prezime',
			LABEL_EMAIL: 'Email',
			LABEL_EMAIL2: 'Email (sekundarni)',
			LABEL_NEW_PASSWORD: 'Nova zaporka',
			LABEL_CONFIRM_PASSWORD: 'Potvrdi zaporku',
			LABEL_NAME: 'Naziv',
			LABEL_DESCRIPTION: 'Opis',
			LABEL_INVESTMENT: 'Investicija',
			LABEL_CONTACT_TOPIC: 'Tema',
			LABEL_CONTACT_TYPE: 'Vrsta upita',
			LABEL_CONTACT_CHANNEL: 'Želim da me kontaktirate',
			LABEL_CONTACT_DATA: 'Vaši podaci za kontakt',
			LABEL_CONTACT_PERSON: 'Ime i prezime',
			LABEL_CONTACT_EMAIL: 'E-mail',
			LABEL_CONTACT_PHONE: 'Telefon',
			LABEL_CONTACT_TEXT: 'Tekst',
			LABEL_NO_MATCHING_TENDERS: 'Nije pronađen niti jedan natječaj koji odgovara vašem profilu',
			
			TOOLTIP_DELETE: 'Obriši',
			
			TENDER_STATE_OPEN: 'OTVOREN',
			TENDER_STATE_PENDING: 'U NAJAVI',
			
			DLG_DELETE_HDR: 'Obriši projekt',
			DLG_DELETE_MSG: 'Da li doista želite obrisati projekt?',
			
			DLG_DEACTIVATE_HDR: 'Deaktivacija profila',
			DLG_DEACTIVATE_MSG: 'Deaktivacijom profila biti ćete automatski odjavljeni te više nećete moći pristupiti Fund Finder aplikaciji. Za ponovnu aktivaciju molimo kontaktirajte službu za korisnike.<p><p>Da li stvarno želite deaktivirati vaš profil?',
			
			INCOMPLETE_PROFILE_HDR: 'Nepotpun profil tvrtke',
			INCOMPLETE_PROFILE_MSG: 'Molimo popunite sve obavezne stavke u profilu vaše tvrtke.',
			
			CHOSEN_SINGLE_PLACEHOLDER: 'Molimo odaberite...',
			CHOSEN_MULTIPLE_PLACEHOLDER: 'Molimo odaberite...',
			CHOSEN_NO_RESULT_PLACEHOLDER: 'No results match',
		});
	
	$translateProvider.preferredLanguage("hr");
};

angular.module('FundFinder').config(config);

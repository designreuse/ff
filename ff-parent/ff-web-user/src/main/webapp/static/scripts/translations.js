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
			HEADER_EMAIL: 'Email',
			HEADER_CHANGE_PASSWORD: 'Promjena zaporka',
			HEADER_PROJECT_ADD: 'Dodaj projekt',
			HEADER_PROJECT_EDIT: 'Uredi projekt',
			HEADER_PROJECTS_4_TENDER: 'Projekti za koje je ovaj natječaj prikladan',
			
			HEADER_DASHBOARD_TENDERS: 'Natječaji',
			HEADER_DASHBOARD_TENDERS_BADGE: 'Objavljeni u zadnjih 30 dana',
			HEADER_DASHBOARD_TENDERS_CHART: 'Broj objavljenih natječaja',
			HEADER_DASHBOARD_TENDERS_CHART_BADGE: 'Zadnjih 12 mjeseci',
			
			MENU_TENDERS: 'Natječaji',
			MENU_PROJECTS: 'Projekti',
			MENU_ARTICLES: 'Članci',
			MENU_COMPANY: 'Kompanija',
			MENU_CONTACT: 'Kontaktirajte nas',
			MENU_DASHBOARD: 'Dashboard',
			
			BUTTON_SAVE: 'Spremi',
			BUTTON_BACK: 'Nazad',
			BUTTON_ADD: 'Dodaj',
			BUTTON_YES: 'Yes',
			BUTTON_NO: 'No',
			BUTTON_SEND: 'Send',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Loading data failed',
			ACTION_SAVE_FAILURE_MESSAGE: 'Saving data failed',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Saving data successful',
			ACTION_DELETE_FAILURE_MESSAGE: 'Deleting record failed',
			ACTION_DELETE_SUCCESS_MESSAGE: 'Record successfully deleted',
			ACTION_SEND_FAILURE_MESSAGE: 'Sending contact message failed',
			ACTION_SEND_SUCCESS_MESSAGE: 'Contact message successfully sent',
			
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_READ_MORE: 'Pročitajte više',
			LABEL_COMPANY_NAME: 'Naziv tvrtke',
			LABEL_COMPANY_CODE: 'OIB tvrtke',
			LABEL_FIRST_NAME: 'Ime',
			LABEL_LAST_NAME: 'Prezime',
			LABEL_EMAIL: 'Email',
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
			
			TOOLTIP_DELETE: 'Obriši',
			
			DLG_DELETE_HDR: 'Delete',
			DLG_DELETE_MSG: 'Do you really want to delete projekt?',
			
			INCOMPLETE_PROFILE_HDR: 'Nepotpun profil kompanije',
			INCOMPLETE_PROFILE_MSG: 'Please fill out all mandatory items in your company profile.',
		});
	
	$translateProvider.preferredLanguage("hr");
};

angular.module('FundFinder').config(config);

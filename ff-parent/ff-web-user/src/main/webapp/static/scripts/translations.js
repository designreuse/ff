function config($translateProvider) {
	$translateProvider
		
		// ===========================================================================
		//	E N G L I S H
		// ===========================================================================
		.translations('en', {
			LANGUAGE: 'Language',
			ENGLISH: 'English',
			CROATIAN: 'Croatian',
			
			LOGOUT: 'Logout',
			SETTINGS: 'Settings',
			
			HEADER_MAIN: 'Fund Finder',
			HEADER_PERSONAL_DATA: 'Personal data',
			HEADER_EMAIL: 'Email',
			HEADER_CHANGE_PASSWORD: 'Change password',
			HEADER_PROJECT_ADD: 'Add project',
			HEADER_PROJECT_EDIT: 'Edit project',
			HEADER_PROJECTS_4_TENDER: 'Projects that are match for this tender',
			
			MENU_TENDERS: 'Tenders',
			MENU_PROJECTS: 'Projects',
			MENU_ARTICLES: 'Articles',
			MENU_COMPANY: 'Company',
			MENU_CONTACT: 'Contact us',
			
			BUTTON_SAVE: 'Save',
			BUTTON_BACK: 'Back',
			BUTTON_ADD: 'Add',
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
			LABEL_READ_MORE: 'Read more',
			LABEL_COMPANY_NAME: 'Company name',
			LABEL_COMPANY_CODE: 'Company identification code',
			LABEL_FIRST_NAME: 'First name',
			LABEL_LAST_NAME: 'Last name',
			LABEL_EMAIL: 'Email',
			LABEL_NEW_PASSWORD: 'New password',
			LABEL_CONFIRM_PASSWORD: 'Confirm password',
			LABEL_NAME: 'Name',
			LABEL_DESCRIPTION: 'Description',
			LABEL_INVESTMENT: 'Investment',
			LABEL_CONTACT_TOPIC: 'Topic',
			LABEL_CONTACT_TYPE: 'Type',
			LABEL_CONTACT_CHANNEL: 'Communication channel',
			LABEL_CONTACT_DATA: 'Contact data',
			LABEL_CONTACT_PERSON: 'First and last name',
			LABEL_CONTACT_EMAIL: 'E-mail',
			LABEL_CONTACT_PHONE: 'Phone',
			LABEL_CONTACT_TEXT: 'Text',
			
			TOOLTIP_DELETE: 'Delete',
			
			DLG_DELETE_HDR: 'Delete',
			DLG_DELETE_MSG: 'Do you really want to delete project?',
			
			INCOMPLETE_PROFILE_HDR: 'Incomplete company profile',
			INCOMPLETE_PROFILE_MSG: 'Please fill out all mandatory items in your company profile.',
		})
	
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
			
			MENU_TENDERS: 'Natječaji',
			MENU_PROJECTS: 'Projekti',
			MENU_ARTICLES: 'Članci',
			MENU_COMPANY: 'Kompanija',
			MENU_CONTACT: 'Kontaktirajte nas',
			
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

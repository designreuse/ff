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
			HEADER_INVESTMENT_ADD: 'Add investment',
			HEADER_INVESTMENT_EDIT: 'Edit investment',
			
			MENU_TENDERS: 'Tenders',
			MENU_INVESTMENTS: 'Investments',
			MENU_ARTICLES: 'Articles',
			MENU_COMPANY: 'Company',
			
			BUTTON_SAVE: 'Save',
			BUTTON_BACK: 'Back',
			BUTTON_ADD: 'Add',
			BUTTON_YES: 'Yes',
			BUTTON_NO: 'No',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Loading data failed',
			ACTION_SAVE_FAILURE_MESSAGE: 'Saving data failed',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Saving data successful',
			ACTION_DELETE_FAILURE_MESSAGE: 'Deleting record failed',
			ACTION_DELETE_SUCCESS_MESSAGE: 'Record successfully deleted',
			
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_READ_MORE: 'Read more',
			LABEL_COMPANY_NAME: 'Name',
			LABEL_COMPANY_CODE: 'Identification code',
			LABEL_FIRST_NAME: 'First name',
			LABEL_LAST_NAME: 'Last name',
			LABEL_EMAIL: 'Email',
			LABEL_NEW_PASSWORD: 'New password',
			LABEL_CONFIRM_PASSWORD: 'Confirm password',
			LABEL_NAME: 'Name',
			LABEL_DESCRIPTION: 'Description',
			LABEL_TYPE: 'Type',
			
			TOOLTIP_DELETE: 'Delete',
			
			DLG_DELETE_HDR: 'Delete',
			DLG_DELETE_MSG: 'Do you really want to delete investment?',
			
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
			HEADER_INVESTMENT_ADD: 'Dodaj investiciju',
			HEADER_INVESTMENT_EDIT: 'Uredi investiciju',
			
			MENU_TENDERS: 'Natječaji',
			MENU_INVESTMENTS: 'Investicije',
			MENU_ARTICLES: 'Članci',
			MENU_COMPANY: 'Kompanija',
			
			BUTTON_SAVE: 'Spremi',
			BUTTON_BACK: 'Nazad',
			BUTTON_ADD: 'Dodaj',
			BUTTON_YES: 'Yes',
			BUTTON_NO: 'No',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Loading data failed',
			ACTION_SAVE_FAILURE_MESSAGE: 'Saving data failed',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Saving data successful',
			ACTION_DELETE_FAILURE_MESSAGE: 'Deleting record failed',
			ACTION_DELETE_SUCCESS_MESSAGE: 'Record successfully deleted',
			
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_READ_MORE: 'Pročitajte više',
			LABEL_COMPANY_NAME: 'Name',
			LABEL_COMPANY_CODE: 'Identification code',
			LABEL_FIRST_NAME: 'Ime',
			LABEL_LAST_NAME: 'Prezime',
			LABEL_EMAIL: 'Email',
			LABEL_NEW_PASSWORD: 'Nova zaporka',
			LABEL_CONFIRM_PASSWORD: 'Potvrdi zaporku',
			LABEL_NAME: 'Naziv',
			LABEL_DESCRIPTION: 'Opis',
			LABEL_TYPE: 'Tip',
			
			TOOLTIP_DELETE: 'Obriši',
			
			DLG_DELETE_HDR: 'Delete',
			DLG_DELETE_MSG: 'Do you really want to delete investment?',
			
			INCOMPLETE_PROFILE_HDR: 'Nepotpun profil kompanije',
			INCOMPLETE_PROFILE_MSG: 'Please fill out all mandatory items in your company profile.',
		});
	
	$translateProvider.preferredLanguage("en");
};

angular.module('FundFinder').config(config);

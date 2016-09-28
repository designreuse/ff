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
			
			HEADER_MAIN: 'Fund Finder',
			
			MENU_TENDERS: 'Tenders',
			MENU_INVESTMENTS: 'Investments',
			MENU_ARTICLES: 'Articles',
			MENU_COMPANY: 'Company',
			
			BUTTON_SAVE: 'Save',
			BUTTON_BACK: 'Back',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Loading data failed',
			ACTION_SAVE_FAILURE_MESSAGE: 'Saving data failed',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Saving data successful',
			
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_READ_MORE: 'Read more',
			LABEL_COMPANY_NAME: 'Name',
			LABEL_COMPANY_CODE: 'Identification code',
		})
	
		// ===========================================================================
		//	C R O A T I A N
		// ===========================================================================
		.translations('hr', {
			LANGUAGE: 'Jezik',
			ENGLISH: 'Engleski',
			CROATIAN: 'Hrvatski',
			
			LOGOUT: 'Odjava',
			
			HEADER_MAIN: 'Fund Finder',
			
			MENU_TENDERS: 'Natječaji',
			MENU_INVESTMENTS: 'Investicije',
			MENU_ARTICLES: 'Članci',
			MENU_COMPANY: 'Kompanija',
			
			BUTTON_SAVE: 'Spremi',
			BUTTON_BACK: 'Nazad',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Loading data failed',
			ACTION_SAVE_FAILURE_MESSAGE: 'Saving data failed',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Saving data successful',
			
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_READ_MORE: 'Pročitajte više',
			LABEL_COMPANY_NAME: 'Name',
			LABEL_COMPANY_CODE: 'Identification code',
		});
	
	$translateProvider.preferredLanguage("en");
};

angular.module('FundFinder').config(config);

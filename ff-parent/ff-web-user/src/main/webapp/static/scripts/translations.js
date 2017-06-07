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
			REGISTRATION: 'Registracija',
			
			HEADER_MAIN: 'MojEUfond',
			HEADER_PERSONAL_DATA: 'Osobni podaci',
			HEADER_EMAIL: 'E-mail',
			HEADER_CHANGE_PASSWORD: 'Promjena zaporke',
			HEADER_PROJECT_ADD: 'Dodaj projekt',
			HEADER_PROJECT_EDIT: 'Uredi projekt',
			HEADER_PROJECTS_4_TENDER: 'Projekti za koje je ovaj natječaj prikladan',
			
			HEADER_DASHBOARD_ARTICLE: 'Najnoviji članci',
			HEADER_DASHBOARD_TENDERS: 'Najnoviji natječaji',
			HEADER_DASHBOARD_TENDERS_CHART: 'Broj i ukupna vrijednost objavljenih natječaja',
			HEADER_DASHBOARD_TENDERS1_CNT: 'Natječaji',
			HEADER_DASHBOARD_TENDERS2_CNT: 'Otvoreni natječaji',
			HEADER_DASHBOARD_TENDERS3_CNT: 'Natječaji za vas',
			HEADER_DASHBOARD_PROJECTS_CNT: 'Vaši projekti',
			HEADER_DASHBOARD_ARTICLES_CNT: 'Članci',
			HEADER_DASHBOARD_PROFILE_CNT: 'Popunjenost profila',
			
			HEADER_DASHBOARD_CHART_X_AXIS: 'Broj natječaja',
			HEADER_DASHBOARD_CHART_Y_AXIS: 'Ukupna vrijednost natječaja',
			
			MENU_TENDERS: 'Moji natječaji',
			MENU_PROJECTS: 'Moji projekti',
			MENU_ARTICLES: 'Članci',
			MENU_COMPANY: 'Profil poduzeća',
			MENU_CONTACT: 'Dogovorite sastanak',
			MENU_DASHBOARD: 'Naslovnica',
			
			BUTTON_SAVE: 'Spremi',
			BUTTON_BACK: 'Nazad',
			BUTTON_ADD: 'Dodaj',
			BUTTON_YES: 'Da',
			BUTTON_NO: 'Ne',
			BUTTON_SEND: 'Pošalji',
			BUTTON_DISABLE: 'Deaktiviraj',
			BUTTON_ENABLE: 'Aktiviraj',
			BUTTON_REGISTER: 'Registriraj',
			BUTTON_CLOSE_AND_LOGOUT: 'Zatvori i odjavi se',
			
			BUTTON_SYNC_DATA: 'Koristiti se podacima iz sustava Banke',
			BUTTON_SYNC_DATA_ALWAYS: 'Ažuriraj podatke sad i ubuduće',
			BUTTON_SYNC_DATA_NOT: 'Koristiti se podacima koje ste sami unijeli',
			
			BUTTON_DATE_CURRENT: 'Danas',
			BUTTON_DATE_CLEAR: 'Izbriši',
			BUTTON_DATE_CLOSE: 'Zatvori',
			
			ACTION_LOAD_FAILURE_MESSAGE: 'Došlo je do greške prilikom učitavanja podataka',
			ACTION_SAVE_FAILURE_MESSAGE: 'Došlo je do greške prilikom spremanja podataka',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Podaci uspješno spremljeni',
			ACTION_DELETE_FAILURE_MESSAGE: 'Došlo je do greške prilikom brisanja zapisa',
			ACTION_DELETE_SUCCESS_MESSAGE: 'Zapis uspješno izbrisan',
			ACTION_SEND_FAILURE_MESSAGE: 'Došlo je do greške prilikom slanja poruke',
			ACTION_SEND_SUCCESS_MESSAGE: 'Poruka uspješno poslana',
			ACTION_DEACTIVATE_FAILURE_MESSAGE: 'Došlo je do greške prilikom deaktivacije profila',
			ACTION_SYNC_DATA_SUCCESS_MESSAGE: 'Podaci o poduzeću su uspješno ažurirani',
			ACTION_SYNC_DATA_ALWAYS_SUCCESS_MESSAGE: 'Podaci o poduzeći su uspješno ažurirani te će i ubuduće biti automatski ažurirani',
			ACTION_FAILURE_MESSAGE: 'Došlo je do greške prilikom izvršavanja akcije',
			
			LABEL_LAST_MODIFIED: 'Zadnja promjena',
			LABEL_READ_MORE: 'Pročitajte više',
			LABEL_COMPANY_NAME: 'Naziv poduzeća',
			LABEL_COMPANY_CODE: 'OIB poduzeća',
			LABEL_FIRST_NAME: 'Ime',
			LABEL_LAST_NAME: 'Prezime',
			LABEL_EMAIL: 'E-mail (e-mail adresa potrebna je kako bi Vam pristizale informacije o prihvatljivim natječajima)',
			LABEL_EMAIL2: 'Dodatni e-mail (unesite ako želite obavijesti o novim prilikama dobivati i na ovu e-mail adresu)',
			LABEL_PASSWORD: 'Zaporka',
			LABEL_NEW_PASSWORD: 'Nova zaporka',
			LABEL_CONFIRM_PASSWORD: 'Potvrdi zaporku',
			LABEL_NAME: 'Naziv',
			LABEL_DESCRIPTION: 'Opis',
			LABEL_INVESTMENT: 'Investicija',
			LABEL_CONTACT_DATA: 'Vaši podaci za kontakt',
			LABEL_CONTACT_PERSON: 'Ime i prezime',
			LABEL_CONTACT_EMAIL: 'E-mail',
			LABEL_CONTACT_PHONE: 'Telefon',
			LABEL_CONTACT_TEXT: 'Poruka',
			LABEL_CONTACT_LOCATION: 'Odaberite poslovnicu za sastanak',
			LABEL_CONTACT_SUBHEADER: 'Zatražite termin',
			LABEL_CONTACT_DISCLAIMER: 'Zagrebačka banka osobne podatke unesene u donji obrazac obrađuje u svrhu ugovaranja sastanka u poslovnici. Radi pripreme bankara za sastanak u poslovnici molimo unesite OIB.',
			LABEL_NO_MATCHING_TENDERS: 'Nije pronađen nijedan natječaj koji odgovara Vašem profilu.',
			LABEL_SYNC_DATA: 'Prilikom prijave automatski ažurirajte podatke koje ste unijeli s podacima koje ima Banka.',
			LABEL_SYNC_DATA_WARNING: 'Podaci o poduzeću koje ste unijeli razlikuju se od podataka dostupnih Banci. Želite li:',
			LABEL_SHOW_SYNC_DATA_WARNING: 'Nemoj više prikazivati ovo upozorenje',
			LABEL_NO_PROJECTS: 'Trenutno nema unešenih projekata',
			LABEL_NO_ARTICLES: 'Trenutno nema unešenih članaka',
			
			TOOLTIP_DELETE: 'Izbriši',
			TOOLTIP_SYNC_DATA: 'koristiti se podacima iz sustava Banke',
			TOOLTIP_SYNC_DATA_ALWAYS: 'Ažuriraj podatke o tvrtki koje ste unijeli sa podacima koje ima banka sad i ubuduće',
			TOOLTIP_SYNC_DATA_NOT: 'koristiti se podacima koje ste sami unijeli',
			
			VALIDATION_FAILED_HEADER: 'Neuspješna validacija',
			
			TENDER_STATE_OPEN: 'OTVOREN',
			TENDER_STATE_PENDING: 'U NAJAVI',
			
			DLG_DELETE_HDR: 'Izbriši projekt',
			DLG_DELETE_MSG: 'Želite li izbrisati projekt?',
			
			DLG_REGISTRATION_HDR: 'Registracija',
			DLG_REGISTRATION_MSG: 'Vaš zahtijev za registracijom je uspješno zaprimljen.<p>Za nekoliko minuta primiti ćete konfirmacijski e-mail. Molimo kliknite na poveznicu iz e-maila da dovršite registraciju. Nakon toga ćete se moći prijaviti u MojEUfond.',

			DLG_DEACTIVATE_HDR: 'Deaktivacija profila',
			DLG_DEACTIVATE_MSG: 'Deaktivacijom profila bit ćete automatski odjavljeni te više nećete moći pristupiti aplikaciji MojEUfond. Za ponovnu aktivaciju molimo kontaktirajte službu za korisnike.<p><p>Želite li deaktivirati  profil?',
			
			DLG_NO_EMAIL_HDR: 'Nepotpun profil korisnika',
			DLG_NO_EMAIL_MSG: 'Kroz nekoliko sekundi biti ćete automatski preusmjereni na Postavke gdje možete popuniti sve obavezne stavke vašeg korisničkog profila.',
			
			INCOMPLETE_PROFILE_HDR: 'Nepotpun profil poduzeća',
			INCOMPLETE_PROFILE_MSG: 'Molimo popunite sve obavezne stavke u profilu poduzeća.',
			
			MSG_REGISTRATION_CONFLICT_USER: 'Korisnik sa upisanim e-mailom već postoji u sustavu.',
			MSG_REGISTRATION_CONFLICT_COMPANY: 'Poduzeća sa upisanim OIB-om već postoji u sustavu.',
			MSG_REGISTRATION_ERROR: 'Nažalost, došlo je do pogreške.<p>Molimo kontaktirajte službu za korisnike.',
			MSG_COMPANY_PROFILE_MISSING: 'Registracija nije moguća jer profil poduzeća nije popunjen',
			
			CHOSEN_SINGLE_PLACEHOLDER: 'Molimo odaberite...',
			CHOSEN_MULTIPLE_PLACEHOLDER: 'Molimo odaberite...',
			CHOSEN_NO_RESULT_PLACEHOLDER: 'Nema rezultata koji odgovaraju',
			CHOSEN_SINGLE_PLACEHOLDER_CONTACT: 'Upišite grad ili mjesto',
			CHOSEN_NO_RESULT_PLACEHOLDER_CONTACT: 'Nema rezultata koji odgovaraju',
		});
	
	$translateProvider.preferredLanguage("hr");
};

angular.module('FundFinder').config(config);

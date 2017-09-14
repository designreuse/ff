CREATE TABLE `config_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` longtext,
  `description` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `config_param` (`id`, `name`, `value`, `description`)
VALUES
    (1, 'mail_sender', 'admin@fundfinder.com', 'Adresa pošiljaoca svih e-mailova koji se šalju kroz Fund Finder.'),
	(2, 'gfi_sync_email_subject', 'MojEUfond - Ažuriranje podataka o poduzeću', 'Naslov e-mail poruke koja se šalje kod GFI sinkronizacije.'),
    (3, 'gfi_sync_report_email_subject', 'MojEUfond - Zapisnik o GFI sinkronizaciji', 'Naslov e-mail poruke koja se šalje nakon GFI sinkronizacije, te sadrži izvještaj o učinjenom.'),
    (4, 'gfi_sync_report_email_to', 'kincade1975@gmail.com', 'E-mail adrese odvojene pipe (|) separatorom na koje se šalje izvještaj o GFI sinkronizaciji.'),
    (5, 'contact_email_subject', 'MojEUfond - Kontakt', 'Naslov e-mail poruke koja se šalje sa stranice Dogovorite sastanak.'),
    (6, 'contact_email_to', 'kincade1975@gmail.com', 'E-mail adresa na koju se šalju sve poruke sa stranice Dogovorite sastanak. Moguće je definirati više adresa u kojem slučaju je delimiter pipe znak (npr. address1@test.net|address2@test.net).'),
    (7, 'sendgrid_enabled', 'false', ''),
    (8, 'sendgrid_apikey', '', ''),
    (9, 'test_mode', 'false', '');

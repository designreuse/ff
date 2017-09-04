<!DOCTYPE html>

<html>
    <body>
        <div>
        	Dana ${startDate} u ${startTime} upućen je zahtjev za sinkronizaciju podataka iz godišnjih financijskih izvješća za korisnike aplikacije MojEUfond koji su ujedno i klijenti e-zabaPS.<p>
       	 
       	 	GFI sinkronizacija zatražena je za ukupno ${cntUsers} korisnika.<p>

			GFI sinkronizacija uspješno je provedena za ${cntUsersOk} korisnika.<p>
			
			GFI sinkronizacija nije uspješno provedena za niže navedenih ${cntUsersNok} korisnika:
            <ul>
                <#list users as user>
                    <li>${user}</li>
                </#list>
            </ul>
        </div>
    </body>
</html>
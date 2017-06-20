<!DOCTYPE html>

<html>
    <body>
        <div>
            Poštovani, e-mail poruka sljedećeg sadržaja:
            
            <blockquote style="background: #f0f0f0; border-left: 10px solid #ccc; padding: 5px;">
                ${originalEmailText}
            </blockquote>
            
            upravo je poslana korisnicima navedenim u nastavku: 
            <ul>
                <#list users as user>
                    <li>${user}</li>
                </#list>
            </ul>
        </div>
    </body>
</html>
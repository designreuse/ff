<!DOCTYPE html>

<html>
    <body>
        <div>
            An e-mail with the following content:
            
            <blockquote style="background: #f0f0f0; border-left: 10px solid #ccc; padding: 5px;">
                ${originalEmailText}
            </blockquote>
            
            Has just been sent to these users: 
            <ul>
                <#list users as user>
                    <li>${user}</li>
                </#list>
            </ul>
        </div>
    </body>
</html>
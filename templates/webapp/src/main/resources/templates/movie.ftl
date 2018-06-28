<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
    <title>${title}</title>
</head>
<body>
<h1>${title}
    <small>(${year?string("0000")})</small>
</h1>
<h4> ${genres?join(" | ")} </h4>
<br>
${synopsis}
<br>
<br>
Actors:
<ul>
    <#list actors as actor>
        <li>
            <b>${actor.firstName} ${actor.lastName}</b>
            <small>(${actor.birthDate})</small>
        </li>
    </#list>
</ul>

</body>
</html>
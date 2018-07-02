<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
    <title>Notes</title>
</head>
<body>
<h1>Notes</h1>

<div class="container">
    <#list notes as note>
        <div class="note">
            <small>${note.formattedTimestamp}</small>
            <br>
            <b>${note.text}</b>
        </div>
    </#list>
</div>

<br>

<form action="notes" method="post">
    <input type="text" name="text" placeholder="New note">
    <input type="submit" value="Submit">
</form>

</body>
</html>
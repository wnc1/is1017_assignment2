<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <%
    session.setAttribute("serverMessage", null);
    session.invalidate();
 %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.css" type="text/css" media="screen" title="no title" charset="utf-8">
        <title>wnc1 Bank</title>
    </head>
    <body>
        <div class="container">
            <h1>Goodbye </h1>
            <p class="bg-success">You have successfully logged out.<p>
        </div>
    </body>
</html>

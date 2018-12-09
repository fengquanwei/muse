<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<form id="login" action="/login/check">
    username: <input type="text" name="username" value="${username}"/><br/>
    password: <input type="password" name="password"/><br/>
    <input type="submit" value="Login">
</form>

</body>
</html>

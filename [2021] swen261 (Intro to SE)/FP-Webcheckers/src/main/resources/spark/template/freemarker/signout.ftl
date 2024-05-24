<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
  <link rel="shortcut icon" href="../img/flaming_icon.png" />
</head>

<body>
<div class="page">

  <img src="../img/flaming_logo.gif">
    <h1>  | ${title}</h1>

  <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
      <#include "message.ftl" />

    <div class="form-group">
      <form action="./signin" method = "POST">
        <br/>
        <input name = "name" />
        <br/><br/>
        <button type = "submit"> Ok </button>
      </form>
    </div>



  </div>

</div>
</body>

</html>

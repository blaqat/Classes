<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers  | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
  <link rel="shortcut icon" href="../img/flaming_icon.png" />
</head>

<body>
<div class="page">
  <div class="FLAMING">
      <img src="../img/flaming_logo.gif">
  </div>
  <h1>  | ${title}</h1>
  
  

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <!-- TODO: future content on the Home:
            list players online
            to start games,
            spectating active games,
            or replay archived games
    -->
    <#if currentUser??>
    <h3>Your stats</h3>
        ${currentUser.getName()} ${currentUser.getAchievementsAsString()} - ${currentUser.getStats()}
    </#if>

    <h2>Players Online</h2>
    <#if playerLobby??>
      <#if currentUser??>
      <#if lobbySize==0> [No available players]
      <#else>
        <ul>
        <#list playerLobby as plr>
          <#if !plr.equals(currentUser)>
            <li>
              <form action="./game" method = "POST">
                    <button type = "submit" name = "anything" value = "${plr.getName()}"> ${plr.getName()} </button> 
                    ${plr.getStats()}
                    ${plr.getAchievementsAsString()}
              </form>
            </li>
          </#if>
        </#list>
        </ul>
      </#if>
      <#else>
          <p>Currently there <#if totalPlayers!=1>are<#else>is</#if> <b>${totalPlayers}</b> player<#if totalPlayers!=1>s</#if> online!</p>
      </#if>
    </#if>

    <#if currentUser??>
    <h2>Tournaments</h2>
    <form action="./tournament" method = "POST">
            <button type = "submit" name = "anything" value = "Create">
            Create Tournament
             </button>
          </form>
    <h3>Invitations</h3>
    <#if inviteCount == 0> [No invitations]
          <#else>
            <ul>
            <#list inviteList as trn>
                <li>
                  <form action="./tournament" method = "GET">
                    <button type = "submit" name = "leader" value = "${trn.getLeader().getName()}"> ${trn.getLeader().getName()}'s Tournament </button>
                  </form>
                </li>
            </#list>
            </ul>
          </#if>
          </#if>
  </div>

</div>
</body>

</html>

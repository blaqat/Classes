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

    <div class="FLAMING">
      <img src="../img/flaming_logo.gif">
    </div>
    <h1>  | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

<#if isOver>

 <form action="./leaveTournament" method = "POST">
  <button type = "submit" name = "anything" value = "Create">
       Leave Tournament
  </button>
  </form>

<h2> ${winner.getName()} HAS WON!! </h2>

<#else>

<#if inProgress>

<h2>Bracket</h2>
  <#if bracket??>
     <ul>
      <#list bracket as plr>
          <li>
            {${plr[0].getName()} VS. ${plr[1].getName()}}
          </li>
      </#list>
    </ul>
  <#else>
  [Waiting for players to finish...]
  <#if areGames>
  <h3> In progress </h3>
       <ul>
        <#list gamesInProgress as game>
            <li>
              {${game.redPlayer.getName()} VS. ${game.whitePlayer.getName()}}
            </li>
        </#list>
      </ul>
    </#if>
  </#if>

  <#if inProgress>
    <h2>Waiting Lobby</h2>
  <#else>
    <h2>Participants</h2>
  </#if>
          <ul>
            <#list participants as plr>
                <li>
                  ${plr.getName()}  ${plr.getStats()}
                </li>
            </#list>
          </ul>


<#else>
  <form action="./leaveTournament" method = "POST">
  <button type = "submit" name = "anything" value = "Create">
       Leave Tournament
  </button>
  </form>

<#if isLeader>
<#if ready>
    <form action="./startTournament" method = "POST">
      <button type = "submit" name = "anything" value = "Create">
           <h2>START TOURNAMENT(${lobbyCount}/${maxPlayers})</h2>
      </button>
      </form>
<#else>
    <#if lobbyCount < maxPlayers>
     <h5>Waiting for players... (${lobbyCount}/${maxPlayers})<h5>
     <#else>
     <h5>Too many players (${lobbyCount}/${maxPlayers})<h5>
    </#if>
</#if>
</#if>


  <h2>Bracket</h2>
  <#if bracket??>
  <#if isLeader>
  <form action="./settings" method = "POST">
    <button type = "submit" name = "change" value = "seed">
         Seed
    </button>
    </form>
    </#if>
     <ul>
      <#list bracket as plr>
          <li>
            {${plr[0].getName()} VS. ${plr[1].getName()}}
          </li>
      </#list>
    </ul>
  <#else>
  [Not enough players to seed]
  </#if>

  <h2>Participants</h2>
    <#if isLeader>
    <form action="./opt" method = "POST">
        <button type = "submit" name = "anything" value = "Create">
            <#if leaderIsPlaying>
               Sit out
              <#else>
                Rejoin
             </#if>
          </button>
      </form>
      </#if>
          <ul>
            <#list participants as plr>
                <li>
                  ${plr.getName()}  ${plr.getStats()}
                </li>
            </#list>
          </ul>

  <#if isLeader>
  <h2>Invite Players</h2>
  <#if lobbySize==0> [No available players]
        <#else>
          <ul>
          <#list playerLobby as plr>
            <#if !plr.equals(currentUser)>
              <li>
                <form action="./invite" method = "POST">
                      <button type = "submit" name = "invitee" value = "${plr.getName()}"> ${plr.getName()} </button> ${plr.getStats()}
                </form>
              </li>
            </#if>
          </#list>
          </ul>
        </#if>
  </#if>

  <#if isLeader>
     <h3>Settings</h3>
     <h4>Maxmimum Players</h4>
     <form action="./settings" method = "POST">

     <button type = "submit" name = "change" value = "decrease">
            Decrease
       </button>
       ${maxPlayers}
       <button type = "submit" name = "change" value = "increase">
                 Increase
       </button>
       </form>
    </#if>
 </#if>
 </#if>

</div>

</body>

</html>

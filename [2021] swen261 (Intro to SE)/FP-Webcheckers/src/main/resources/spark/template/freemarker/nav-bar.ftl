 <div class="navigation">
  <#if currentUser??>
   <#if !currentUser.gameSession??>
    <a href="/">my home</a> |
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">sign out [${currentUser.getName()}]</a>
    </form>
    <#else>
        ${currentUser.getName()} - IN GAME
    </#if>
  <#else>
    <a href="/signin">sign in</a>
  </#if>
 </div>

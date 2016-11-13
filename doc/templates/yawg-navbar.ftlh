<#--
  Template for the navigation bar block.
-->

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="${yawg.rootRelativeUrl}/index.html">Yawg</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <@navbarItem
               name="home"
               url="index.html"
               title="Home" />
            <@navbarItem
               name="download"
               url="Download/index.html"
               title="Download" />
            <@navbarItem
               name="documentation"
               url="Documentation/index.html"
               title="Documentation" />
            <@navbarItem
               name="developers"
               url="Developers/index.html"
               title="Developers" />

            <#if (isProduction!"false") != "true">
              <li style="float: right">
                <a href="#"><span style="background:red; color:white; padding:0.5em;">TEST ENVIRONMENT</span></a>
              </li>
            </#if>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>


<#-- Renderer for one item in the navigation bar. -->
<#macro navbarItem name url title>
  <#if (navbarSelectedItem!"") == name>
    <li class="active"><a href="${yawg.rootRelativeUrl}/${url}">${title}</a></li>
  <#else>
    <li><a href="${yawg.rootRelativeUrl}/${url}">${title}</a></li>
  </#if>
</#macro>

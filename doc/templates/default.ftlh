<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>${title}</title>

    <link
       rel="stylesheet"
       href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
       integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
       crossorigin="anonymous" />

    <link rel="stylesheet" href="${rootRelativeUrl}/yawg-site.css" />

  </head>

  <body>

    <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-79615115-1', 'auto');
  ga('send', 'pageview');
    </script>

    <div class="yawg-body">

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="${rootRelativeUrl}/index.html">Yawg</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <@navbarItem
               name="home"
               url="index.html"
               title="Home" />
            <li><a href="https://github.com/jorgefranconunes/yawg/releases" target="_blank">Download</a></li>
            <@navbarItem
               name="documentation"
               url="Documentation/UserGuide.html"
               title="Documentation" />
            <@navbarItem
               name="developers"
               url="Developers/index.html"
               title="Developers" />
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container yawg-content">

      <h1>${title}</h1>
      ${body?no_esc}

      <footer class="yawg-footer">
        <p>&copy;Jorge Nunes 2016
        <p>Baked with ${productName} ${version}</p>
      </footer>

    </div>
    </div>
  </body>
</html>

<#macro navbarItem name url title>
  <#if (navbarSelectedItem!"") == name>
    <li class="active"><a href="${rootRelativeUrl}/${url}">${title}</a></li>
  <#else>
    <li><a href="${rootRelativeUrl}/${url}">${title}</a></li>
  </#if>
</#macro>

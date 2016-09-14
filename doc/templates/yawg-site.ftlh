<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>${yawg.title}</title>

    <link
       rel="stylesheet"
       href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
       integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
       crossorigin="anonymous" />

    <link
       rel="stylesheet"
       href="${yawg.rootRelativeUrl}/yawg-site.css?v=${yawg.bakeId}" />
    
    <script
       type="text/javascript"
       async
       src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML">
    </script>

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

      <#include "yawg-navbar.ftlh">

      <div class="container yawg-content">

        <#include "yawg-breadcrumbs.ftlh">

        <h1>${yawg.title}</h1>
        ${yawg.body?no_esc}

        <#include "yawg-footer.ftlh">

      </div>
      
    </div>
    
  </body>
  
</html>

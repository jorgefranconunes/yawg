<!DOCTYPE html>
<#--
  Template for pages with no title in the Yawg site.

  This is actually very similar to the "yawg-site.ftlh" template, only
  without the H1 title. This is intended to be used for HTML documents
  that render the page title in their own way. One example is the site
  home page.
-->
<html>

  <#include "yawg-head.ftlh">

  <body>

    <div class="yawg-body">

      <#include "yawg-navbar.ftlh">

      <div class="container yawg-content">

        <#include "yawg-breadcrumbs.ftlh">

        ${yawg.body?no_esc}

        <#include "yawg-footer.ftlh">

      </div>

    </div>

  </body>

</html>

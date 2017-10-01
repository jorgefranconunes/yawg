<!DOCTYPE html>
<#--
  Template for regular pages in the Yawg site.
-->
<html>

  <#include "yawg-head.ftlh">

  <body>

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

<#ftl output_format="HTML">
<!DOCTYPE html>
<html>
  <head>
    <#if yawg.title ??>
    <title>${yawg.title}</title>
    </#if>
  </head>
  <body>
    <#if yawg.title ??>
    <h1>${yawg.title}</h1>
    </#if>

    ${yawg.body?no_esc}

    <footer>
      <p>Baked with Yawg</p>
    </footer>
  </body>
</html>

<#ftl output_format="HTML">
<!DOCTYPE html>
<html>
  <head>
    <#if title ??>
    <title>${title}</title>
    </#if>
  </head>
  <body>
    <#if title ??>
    <h1>${title}</h1>
    </#if>

    ${body?no_esc}

    <footer>
      <p>Baked with Yawg</p>
    </footer>
  </body>
</html>

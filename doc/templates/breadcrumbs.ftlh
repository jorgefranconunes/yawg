<#if (breadcrumbList.items?size) gt 1 >
  <#list breadcrumbList.items>
    <ol class="breadcrumb">
      <#items as breadcrumb>
        <#if pageUrl != breadcrumb.url >
          <li><a href="${rootRelativeUrl}/${breadcrumb.url}">${breadcrumb.title}</a></li>
        <#else>
          <li>${breadcrumb.title}</li>
        </#if>
      </#items>
    </ol>
  </#list>
</#if>

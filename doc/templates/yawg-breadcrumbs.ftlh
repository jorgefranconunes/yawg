<#-- Template for the bread crumb bar. -->

<#if (breadcrumbs.items?size) gt 1 >
  <!#-- The breadcrumb bar is only visible if there is more than
        one bread crumb on this page.-->

  <#list breadcrumbs.items>
    <ol class="breadcrumb">
      <#items as breadcrumb>
        <#if yawg.pageUrl != breadcrumb.url >
          <li><a href="${yawg.rootRelativeUrl}/${breadcrumb.url}">${breadcrumb.title}</a></li>
        </#if>
      </#items>
    </ol>
  </#list>
</#if>

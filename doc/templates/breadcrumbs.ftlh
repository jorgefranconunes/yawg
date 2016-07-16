<#if (breadcrumbList.items?size) gt 1 >
        <#list breadcrumbList.items>
          <ol class="breadcrumb">
            <#items as breadcrumb>
              <li><a href="${rootRelativeUrl}/${breadcrumb.url}">${breadcrumb.title}</a></li>
            </#items>
          </ol>
        </#list>
</#if>

<#setting url_escaping_charset="UTF-8">
<div class="alert alert-info">
<h4 style="word-wrap: break-word;"><a href="${basePath}/eks-pub/display/${sectionContent.id}">${sectionContent.title}</a></h4>
<p>${sectionContent.dublincore.description}</p>
<div class="row-fluid">
	<div>
<img style="display: block;margin-left: auto;margin-right: auto;max-width: 140px;"
		src="${Context.getBaseURL()}/nuxeo/nxthumb/default/${sectionContent.id}/blobholder:0/"
		alt="(no thumbnail)"} ></img>
	</div>
</div>
<!--
<div class="row-fluid">
	<div class="span4">
		<div class="label label-info">Nature</div> 
	</div>
	<div class="span8">
		${sectionContent.dublincore.nature}
	</div>
</div>
-->
<p></p>
<div class="row-fluid">
	
	<div class="span4">
		<div class="label label-info">Published</div> 
	</div>
	<div class="span8">
		${sectionContent.dublincore.issued?date}
	</div> 
</div>
<!--
<#if sectionContent.file.content.filename!="">
	<div class="row-fluid">
		<a href="${This.getDownloadURL(sectionContent)}">${sectionContent.file.content.filename}</a>
	</div>
</#if>
-->
</div>
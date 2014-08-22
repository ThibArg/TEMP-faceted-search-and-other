<@extends src="base.ftl">

<!-- Hiding the login name, but kipping the thing so I remember how it's done ;-) -->
<@block name="content">

<div style="margin: 10px 10px 10px 10px">

<#if Context.getProperty("found") = false>
<p></p>
<h1>Document not found</h1>
<p>We are sorry, but the document you requested cannot be found anymore.</p>
<p>Please, contact your EKS representative for more information.</p>
<#else>

<table>
<tr>
<td>
	<#if Context.getProperty("thumbnailUrl") == "">
		<img src="${skinPath}/img/doc_icon.png" alt="(no thumbnail" style="max-width: 256px; height: auto;"}></img>
	<#else>
		<img src="${Context.getProperty("thumbnailUrl")}" alt="(no thumbnail)"} style="max-width: 128px; height: auto;"></img>
	</#if>
</td>
<td width="25px"></td>
<td><span style="font-size:x-large; font-weight:bold">${Context.getProperty("title")}</span><br/><i>${Context.getProperty("type")}</i></td>
</tr>
</table>
<p></p>
<!--
<div class="oneTitle">
INFO
</div>
-->
<hr>
<table>
	<tr>
		<td class="infoLabel">Event:</td>
		<td class="infoValue">${Context.getProperty("event")}</td>
	</tr>
	<tr>
		<td class="infoLabel">Category:</td>
		<td class="infoValue">${Context.getProperty("category")}</td>
	</tr>
	<tr>
		<td class="infoLabel">Width:</td>
		<td class="infoValue">${Context.getProperty("width")}</td>
	</tr>
	<tr>
		<td class="infoLabel">Height:</td>
		<td class="infoValue">${Context.getProperty("height")}</td>
	</tr>
</table>

<#if Context.getProperty("pictureUrl") != "">
<hr>
<p></p>
<#if Context.getProperty("downloadUrl") != "">
	<div class="row-fluid">
		<a href="${Context.getProperty("downloadUrl")}">Download file</a>
	</div>
</#if>
<p></p>
<img src="${Context.getProperty("pictureUrl")}" alt="(no image available)"} style="max-width: 1000x; height: auto;"></img>
</#if>

</div>
</#if>
</@block>
</@extends>

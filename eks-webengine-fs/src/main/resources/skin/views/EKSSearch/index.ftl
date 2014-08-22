<@extends src="base.ftl">
<@block name="header_scripts"><script src="${skinPath}/scripts/main.js"></script></@block>
<@block name="header"><!--You signed in as ${Context.principal}--></@block>

<@block name="content">

<div id="allContent" style="margin: 10px 10px 10px 10px">
	<div id="queryObjects" class="queryObjects">
		<div class="queryTopic" id="events">
			<div class="queryTopicTitle">Events</div>
			<div class="" id="cb_Events">
			</div>
		</div>
		<div class="queryTopic" id="categories">
			<div class="queryTopicTitle">Categories</div>
			<div class="" id="cb_Categories">
			</div>
		</div>
		<div class="queryTopic" id="keywords">
			<div class="queryTopicTitle">Keywords</div>
			<div class="" id="cb_Keywords">
			</div>
		</div>
		<div class="queryTopic" id="widthRange">
			<div class="queryTopicTitle">Width</div>
			<div class="" id="cb_WidthRange">
			</div>
		</div>
		<div class="queryTopic" id="heightRange">
			<div class="queryTopicTitle">Height</div>
			<div class="" id="cb_HeightRange">
			</div>
		</div>
	</div>
	<div id="queryResults" class="queryResults">
	et resultats
	</div>
</div>

</@block>
</@extends>

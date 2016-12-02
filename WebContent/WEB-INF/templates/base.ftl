<#macro title>
	Chat
</#macro>

<#macro stylesheet>
</#macro>

<#macro headscript>
</#macro>

<#macro header>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="navbar-header"> 
        <!-- <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button> -->
        <a id="navbar_header" class="navbar-brand" href="/WebChat/chat">Anonymous Chatting</a>
    </div>
    <div class="collapse navbar-collapse" id="navbar-collapse">
    </div>
</div>
</#macro>

<#macro body>
</#macro>

<#macro footer>
</#macro>

<#macro public_js >
<script src="js/base.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    var msg = {'password_alert': 'The password must be a string, and can not contain spaces, and the length is greater than 6!'};
    baseInit('Chat', 'http', 'en_US', msg);
});
</script>
</#macro>

<#macro javascript>
</#macro>

<#macro display_page>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="/WebChat/img/chat.png">
		<title>
			<@title/>
		</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="bootstrap/css/bootstrap.css" rel="stylesheet" />
	    <link href="bootstrap/css/bootstrap-theme.css" rel="stylesheet" />
	    <link href="css/bootstrap.css" rel="stylesheet" >
	    <link href="css/template.css" rel="stylesheet" />
	    <script src="jquery/jquery-2.0.3.js") }}"></script>
	    <script src="bootstrap/js/bootstrap.js"></script>
	    <@stylesheet/>
		<@headscript/>
	</head>
	<body>
		<@header/>
		<div id="body_container" class="container" >
			<div id="body_row" class="row">
				<@body/>
				<@footer/>
	        </div>
		</div>
		<@public_js/>
		<@javascript/>
	</body>
</html>
</#macro>
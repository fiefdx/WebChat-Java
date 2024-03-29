<#include "../base.ftl">

<#macro title>
	Chat - 0.0.1
</#macro>

<#macro stylesheet>
</#macro>

<#macro body>
<div id="row_container" class="row">
    <div id="rooms_div">
        <div id="rooms_list" class="row">
            <div id="rooms_list_ul" class="list-group">
                <!-- rooms list -->
            </div>
        </div>
    </div>
    <div id="chat_area" class="" role="form">
        <form id="chat_form" class="container col-xs-12">
            <div id="div_chat_text" class="col-xs-12">
                <div id="chat_text" class="col-xs-12">
                    <div id="chat_content" class="list-group col-xs-12">
                    </div>
                </div>
            </div>
            <div id="button_div" class="row col-xs-12">
                <div id="msg_text_div" class="col-xs-12">
                    <textarea id="msg_text" class="form-control col-xs-12" name="msg_content" type="text">
                    </textarea>
                </div>
                <div class="pull-left">
                    <input name="nick_name" type="text" class="form-control" placeholder = "Nick Name" id="nick_name_input">
                </div>
                <div class="pull-right">
                    <button id="send_button" type="button" class="btn btn-primary col-xs-2" title="Ctrl + Enter">
                        <i class="glyphicon glyphicon-comment"></i>
                        &nbsp;Send
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<div id="change_room_modal" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="changeRoomModalLabel" aria-hidden="true">
    <form id="form_change_room" class="form-horizontal">
        <div id="dialog_modal" class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h3 id="changeRoomModalLabel" class="modal-title">Warning</h3>
                </div>
                <div class="modal-body">
                    <span class="col-xs-12">Do you want to change room?</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default" type="button" data-dismiss="modal" aria-hidden="true">No</button>
                    <button id="change_room" class="btn btn-primary" type="button" data-dismiss="modal" aria-hidden="true">Yes</button>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="empty_message_modal" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="emptyMessageModalLabel" aria-hidden="true">
    <form id="empty_message" class="form-horizontal">
        <div id="dialog_modal" class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h3 id="createNoteAllModalLabel" class="modal-title">Warning</h3>
                </div>
                <div class="modal-body">
                    <span class="col-xs-12">Empty message can't be sent!</span>
                </div>
                <div class="modal-footer">
                </div>
            </div>
        </div>
    </form>
</div>
<div id="first_select_modal" class="modal fade" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="firstSelectModalLabel" aria-hidden="true">
    <form id="form_first_select" class="form-horizontal">
        <div id="dialog_modal" class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h3 id="createNoteAllModalLabel" class="modal-title">Warning</h3>
                </div>
                <div class="modal-body">
                    <span class="col-xs-12">You must first select a room!</span>
                </div>
                <div class="modal-footer">
                </div>
            </div>
        </div>
    </form>
</div>
</#macro>

<#macro javascript>
<link href="bootstrap/css/prettify.css" rel="stylesheet" >
<link href="css/chat.css" rel="stylesheet" >
<script src="bootstrap/js/prettify.js"></script>
<script src="js/chat.js"></script>
<!--<script src="js/code.js"></script>-->
<script src="js/tea.js"></script>
<script src="js/spark-md5.js"></script>
<script src="js/long.js"></script>
<script type="text/javascript">
window.onload=window.onresize=function(){
    $(document).ready(function(){
        var window_height = $(window).height();
        var window_width = $(window).width();
        $("div#rooms_list").height(window_height - 74);
        $("div#chat_text").height(window_height - 248);
        $("textarea#msg_text").height(116);
        $("div#chat_area").width(window_width - 220);
    });
}
    
$(document).ready(function(){
    var window_height = $(window).height();
    var window_width = $(window).width();

    $("div#rooms_list").height(window_height - 74);
    $("div#chat_text").height(window_height - 248);
    $("textarea#msg_text").height(116);
    $("div#chat_area").width(window_width - 220);
    
    chatInit('http', 'en_US', 100);
});

</script>
</#macro>

<@display_page/>
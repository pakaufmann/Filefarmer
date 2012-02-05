$(function() {
	$("#archiveTree").jstree({
		"dnd" : {
			"drag_finish" : function(data) {
				var archive = $(data.r).attr("id").substring(8);
				var tableRow = $(data.o).parent().get(0);
				var table = $("#fileTable").dataTable();
				var fileId = table.fnGetData(tableRow)[0];
				
				$.getJSON("/archives/moveFile/" + fileId + "/" + archive, function(data) {
					table.fnDraw();
				});
				
				$("#fileSettings").accordion("activate", false);
			}
		},
		"themes" : {
			"theme" : "default",
			"dots" : false
		},
		"plugins" : ["themes", "ui", "html_data", "dnd"]
	}).bind("select_node.jstree", function(event, data) {
		$("#fileSettings").accordion("activate", false);
		$("#archiveTree").jstree("toggle_node", data.rslt.obj);
		$("#fileTable").dataTable().fnDraw();
		$("#file img").attr("src", "");
	}).bind("loaded.jstree", function(event, data) {
		$("#archiveTree").jstree("select_node", ".selected");
	});
	
	$("#main").tabs();
	$("#fileSettings").accordion({
		autoHeight: false,
		collapsible: true
	}).accordion("activate", false);
	
	$("#fileTable tr").live("click", function(event, ui) {
		$("#fileTable .selected").removeClass("selected");
		var table = $("#fileTable").dataTable();
		var data = table.fnGetData(this);
		
		$("#file img").attr("src", "/archives/picture/" + data[0] + ".png");
		
		$.getJSON("/archives/file/" + data[0], function(data) {
			if(data.success) {
				$("#fileParams .fileName").html(data.fileName);
				
				//set metadata
				$("#fileParams .creator").html(data.creator);
				$("#fileParams .insertDate").html(data.insertDate);
				
				//set fulltext
				$("#fileParams .fullText").html(data.fullText);
				
				//set fields
				$("#fileParams .additionalFields").html("");
				
				$(data.fields).each(function() {
					var field = "";
					
					switch(this.fieldType) {
						case "text":
							field = new TextField();
							break;
						case "dropdown":
							field = new DropdownField();
							break;
					}
					
					$("#fileParams .additionalFields").append("<div>" + field.createField(this.id, this.name, this.values, this.value) + "</div>");
					$("#fileSettings").accordion("activate", 0);
				});
			} else {
				$("#fileParams .fileName").text("An error occurred")
			}
		});
		
		$(this).addClass('selected');
	});
	
	$("#fileTable").dataTable({
		"aoColumns" : [{"bSearchable": false, "bVisible": false}, null, null, null],
		"bJQueryUI" : true,
		"bFilter" : false,
		"aLengthMenu" : [[25, 50, 100, 200, -1], [25, 50, 100, 200, "all"]],
		"iDisplayLength" : 25,
		"sScrollY" : "400px",
		"bAutoWidth" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"sAjaxSource" : "/archives/files",
		"fnServerParams" : function( data ) {
			data.push( { "name" : "archive", "value" : $("#archiveTree").jstree("get_selected").attr("id").substring(8) } );
			
			$("#files div.fg-toolbar:last").addClass("fg-toolbar-last");
		},
		"sServerMethod" : "GET",
		"fnDrawCallback" : function() {
			$("#fileTable tr").addClass("jstree-draggable");
		}
	});
});

$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
  return {
    "iStart":         oSettings._iDisplayStart,
    "iEnd":           oSettings.fnDisplayEnd(),
    "iLength":        oSettings._iDisplayLength,
    "iTotal":         oSettings.fnRecordsTotal(),
    "iFilteredTotal": oSettings.fnRecordsDisplay(),
    "iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
    "iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
  };
}
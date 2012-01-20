$(function() {
	$("#archiveTree").jstree({
		"plugins" : "themes, ui",
		"themes" : {
			"theme" : "default",
			"dots" : false
		}
	}).bind("select_node.jstree", function(event, data) {
		var table = $("#fileTable").dataTable();
		var archive = $("#archiveTree").jstree("get_selected").attr("id");
		
		refreshTable("#fileTable", "/archives/files/" + archive.substring(8));
		var archiveId = data.rslt.obj.children("a").attr("id");
		$("#archiveTree").jstree("toggle_node", data.rslt.obj);
	}).bind("loaded.jstree", function(event, data) {
		$("#archiveTree").jstree("select_node", ".selected");
	});
	
	$("#main").tabs();
	
	$("#fileTable").dataTable({
		"bJQueryUI" : true,
		"bFilter" : false,
		"bAutoWidth" : false,
		"bProcessing" : true
	});
})

function refreshTable(tableId, urlData) {
	//Retrieve the new data with $.getJSON. You could use it ajax too
	$.getJSON(urlData, null, function( json ) {
		table = $(tableId).dataTable();
		oSettings = table.fnSettings();

		table.fnClearTable(this);

		for(var i=0; i<json.aaData.length; i++) {
			table.oApi._fnAddData(oSettings, json.aaData[i]);
		}

		oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
		table.fnDraw();
  });
}
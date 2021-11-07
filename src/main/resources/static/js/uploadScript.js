$(document).ready(function() {
	$("#dataUpload").on("submit", function(event){
		event.preventDefault();
		$("#status").show();
        $("#status").removeClass("text-danger");
        $("#status").addClass("text-success");
        $("#status").html("Uploading...");
        var form = $("#dataUpload")[0];
        var formData = new FormData(form);
		$.ajax({
			url: "/file-upload",
			method: "post",
			data: formData,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
			success: function(json, text, request){
				if (json.message==="done") {
				    $("#status").html("Downloading...");
				    var blob = new Blob([json.content], { type: "application/cgs" });
				    var fileName = request.getResponseHeader('download.cgs');
				    if (window.navigator && window.navigator.msSaveOrOpenBlob) { // for IE
				    	window.navigator.msSaveOrOpenBlob(blob, fileName);
				    } else { // for others
					    var url = window.URL.createObjectURL(blob);
					    const a = document.createElement('a');
					    a.style.display = 'none';
					    a.href = url;
					    a.download = "download.cgs";
					    document.body.appendChild(a);
					    a.click();
					    window.URL.revokeObjectURL(url);
					}
				    $("#status").html("Downloaded");
				} else if(json.message==="fileError"){
				    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("File doesn't upload'");
				} else{
				    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
	
	$("#dataDecrypt").on("submit", function(event){
		event.preventDefault();
		$("#status").show();
        $("#status").removeClass("text-danger");
        $("#status").addClass("text-success");
        $("#status").html("Uploading...");
        var form = $("#dataDecrypt")[0];
        var formData = new FormData(form);
		$.ajax({
			url: "/file-decyrpt",
			method: "post",
			data: formData,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
			success: function(json, text, request){
				if (json.message==="done") {
				    $("#status").html("Downloading...");
				    var arrayBuffer = _base64ToArrayBuffer(json.content);
				    var fuc = new Uint8Array(arrayBuffer);
				    var blob = new Blob([fuc], { type: "application/"+json.extention });
				    var fileName = request.getResponseHeader('download.'+json.extention);
				    if (window.navigator && window.navigator.msSaveOrOpenBlob) { // for IE
				    	window.navigator.msSaveOrOpenBlob(blob, fileName);
				    } else { // for others
					    var url = window.URL.createObjectURL(blob);
					    const a = document.createElement('a');
					    a.style.display = 'none';
					    a.href = url;
					    a.download = "download."+json.extention;
					    document.body.appendChild(a);
					    a.click();
					    window.URL.revokeObjectURL(url);
					}
				    $("#status").html("Downloaded");
				} else if(json.message==="fileError"){
				    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("File doesn't upload");
				}  else if(json.message==="extentionError"){
				    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Please upload only .cgs file.");
				} else if(json.message==="passwordNot"){
				    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Password does not matched.");
				} else{
				    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
	
});
function _base64ToArrayBuffer(base64) {
    var binary_string = window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
}
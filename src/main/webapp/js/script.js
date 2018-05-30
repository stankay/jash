$(document).ready(function(e) {
	$("#command").focus();
});

function sendCommand() {
	
	//posli
	var req = $.ajax({
		url: "./ShellCommandServlet",
		data: {
			command: $("#command").val()
		}
	})
	.done(function(msg) {
			//appendni vysledok nad current line
			$('#command-output').html(msg);

			//$("#caseList tr:last").after(row);
	})
	.fail(function(jqXHR, msg) 	{alert(msg.toString());});

	//zresetuj hodnotu komandu
	$("#command").val("");

	//fokusni #command
	$("#command").focus();	
	
	 //$('html, body').animate({
	   //     scrollTop: ($("#bottom").offset().top)
	    //}, 20);	
	
	return false;
}
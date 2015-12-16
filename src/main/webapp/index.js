$(document).ready(function() {
	function getTranslationHistory() {
		var url = 'TranslationHistory';
		$.getJSON(url, function(data) {
			var items = [];
			$.each(data, function(key, val) {
				items.push("<tr><td>"+ val.textInput +"</td><td>"+ val.textTranslated +"</td><td>"+ val.detectedLanguage +"</td><td>"+ val.confidenceDetectedLanguage +"%</td></tr>");
			});
			$("#translationHistoryTbody").html(items.join(""));
		});	
	}
	getTranslationHistory();
	
	
	$('#translateButton').click(function(evt) {
		var url = 'Translation';
		var btn = $('#translateButton');
		
		$("#errorMessageInput").hide();
		$('#languageDetectedDiv').hide();
		
		var text = $('#textareaInput').val();
		if ($.trim(text) == "") {
			showMessageDiv("Please, type your text", "errorMessageInput");
			return;
		}
		
		showLoadingButton(btn);		
	    $.post(url, {text: text})
	    	.done( function(result){
		    	var obj = jQuery.parseJSON(result);
		    	$('#textareaOutput').val(obj.textTranslated);
		    	showLanguageDetected(obj.detectedLanguage, obj.confidenceDetectedLanguage);
		    	showTranslateButton(btn);
		    })
		    .fail( function(xhr, textStatus, errorThrown) {
		    	//API down
		    	showMessageDiv("Error! Could not translate your text. Please, try again", "errorMessageInput");
		    	showTranslateButton(btn);		    	
		    }
		);		
	    
		return true;
	});
	
	$('#listenButton').click(function(evt) {
		$("#errorMessageOutput").hide();
		
		var text = $('#textareaOutput').val();
		if ($.trim(text) == "") {
			showMessageDiv("You have to translate your text first", "errorMessageOutput");
			return;
		}

		var url = 'TextToSpeech?text=' + encodeURIComponent(text);
		var audioResult = $('#audioResult')[0];
		audioResult.src = url;
		audioResult.play();
		
		return true;
	});

	function showLoadingButton(btn) {
		btn.addClass("btn-warning");		
		btn.html('<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Loading...');
	}
	
	function showTranslateButton(btn) {
		btn.removeClass("btn-warning");		
		btn.html('Translate');
	}
	
	function showLanguageDetected(language, confidence) {
		$('#languageDetectedSpan').html(language);
    	$('#languageDetectedConfidenceSpan').html(confidence);
    	$('#languageDetectedDiv').show();
	}
	
	function showMessageDiv(htmlText, divId) {
		$("#" + divId).html(htmlText)
		.show();
	}

});

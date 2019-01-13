$('body').click(function(){

	var doc = document.activeElement.tagName;

	if(doc == "INPUT") {
		$('#searchResult').show();
	}
	else
	{
		$('#searchResult').hide();
	}
	
});


var popup = document.getElementsByClassName("staff-img");
var i;

for (i = 0; i < popup.length; i++) {

	popup[i].addEventListener("mouseover", function() {

		var name = this.nextElementSibling;

		name.className += " active";

		setTimeout(function(){

			name.className = "popup name";

		}, 1000);

	});

}

var acc = document.getElementsByClassName("accordion");
var i;

for (i = 0; i < acc.length; i++) {
	acc[i].addEventListener("click", function() {
		this.classList.toggle("active");
		var panel = this.nextElementSibling;
		if (panel.style.maxHeight){
			panel.style.maxHeight = null;
		} else {
			panel.style.maxHeight = panel.scrollHeight + "px";
		} 
	});
}
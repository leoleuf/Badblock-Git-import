function menu() {
    var x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}

$(document).ready(function () {

    //Check to see if the window is top if not then display button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollToTop').fadeIn();
        } else {
            $('.scrollToTop').fadeOut();
        }
    });

    //Click event to scroll to top
    $('.scrollToTop').click(function () {
        $('html, body').animate({scrollTop: 0}, 800);
        return false;
    })

    //Get players
    // $.get(endpoint + '/api/minecraft/players', function(data, status){
    //     alert("Players: " + data);
    // });

    $.getJSON(endpoint + '/api/minecraft/players', function(data) {
        if (data.now == 0){
            var message = trans.noPlayers
        }
        if (data.now == 1){
            var message = data.now + ' ' + trans.onePlayer
        }
        if (data.now > 1){
            var message = data.now + ' ' + trans.manyPlayers
        }
        $('#players').html(message);
    });
});
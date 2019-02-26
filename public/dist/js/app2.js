$('body').click(function () {

    var doc = document.activeElement.tagName;

    if (doc == "INPUT") {
        $('#searchResult').show();
    }
    else {
        $('#searchResult').hide();
    }

});


var popup = document.getElementsByClassName("staff-img");
var i;

for (i = 0; i < popup.length; i++) {

    popup[i].addEventListener("mouseover", function () {

        var name = this.nextElementSibling;

        name.className += " active";

        setTimeout(function () {

            name.className = "popup name";

        }, 1000);

    });

}

var acc = document.getElementsByClassName("accordion");
var i;

for (i = 0; i < acc.length; i++) {
    acc[i].addEventListener("click", function () {
        this.classList.toggle("active");
        var panel = this.nextElementSibling;
        if (panel.style.maxHeight) {
            panel.style.maxHeight = null;
        } else {
            panel.style.maxHeight = panel.scrollHeight + "px";
        }
    });
}

function players() {
    var trans = {
        'noPlayers': "Aucun joueurs connectés",
        'onePlayer': "joueur",
        'manyPlayers': "joueurs"
    }
    //Get players
    $.getJSON('/api/minecraft/players', function (data) {
        if (data.players.now == 0) {
            var message = trans.noPlayers
        }
        if (data.players.now == 1) {
            var message = data.players.now + ' ' + trans.onePlayer
        }
        if (data.players.now > 1) {
            var message = data.players.now + ' ' + trans.manyPlayers
        }
        $('#players').html(message);

        console.log('%c' + message + " connecté!", 'background: #222; font-size: 2em; color: #9b59b6');

        setTimeout(players, 10000);
    });
}

players();

$('#searchPlayer').keyup(function () {

    var input = document.getElementById("searchPlayer");
    var filter = input.value.toUpperCase();
    var ul = document.getElementById("searchResult");
    var li = ul.getElementsByTagName("li");

    var last = "";

    $.ajax({
        url: '/api/stats/search',
        type: "POST",
        data: {search_player: filter},
        success: function (data) {
            data = JSON.parse(data);
            $("#searchResult").empty();
            for (i = 0; i < data.length; i++) {
                if (data[i] != last) {
                    $("#searchResult").append('<li><a href="/profile/' + data[i]["username"] + '"><img src="https://cdn.badblock.fr/head/' + data[i]["username"] + '/64.png">' + data[i]["username"] + '</a></li>');

                    last = data[i];
                    console.log(data[i]);

                    for (i = 0; i < li.length; i++) {
                        a = li[i].getElementsByTagName("a")[0];
                        if (a.innerHTML.toUpperCase().indexOf(filter) > -1) {
                            li[i].style.display = "block";
                        } else {
                            li[i].style.display = "none";

                        }
                    }
                }
            }
        }
    });

});
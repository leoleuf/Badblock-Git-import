function menu() {
    var x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}

function players() {
    //Get players
    $.getJSON('https://mcapi.us/server/status?ip=' + mcHost, function(data) {
        if (data.players.now == 0){
            var message = trans.noPlayers
        }
        if (data.players.now == 1){
            var message = data.players.now + ' ' + trans.onePlayer
        }
        if (data.players.now > 1){
            var message = data.players.now + ' ' + trans.manyPlayers
        }
        $('#players').html(message);

        console.log('%c' + message + " connecté!", 'background: #222; font-size: 2em; color: #9b59b6');
    });
}

$(document).ready(function () {

    console.log("%cHeyyy!", 'background: #222; font-size: 5em; color: #00A8E7');

    console.log("%cBadblock est fier de te présenter son nouveau site web !", 'background: #222; font-size: 2em; color: #bada55');

    console.log("%cMais pour encore mieux en profiter, et pour la sécurité de votre compte badblock, blabla en fait je fait un texte pour meubler ma vrai pensé : - je sais ce que tu veux faire, tu veux trafiquoter le site pour impréssionner tes amis! LOLOLOLOLOLOL", 'background: #222; color: #bada55');

    console.log("%cBref, le site à été développé par plein de développeurs comme Skript/FeedDev/Fluor (oui, oui le mec à 50 0000 pseudos), Hooki_, et Le_Futuriste <lefuturiste.fr>", 'background: #000; color: #fff');

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

    players();

    /*
    Rechargez son compte
     */

});

//drop downu
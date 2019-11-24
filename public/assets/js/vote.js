$(document).ready(() => {
    var username = $("#username");
    var btn = $(".site-btn");

    /* On trigger quand l'utilisateur appui sur une touche */
    username.on("keydown", () => {
        /* Si ce qu'il entre est supérieur à 1 caractère */
        if(username.val().length > 1){
            /* On enleve la class disabled et le style des boutons */
            btn.removeClass("disabled btn-success");
            /* On ajoute un autre style */
            btn.addClass("btn-default");
        }else{
            /* Sinon on desactive les boutons servers, sites et on remet le style par défaut */
            btn.removeClass("btn-default");
            btn.addClass("disabled");
            $(".servers .btn").addClass("disabled");
        }

    });
    btn.click((e) => {
        /* Si le bouton site n'est pas désactivé */
        if(!$(e.target).hasClass("disabled")){
            /* Quand on clique sur le bouton on lui applique un style et on active ceux des servers */
            $(e.target).removeClass("btn-default");
            $(e.target).addClass("disabled btn-success");
            $(".servers .btn").removeClass("disabled");
        }

    });

});
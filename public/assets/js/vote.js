$(document).ready(() => {
    var username = $("#username");
    var btn = $(".site-btn");
    var getAwardBtn = $("get-award-btn");
    var error = $("#error");

    /* On trigger quand l'utilisateur appui sur une touche */
    username.on('input', function() {
        /* Si ce qu'il entre est supérieur à 1 caractère */
        if(username.val().length > 1){

            $.ajax({

                url: "/api.php",
                method: "POST",
                data: {
                    exists: username.val()
                },
                success: function(response){
                    if(response) {
                        /* On enleve la class disabled et le style des boutons */
                        btn.removeClass("disabled btn-success");
                        /* On ajoute un autre style */
                        btn.addClass("btn-default");
                        error.addClass('hide');
                    }
                    else {
                        error.html("Nous ne vous avons pas trouvé :( <br /> Avez-vous bien marqué votre pseudo ?");
                        error.removeClass('hide');
                    }
                },
                error: function(jqHXR, status, responseCode){
                    $.notify({
                        type: "error"
                    },
                        {
                            message : "Erreur lors du traitement de la requête, merci de contacter un administrateur"
                        });
                }

            });
        }else{
            /* Sinon on desactive les boutons servers, sites et on remet le style par défaut */
            btn.removeClass("btn-default");
            btn.addClass("disabled");
            $(".servers .btn").addClass("disabled");
            error.addClass('hide');
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

    getAwardBtn.click((e) => {

        $.ajax({
            url: "/api.php",
            type: "POST",
            data: {
                username: username.val()
            },
            success: function (response) {
                if(response) {
                    error.addClass('hide');
                    //We will send a command to the remote Minecraft server through an API
                    console.log("Player has voted !");
                }
                else {
                    error.html("<p>On dirait que vous n'avez voté sur aucune plateforme :( <br />" +
                        "Ou vous n'avez pas pu parce que vous êtes trop rapide, réessayez plus tard ! :D");
                    error.removeClass('hide');
                }

            }
        })

    });

});
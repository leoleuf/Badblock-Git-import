function customdqzzqdzz(){
var SDDQmcHost = "eu.badblock.fr"
var SDDQtrans = {
        'noPlayers': "Pas de joueurs",
        'onePlayer': "joueur",
        'manyPlayers': "joueurs"
}
    console.log('SDDQ players loading');
    //Get players
    $.getJSON('https://mcapi.us/server/status?ip=' + SDDQmcHost, function(data) {
        if (data.players.now == 0){
            var message = SDDQtrans.noPlayers
        }
        if (data.players.now == 1){
            var message = data.players.now + ' ' + SDDQtrans.onePlayer
        }
        if (data.players.now > 1){
            var message = data.players.now + ' ' + SDDQtrans.manyPlayers
        }
        $('#players').html(message);

        console.log('players online: ' + message);
    });
}
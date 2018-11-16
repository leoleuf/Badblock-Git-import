@section('title', 'Installation de la méthode JSON - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode JSON pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/json')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Installation JSON
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a> <span class="lnr lnr-arrow-right"></span> <a title="Ajouter mon serveur" href="/add-server">Ajouter mon serveur</a> <span class="lnr lnr-arrow-right"></span> <a title="Installation de la méthode JSON" href="/add-server/json">Installation de la méthode JSON</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Installation de la méthode JSON</h3>
                <div class="row">
                    <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation de la méthode JSON pour vérifier les votes. Si vous ne savez pas comment implémenter cette méthode, utilisez plutôt la <a title="Installation par la méthode TRUE" href="/add-server/true">méthode TRUE</a>.
                            Lorsque vous enverrez une requête à notre API avec la méthode <strong>JSON</strong>, vous receverez une réponse formattée qui vous indiquera le temps avant le prochain vote d'une IP et si cette personne a voté ou non. Cette méthode est utile pour récupérer le temps avant le prochain vote d'un joueur.</p>
                        <blockquote class="generic-blockquote">
                            <strong>Exemple d'implémentation en PHP</strong><br /><br />
                            &lt;?php<br /><br />
                            $SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                            $IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant<br>
                            $SM = "https://serveur-multigames.net/api/$SERVER_ID/?ip=$IP";<br>
                            $result = @file_get_contents($SM);<br>
                            $result = json_decode($result);<br /><br />
                            if ($result->status == "SUCCESS")<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote valide<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;$nextvote = $result->nextvote; // Temps restant possible avant le prochain vote<br />
                            }<br>
                            else<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Déjà voté ou vote non valide<br>
                            }<br><br />
                            ?&gt;<br /><br />
                            <strong>Clés et valeurs retournées</strong><br />
                            <i>ip</i> : toutes les IPv4/IPv6 (exemple: 127.0.0.1)<br />
                            <i>status</i> : SUCCESS (vote effectué) ou WAIT (vote non effectué ou déjà effectué)<br />
                            <i>nextvote</i> : temps en secondes avant le prochain vote (-1 si jamais voté)
                            <br /><br />
                            <strong>Exemple de réponse</strong><br /><br />
                            {<br />
                            &nbsp;&nbsp;&nbsp;&nbsp;"ip": "127.0.0.1",<br />
                            &nbsp;&nbsp;&nbsp;&nbsp;"status": "SUCCESS",<br />
                            &nbsp;&nbsp;&nbsp;&nbsp;"nextvote": 3208<br />
                            }<br /><br />
                            <strong>Autres languages de programmation</strong><br /><br />
                            Vous pouvez également implémenter l'API et effectuer les requêtes dans d'autres langages de programmation, cela fonctionne également et plusieurs bibliothèques pour décoder le JSON existent. Il faut utiliser la requête suivante : <br/>
                            URL : https://serveur-multigames.net/api/<i>nom-de-votre-serveur</i>/?ip=<i>adresse.ip.du.joueur</i>
                        </blockquote>
                    </div>
                </div>
            </div>
            <div class="section-top-border">
            </div>
        </div>
    </div>
@endsection
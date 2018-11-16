@section('title', 'Installation de la méthode TRUE - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode TRUE pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/true')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Installation TRUE
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a> <span class="lnr lnr-arrow-right"></span> <a title="Ajouter mon serveur" href="/add-server">Ajouter mon serveur</a> <span class="lnr lnr-arrow-right"></span> <a title="Installation de la méthode TRUE" href="/add-server/true">Installation de la méthode TRUE</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Installation de la méthode TRUE</h3>
                <div class="row">
                    <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation de la méthode TRUE pour vérifier les votes. C'est la méthode la plus simple à implémenter.<br />
                            Lorsqu'un joueur vote pour votre serveur, vous pourrez simplement vérifier le vote lors de la requête à notre API.</p>
                        <blockquote class="generic-blockquote">
                            <strong>Exemple d'implémentation en PHP</strong><br /><br />
                            &lt;?php<br /><br />
                            $SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                            $IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant<br>
                            $SM = "https://serveur-multigames.net/api/$SERVER_ID/?ip=$IP";<br>
                            $result = @file_get_contents($SM);<br><br />
                            if ($result == true)<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote valide<br>
                            }<br>
                            else<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote non valide<br>
                            }<br><br />
                            ?&gt;<br /><br />
                            <strong>Autres languages de programmation</strong><br /><br />
                            Vous pouvez implémenter l'API et effectuer les requêtes dans d'autres langages de programmation, cela fonctionne également. Il suffit d'envoyer la requête comme demandé.<br />
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
@section('title', 'Mise en place de l\'API pour les Créateurs - Serveur MultiGames')
@section('description', 'Utilisez gratuitement notre API pour vérifier les votes de votre serveur.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/api')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Mise en place API
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Mise en place API" href="/api">Mise en place API</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30" style="display: block;">Mise en place API</h3>
                <div class="row">
                    <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose une API gratuite à utiliser et à mettre en place pour votre serveur. Vous pouvez retrouver le code de votre API après avoir créé votre serveur directement sur le Tableau de Bord. Avec cette API, vous pouvez vérifier si un utilisateur a voté, pour pouvoir donner une récompense, ou simplement récupérer des statistiques sur votre serveur.<br /></p>
                        <blockquote class="generic-blockquote">
                            <strong>Séléctionnez une méthode de vérification des votes</strong><br /><br />
                            <a title="Installation de la méthode TRUE" href="/add-server/true">Méthode TRUE</a> (méthode la plus simple)<br />
                            <a title="Installation de la méthode JSON" href="/add-server/json">Méthode JSON</a> (méthode intermédiaire)<br />
                            <a title="Installation de la méthode CALLBACK" href="/add-server/callback">Méthode CALLBACK</a> (méthode experte)<br />
                            <a title="Installation de la méthode VOTIFIER" href="/add-server/votifier">Méthode VOTIFIER</a> (pour les serveurs Minecraft)<br />
                            <strong></strong><br /><br />
                        </blockquote>
                        <blockquote class="generic-blockquote">
                            <strong>Statistiques : récupérer le nombre de votes</strong><br /><br />
                            Vous pouvez récupérer le nombre de votes directement avec notre API statistiques : <br/><br />
                            &lt;?php<br /><br />
                            $SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                            $SM = "https://serveur-multigames.net/api/$SERVER_ID/stats/votes";<br>
                            $nbvotes = @file_get_contents($SM);<br><br />
                            echo 'Nombre de votes : '.$nbvotes;<br /><br />
                            ?&gt;
                        </blockquote>
                    </div>
                </div>
            </div>
            <div class="section-top-border">
            </div>
        </div>
    </div>
@endsection
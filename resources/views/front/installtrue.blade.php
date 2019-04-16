@section('title', 'Installation de la méthode TRUE - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode TRUE pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/true')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Méthode True</h2>
                        <p class="mb-0">Initiation à la méthode True, simple.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none"><a title="API et Utilisation" href="/api" class="lis-light">API et Utilisation</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">True</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation de la méthode TRUE pour vérifier les votes. C'est la méthode la plus simple à implémenter.<br />
                            Lorsqu'un joueur vote pour votre serveur, vous pourrez simplement vérifier le vote lors de la requête à notre API.</p>
                        <blockquote class="generic-blockquote">
                            <strong>Exemple d'implémentation en PHP</strong><br /><br />
                            <kbd>&nbsp;&lt;?php<br>
                                &nbsp;&nbsp;&nbsp;$SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                                &nbsp;&nbsp;&nbsp;$IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant<br>
                                &nbsp;&nbsp;&nbsp; $SM = "https://serveur-multigames.net/api/$SERVER_ID/?ip=$IP";<br>
                                &nbsp;&nbsp;&nbsp;$result = @file_get_contents($SM);<br>
                                &nbsp;&nbsp;&nbsp;if ($result == true)<br />
                                &nbsp;&nbsp;&nbsp;{<br>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Vote valide<br>
                                &nbsp;&nbsp;&nbsp;}<br>
                                &nbsp;&nbsp;&nbsp;else<br>
                                &nbsp;&nbsp;&nbsp;{<br>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Vote non valide<br>
                                &nbsp;&nbsp;&nbsp;}<br>
                                &nbsp;?&gt;</kbd><br /><br />
                            <strong>Autres languages de programmation</strong><br /><br />
                            Vous pouvez implémenter l'API et effectuer les requêtes dans d'autres langages de programmation, cela fonctionne également. Il suffit d'envoyer la requête comme demandé.<br />
                            URL : https://serveur-multigames.net/api/<i>nom-de-votre-serveur</i>/?ip=<i>adresse.ip.du.joueur</i>
                        </blockquote>
                    </div>
                </div>
        </div>
    </section>
@endsection
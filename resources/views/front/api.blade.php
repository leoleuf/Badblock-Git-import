@section('title', 'Mise en place de l\'API pour les Créateurs - Serveur MultiGames')
@section('description', 'Utilisez gratuitement notre API pour vérifier les votes de votre serveur.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/api')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>API et Utilisation</h2>
                        <p class="mb-0">Mise en place de l'API</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">API et Utilisation</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose une API gratuite à utiliser et à mettre en place pour votre serveur. Vous pouvez retrouver le code de votre API après avoir créé votre serveur directement sur le Tableau de Bord. Avec cette API, vous pouvez vérifier si un utilisateur a voté, pour pouvoir donner une récompense, ou simplement récupérer des statistiques sur votre serveur.<br /></p>
                        <br />
                        <blockquote class="generic-blockquote">
                            <h6>Séléctionnez une méthode de vérification des votes</h6>
                            <a title="Instllation méthode true" class="btn btn-primary btn-default" href="/add-server/true">Méthode True (méthode la plus simple)</a><br /><br />
                            <a title="Instllation méthode json" class="btn btn-info btn-default" href="/add-server/json">Méthode Json (méthode intermédiaire)</a><br /><br />
                            <a title="Instllation méthode Callback" class="btn btn-warning btn-default" href="/add-server/callback">Méthode Callback (méthode experte)</a><br /><br />
                            <a title="Instllation méthode Votifier" class="btn btn-success btn-default" href="/add-server/votifier">Méthode Votifier (par plugin Minecraft)</a>
                            <strong></strong>
                        </blockquote><br />
                        <blockquote class="generic-blockquote">
                            <h6>Statistiques : récupérer le nombre de votes</h6>
                            Vous pouvez récupérer le nombre de votes directement avec notre API statistiques : <br/><br />
                            <kbd>&nbsp;&lt;?php&nbsp;<br />
                            &nbsp;&nbsp;&nbsp;$SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                            &nbsp;&nbsp;&nbsp;$SM = "https://serveur-multigames.net/api/$SERVER_ID/stats/votes";<br>
                            &nbsp;&nbsp;&nbsp;$nbvotes = @file_get_contents($SM);<br>
                            &nbsp;&nbsp;&nbsp;echo 'Nombre de votes : '.$nbvotes;<br />
                                &nbsp;?&gt;</kbd>
                        </blockquote><br />
                    </div>
                </div>
        </div>
    </section>

@endsection
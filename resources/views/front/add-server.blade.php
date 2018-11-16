@section('title', 'Ajouter mon serveur Gratuitement dans la Liste - Serveur MultiGames')
@section('description', 'Ajoutez gratuitement votre serveur dans la liste. Ayez accès à des statistiques et donnez de la visibilité à votre serveur sans donner un sous.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Ajouter votre serveur
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Ajouter votre serveur MultiGaming" href="/add-server">Ajouter votre serveur</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Ajoutez votre serveur gratuitement</h3><br />
                <div class="row">
                    <div class="col-md-3 text-center">
                        <a title="Agrandir la photo du Tableau de bord" href="/img/tableau_de_bord.png" target="_blank"><img alt="Tableau de bord Serveur MultiGames" src="/img/tableau_de_bord.png" class="img-fluid"></a>
                        <i>Cliquez sur l'image pour agrandir</i>
                    </div>
                    <div class="col-md-9 mt-sm-20">
                        <p>Serveur-Multigames.net vous propose d'ajouter <strong>gratuitement</strong> votre serveur dans le but de <strong>rendre visible votre projet</strong>.<br />
                            Grâce à un classement et un système de vote <strong>complètement gratuit</strong>, les joueurs peuvent découvrir de <strong>nouveaux serveurs</strong> mais également
                            <strong>voter</strong> pour son serveur favori afin de gagner des récompenses.
                            <br /><br />Une <strong><a title="Exemple API Serveur MultiGames" href="/api">API gratuite</a></strong> est à disposition des créateurs pour vérifier
                            les votes de son serveur et ainsi donner des récompenses, contrairement à d'autres classements où la validité d'un vote est incertaine.
                            Nous nous efforçons à <strong>rendre plus facile les votes</strong> en utilisant les dernières technologies de <strong>Captcha invisibles</strong>.
                            Les votes sont vérifiés et contrôlés régulièrement afin de <strong>conserver une neutralité</strong> que d'autres classements peinent à avoir.<br /><br />
                            <a title="Ajouter mon serveur sur Serveur MultiGames" class="genric-btn danger" href="/dashboard/add-server" style="font-size: 14px;">Ajouter mon serveur depuis le Tableau de Bord</a>
                            &nbsp;
                            <a title="Exemple API Serveur MultiGames" class="genric-btn info" href="/api" style="font-size: 14px;">Exemple d'utilisation de l'API pour les Créateurs</a>
                        </p>
                    </div>
                </div>
                <br />
                <hr />
                <br />
                <h3 class="mb-30">Fonctionnalités des créateurs</h3><br />
                <div class="row">
                    <div class="col-md-3 text-center">
                        <a title="Agrandir les statistiques" href="/img/statistiques.png" target="_blank"><img alt="Statistiques d'un Serveur MultiGames" src="/img/statistiques.png" class="img-fluid"></a>
                        <i>Cliquez sur l'image pour agrandir</i>
                    </div>
                    <div class="col-md-9 mt-sm-20">
                        <p>Les créateurs peuvent suivre en temps réel les votes et les clics sortants vers leur serveur pour plusieurs tranches (par heure, par jour...). L'ajout et la modification de son serveur est possible à tout moment grâce au <a title="Tableau de Bord de Serveur MultiGames" href="/dashboard">Tableau de bord</a>.</p>
                    </div>
                </div>
                <br />
                <hr />
                <br />
                <h3 class="mb-30">Vérification des votes par quatre moyens différents</h3><br />
                <div class="row">
                    <div class="col-md-3 text-center">
                        <a title="Agrandir la liste des choix" href="/img/verification-votes.png" target="_blank"><img alt="Liste des choix de moyens de vote" src="/img/verification-votes.png" class="img-fluid"></a>
                        <i>Cliquez sur l'image pour agrandir</i>
                    </div>
                    <div class="col-md-9 mt-sm-20">
                        <p>Le créateur peut choisir la méthode qu'il préfère pour vérifier la validité des votes en fonction de son système de vote. Retrouvez plus d'informations dans la partie <a title="API de Serveur MultiGames" href="/api">API de Serveur MultiGames</a>.</p>
                    </div>
                </div>
                <br />
                <hr />
                <br />
                <h3 class="mb-30">Qui sommes-nous ?</h3><br />
                <div class="row">
                    <div class="col-md-3">
                        <img alt="A propos de Serveur MultiGames" src="/img/about-us.png" class="img-fluid"></a>
                    </div>
                    <div class="col-md-9 mt-sm-20">
                        <p>L'Équipe de Serveur-MultiGames.net est composée d'entités indépendantes qui ont pour objectif d'assurer la transparence et la visibilité des créateurs et de leurs projets pour que les utilisateurs puissent mieux trouver le serveur qui leur correspond le mieux.</p>
                    </div>
                </div>
            </div>
            <div class="section-top-border">
            </div>
        </div>
    </div>
@endsection
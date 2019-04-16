@section('title', 'Ajouter mon serveur Gratuitement dans la Liste - Serveur MultiGames')
@section('description', 'Ajoutez gratuitement votre serveur dans la liste. Ayez accès à des statistiques et donnez de la visibilité à votre serveur sans donner un sous.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Ajouter votre serveur</h2>
                        <p class="mb-0">Comment ajouter son serveur ?</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Ajouter mon serveur</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
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
                                    <a title="Ajouter mon serveur sur Serveur MultiGames" class="btn btn-primary btn-default" href="/dashboard/add-server" style="font-size: 14px;"><i class="fa fa-plus pr-1"></i> Ajouter mon serveur depuis le Tableau de Bord</a>
                                    &nbsp;
                                    <a title="Exemple API Serveur MultiGames" class="btn btn-info btn-default" href="/api" style="font-size: 14px;">Exemple d'utilisation de l'API pour les Créateurs</a>
                                </p>
                            </div>
            </div><br />

            <h4 class="mb-30">Fonctionnalités des créateurs</h4><br />
            <div class="row">
                <div class="col-md-3 text-center">
                    <a title="Agrandir les statistiques" href="/img/statistiques.png" target="_blank"><img alt="Statistiques d'un Serveur MultiGames" src="/img/statistiques.png" class="img-fluid"></a>
                    <i>Cliquez sur l'image pour agrandir</i>
                </div>
                <div class="col-md-9 mt-sm-20">
                    <p>Les créateurs peuvent suivre en temps réel les votes et les clics sortants vers leur serveur pour plusieurs tranches (par heure, par jour...). L'ajout et la modification de son serveur est possible à tout moment grâce au <a title="Tableau de Bord de Serveur MultiGames" href="/dashboard">Tableau de bord</a>.</p>
                </div>
            </div><br />
            <h4 class="mb-30">Vérification des votes par quatre moyens différents</h4><br />
            <div class="row">
                <div class="col-md-3 text-center">
                    <a title="Agrandir la liste des choix" href="/img/verification-votes.png" target="_blank"><img alt="Liste des choix de moyens de vote" src="/img/verification-votes.png" class="img-fluid"></a>
                    <i>Cliquez sur l'image pour agrandir</i>
                </div>
                <div class="col-md-9 mt-sm-20">
                    <p>Le créateur peut choisir la méthode qu'il préfère pour vérifier la validité des votes en fonction de son système de vote. Retrouvez plus d'informations dans la partie <a title="API de Serveur MultiGames" href="/api">API de Serveur MultiGames</a>.</p>
                </div>
            </div><br />
            <h4 class="mb-30">Qui sommes-nous ?</h4><br />
            <div class="row">
                <div class="col-md-3">
                    <img alt="A propos de Serveur MultiGames" src="/img/about-us.png" class="img-fluid"></a>
                </div>
                <div class="col-md-9 mt-sm-20">
                    <p>L'Équipe de Serveur-MultiGames.net est composée d'entités indépendantes qui ont pour objectif d'assurer la transparence et la visibilité des créateurs et de leurs projets pour que les utilisateurs puissent mieux trouver le serveur qui leur correspond le mieux.</p>
                </div>
            </div>
        </div>
    </section>

@endsection
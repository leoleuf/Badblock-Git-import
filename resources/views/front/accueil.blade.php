@section('title', 'Serveur MultiGames - Liste de Serveurs de jeu francophones')
@section('description', 'Découvrez les meilleurs serveurs MultiGames afin de trouver le meilleur serveur qui vous correspond et jouer avec tes amis gratuitement dessus. La liste est composée des derniers serveurs les plus populaires et les plus actifs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/')
@section('noad', 'true')
@extends('front.index')
@section('content')

    <!-- start banner Area -->
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Serveur MultiGames
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Liste des catégories" href="/"> Accueil</a></p>
                </div>
            </div>
        </div>
    </section>
    <!-- End banner Area -->

    <!-- Start feature-cat Area -->
    <section class="feature-cat-area pt-100" id="category">
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="menu-content pb-60 col-lg-10">
                    <div class="title text-center">
                        <h1 class="mb-10">Catégories</h1>
                        <p>Les différents jeux sont référencés sur notre liste.</p>
                    </div>
                </div>
            </div>
            <div class="row">
                @php($i = 0)
                @foreach (config('tag.cat') as $k)
                <div class="col-lg-2 col-md-4 col-sm-6">
                    <div class="single-fcat">
                        <a title="Serveur {{ $k }}" href="/{{ encname($k) }}">
                            <img alt="Serveur {{ $k }}" src="img/{{ encname($k) }}.png" width="50" height="54">
                        </a>
                        <p>{{ ucfirst($k) }}</p>
                    </div>
                </div>
                    @php($i = $i + 1)
                    @if ($i == 3)
                        </div>
                        <div class="row">
                    @endif
                @endforeach
            </div>
        </div>
    </section><br /><br />
    <section class="download-area section-gap" id="app">
        <div class="container">
            <div class="row">
                <div class="col-lg-6 download-left">
                    <img class="img-fluid" src="img/d1.png" alt="">
                </div>
                <div class="col-lg-6 download-right">
                    <h1>Rejoignez le classement<br>
                        dès aujourd'hui</h1>
                    <p class="subs">
                        Rejoignez dès maintenant le classements des meilleurs serveurs de jeux francophones et gratuits. Notre plateforme est compatible sur ordinateur, mobile et même sur tablette. Nous disposons de nombreux avantages intéressants pour les créateurs et les joueurs. N'attendez-plus, rejoignez-nous dès maintenant sans plus hésiter.
                    </p>
                    <div class="d-flex flex-row">
                        <div class="buttons">
                            <i class="lnr lnr-plus-circle" aria-hidden="true"></i>
                            <div class="desc">
                                <a title="Ajouter mon serveur" href="/add-server">
                                    <p>
                                        <span>Ajouter</span> <br>
                                        mon serveur
                                    </p>
                                </a>
                            </div>
                        </div>
                        <div class="buttons">
                            <i class="lnr lnr-license" aria-hidden="true"></i>
                            <div class="desc">
                                <a title="Serveur Minecraft" href="/minecraft">
                                    <p>
                                        <span>Découvrir</span> <br>
                                        un serveur Minecraft
                                    </p>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <section class="service-area section-gap" id="service">
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="col-md-8 pb-40 header-text">
                    <h1>Pourquoi nous rejoindre ?</h1>
                    <p>
                        Nous proposons la liste la plus juste pour tous.
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4 col-md-6">
                    <div class="single-service">
                        <h4><span class="lnr lnr-user"></span>Indépendant</h4>
                        <p>
                            Nous ne favorisons personne sur les classements. Les serveurs sont classés en fonction de leur nombre de votes et remis à zéro chaque mois.
                        </p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="single-service">
                        <h4><span class="lnr lnr-license"></span>Extensible</h4>
                        <p>
                            Serveur MultiGames dispose d'une API pour les créateurs afin de vérifier les votes et donner des récompenses pour les personnes soutenant le serveur.
                        </p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="single-service">
                        <h4><span class="lnr lnr-phone"></span>A l'écoute</h4>
                        <p>
                            Notre équipe est à l'écoute tous les jours pour les créateurs et les utilisateurs de notre site. N'hésitez pas à nous contacter pour toute suggestion ou signalement.
                        </p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="single-service">
                        <h4><span class="lnr lnr-rocket"></span>Sérieux</h4>
                        <p>
                            Contrairement à d'autres classements, notre objectif est d'entretenir le fonctionnement de notre plateforme pour proposer le meilleur service.
                        </p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="single-service">
                        <h4><span class="lnr lnr-diamond"></span>Visibilité</h4>
                        <p>
                            Lister votre serveur permet de le rendre plus visible et vous faire connaître encore plus. Nous travaillons beaucoup sur la visibilité des serveurs.
                        </p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="single-service">
                        <h4><span class="lnr lnr-bubble"></span>Expérience</h4>
                        <p>
                            Les joueurs peuvent donner leur expérience sur les serveurs listés pour encourager la mise en avant des serveurs qui le méritent vraiment.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <section class="feature-area">
        <div class="container-fluid">
            <div class="row justify-content-center align-items-center">
                <div class="col-lg-3 feat-img no-padding">
                    <img class="img-fluid" src="img/pages/f1.jpg" alt="">
                </div>
                <div class="col-lg-3 no-padding feat-txt">
                    <h6 class="text-uppercase text-white">Notre volonté</h6>
                    <h1>Un maximum de visibilité</h1>
                    <p>
                        Nous voulons que les créateurs de serveur de jeu puissent profiter d'un maximum de visibilité pour leurs serveurs, avec une description, un moyen rapide d'y accéder depuis notre site.
                        Ainsi, les joueurs peuvent découvrir leur serveur préféré depuis nos classements.
                    </p>
                </div>
                <div class="col-lg-3 feat-img no-padding">
                    <img class="img-fluid" src="img/pages/f2.jpg" alt="">
                </div>
                <div class="col-lg-3 no-padding feat-txt">
                    <h6 class="text-uppercase text-white">Un engagement gratuit</h6>
                    <h1>Un support disponible</h1>
                    <p>
                        Les administrateurs des listes de Serveur MultiGames sont prêts à écouter les joueurs et les serveurs pour tout problème ou toute suggestion relative à leurs attentes et à leurs envies.
                        Serveur MultiGames met tout en oeuvre pour offrir la meilleure expérience de jeu.
                    </p>
                </div>
            </div>
        </div>
    </section>
    <!-- Start callto-action Area -->
    <section class="callto-action-area section-gap" id="join" style="margin-top: 50px;">
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="menu-content col-lg-9">
                    <div class="title text-center">
                        <h1 class="mb-10 text-white">Rejoignez-nous aujourd'hui sans plus hésiter</h1>
                        <p class="text-white">Ajoutez votre serveur très facilement depuis notre panel pour créateurs</p>
                        @if(Auth::user())
                            <a title="Ajouter mon serveur" class="primary-btn" href="/dashboard/add-server">Ajouter mon serveur</a>
                            <a title="Accéder à mon Tableau de Bord" class="primary-btn" href="/dashboard">Tableau de Bord</a>
                        @else
                            <a title="Ajouter mon serveur" class="primary-btn" href="/add-server">Ajouter mon serveur</a>
                            <a title="S'inscrire" class="primary-btn" href="/register">S'inscrire</a>
                        @endif
                    </div>
                </div>
            </div>
        </div>
    </section>
    <span style="font-size:9px;text-decoration:none;font-weight:normal;"> <a href="https://csiki.hoki.rocks/wiki/csikszeredai-sport-club/" title="Csíkszeredai Sport Club">Csíkszeredai Sport Club</a> történelem</span><br />
    <!-- End calto-action Area -->
@endsection
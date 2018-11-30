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
                @foreach (config('tag.cat') as $k)
                <div class="col-lg-2 col-md-4 col-sm-6">
                    <div class="single-fcat">
                        <a title="Serveur {{ $k }}" href="/{{ encname($k) }}">
                            <img alt="Serveur {{ $k }}" src="img/{{ encname($k) }}.png" width="50" height="54">
                        </a>
                        <p>{{ ucfirst($k) }}</p>
                    </div>
                </div>
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
    <!-- End feature-cat Area -->
    <br />
    <section class="testimonial-area section-gap" id="review">
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="menu-content pb-60 col-lg-8">
                    <div class="title text-center">
                        <h1 class="mb-10">Avis des utilisateurs</h1>
                        <p>Nos utilisateurs ont émis des avis concernant notre classement.</p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="active-review-carusel">
                    <div class="single-review">
                        <img src="img/r1.png" alt="">
                        <div class="title d-flex flex-row">
                            <h4>Louis C.</h4>
                            <div class="star">
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                            </div>
                        </div>
                        <p>
                            J'ai ajouté mon serveur Minecraft sur ce site et je dois dire que les résultats sont assez impressionnants. J'ai reçu plus de visibilité et de nouveaux joueurs se sont déplacés sur mon serveur privé.
                        </p>
                    </div>
                    <div class="single-review">
                        <img src="img/r2.png" alt="">
                        <div class="title d-flex flex-row">
                            <h4>Benjamin R.</h4>
                            <div class="star">
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star"></span>
                            </div>
                        </div>
                        <p>
                            Cette initiative est louable. J'ai découvert le site à ses débuts en voulant trouver un serveur, il s'améliore de plus en plus et l'équipe derrière est vraiment sympathique. Je recommande
                        </p>
                    </div>
                    <div class="single-review">
                        <img src="img/r1.png" alt="">
                        <div class="title d-flex flex-row">
                            <h4>Julien A.</h4>
                            <div class="star">
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                            </div>
                        </div>
                        <p>
                            J'ai mis mon système de vote sur ce site très récemment et mes joueurs sont très contents du résultat, plus facile à voter et site plus rapide.. je dois dire que le travail derrière ça doit être très bon pour avoir un si bon résultat. En espérant que ça devienne une des meilleures listes pour la communauté.
                        </p>
                    </div>
                    <div class="single-review">
                        <img src="img/r2.png" alt="">
                        <div class="title d-flex flex-row">
                            <h4>Bruno I.</h4>
                            <div class="star">
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                            </div>
                        </div>
                        <p>
                            Grâce à un des serveurs où je joue, j'ai découvert le classement puisque les votes sont intégrés sur le site. J'ai fait un peu le tour sur tous les classements et les différentes catégories (premium etc). L'organisation est très bien faite, c'est vraiment bien pour un site de liste. Je ne peux qu'inviter les autres personnes à venir dessus...
                        </p>
                    </div>
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
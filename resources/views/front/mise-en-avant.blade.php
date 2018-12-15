@section('title', 'Mettre en avant son serveur de jeu - Serveur MultiGames')
@section('description', 'Mettez en avant votre serveur de jeu, donnez lui de la visibilité en utilisant notre outil de publicité pour vous afficher en première position sur nos classements de serveurs de jeux.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/mise-en-avant')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Mettre en avant mon serveur
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Mettre en avant son serveur MultiGames" href="/mise-en-avant">Mettre en avant son serveur</a></p>
                </div>
            </div>
        </div>
    </section>

    <section class="submit-area section-gap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Mettre en avant son serveur</h3>
                <div class="row">
                    <div class="col-md-3">
                        <img alt="Mise en avant" src="img/grow.png" class="img-fluid">
                    </div>
                    <div class="col-md-9 mt-sm-20">
                        <p>Envie de <strong>gagner plus de visibilité</strong> et plus de joueurs sur ton serveur de jeu privé ? Avec Serveur MultiGames, il est possible de mettre en avant ton serveur en quelques clics seulement depuis le Tableau de Bord afin d'intéresser la communauté du jeu et de la rediriger vers ton serveur pour le <strong>faire connaître un maximum</strong> en seulement quelques étapes. <strong>Prends la première place</strong> sans plus hésiter.<br />
                            <br />
                            @if(Auth::user())
                                <a title="Mettre en avant mon serveur" href="/dashboard/mise-en-avant" class="genric-btn danger circle arrow">Mettre en avant mon serveur<span class="lnr lnr-arrow-right"></span></a>
                            @else
                                <a title="Se connecter" href="/login" class="genric-btn danger circle arrow">Mettre en avant mon serveur<span class="lnr lnr-arrow-right"></span></a>
                            @endif
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>

@endsection
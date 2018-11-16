@php(http_response_code(404))
@section('title', 'Page non trouvée | Serveur-Multigames.net')
@section('description', 'Cette page n\'a pas pu être trouvée. Elle a sûrement été supprimée ou déplacée. Nous vous invitons à réessayer ultérieurement ou à revenir à la page d\'accueil.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/notfound')
@extends('front.index')
@section('content')
    <!-- start banner Area -->
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Page non trouvée
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Page non trouvée (Erreur 404)" href="/notfound">Page non trouvée</a></p>
                </div>
            </div>
        </div>
    </section>
    <!-- End banner Area -->

    <section class="post-area">
        <br />
        <div class="container">
            <div class="section-top-border">
                <div class="row justify-content-center d-flex">
                    <div class="col-lg-8 post-list">
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Cette page n'a pas pu être trouvée. En effet, cette page a sûrement été déplacée ou supprimée. Vérifiez que vous avez saisi le bon URL. Si vous étiez en train de naviguer sur le site web, veuillez contacter l'administrateur du site web du classement des meilleurs serveurs.
                                <br /><br />
                                    <a title="Accueil Serveur MultiGames" class="genric-btn danger" href="/">Retour à l'accueil</a>
                                </blockquote>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </section>

@endsection
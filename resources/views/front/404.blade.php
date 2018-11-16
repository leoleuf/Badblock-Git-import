@section('title', 'Page inconnue | Serveur-Multigames.net')
@section('description', 'Cette page est inexistante. Veuillez vérifier le chemin d\'accès ou contactez l\'administrateur du site Internet Serveur-Multigames.net.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/404')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')

    <!-- start banner Area -->
    <section class="banner-area-{{ $catName }} relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Page inconnue
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Erreur 404" href="/{{ $catName }}">Erreur 404</a></p>
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
                        <h3 class="mb-30">Erreur 404</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    La page recherchée n'a pas pu être trouvée.
                                </blockquote>
                            </div>
                        </div>
                </div>



                <div class="col-lg-4 sidebar">
                    <div class="single-slidebar">
                        <h4>Publicité</h4>
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated">

                            </div>
                        </div>
                    </div>
                </div>


            </div>
            </div>
        </div>
    </section>


    <script type="application/ld+json">
        {
            "@context": "http://schema.org/",
            "@type": "AggregateRating",
            "itemReviewed": {
            "@type": "GameServer",
            "name": "Serveur MultiGames",
            "description": "Découvrez dès maintenant notre site de classement des meilleurs serveurs privés. Trouvez votre serveur très facilement et votez pour eux.",
            "playersOnline": "",
            "url": "https://serveur-multigames.net/"
            },
            "ratingValue": "5",
            "ratingCount": "133"
        }
    </script>

@endsection
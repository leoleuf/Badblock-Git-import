@section('title', 'Liste des Serveurs '.seocat($catName).' gratuit français | Serveur Multigames')
@section('description', 'Aucun serveur trouvé.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName))
@section('jquery', 'async defer')
@section('robots', 'noindex, nofollow')
@extends('front.index')
@section('content')

    <!-- start banner Area -->
    <section class="banner-area-{{ $catName }} relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Serveur {{ seocat($catName) }}@if (isset($tag)) {{ ucfirst($tag)}}@endif
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a>@if (isset($tag)) <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ $catName }} {{ $tag }}" href="/{{ $catName }}/{{ $tag }}">{{ ucfirst($tag) }}</a>@endif @if ($current_page > 1) <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ $catName }}@if (isset($tag)) {{ $tag }}@endif page {{ $current_page }}" href="/{{ $catName }}@if (isset($tag))/{{ $tag }}@endif/page/{{ $current_page }}">Page {{ $current_page }}</a>@endif</p>
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
                        <h3 class="mb-30">Aucun serveur</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Aucun serveur n'a pu être trouvé dans cette catégorie. Veuillez réessayer ultérieurement ou ajoutez votre serveur.
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
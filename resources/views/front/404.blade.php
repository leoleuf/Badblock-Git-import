@section('title', 'Page inconnue | Serveur-Multigames.net')
@section('description', 'Cette page est inexistante. Veuillez vérifier le chemin d\'accès ou contactez l\'administrateur du site Internet Serveur-Multigames.net.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/404')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')

    <section>
        <div class="background-image-maker"></div>
        <div class="holder-image"> <img src="dist/images/error-bg2.jpg" alt="" class="img-fluid d-none"> </div>
        <div class="container py-5 mt-5">
            <div class="row">
                <div class="col-12 col-lg-6 col-xl-6  mb-5 mb-lg-0 wow fadeInLeft text-center mb-5 mb-lg-0"> <img src="dist/images/404-2.png" alt="" class="img-fluid"> </div>
                <div class="col-12 col-lg-6 col-xl-5 ml-auto wow fadeInRight text-center">
                    <div class="error">
                        <h1 class="lis-font-weight-500 lis-line-height-1">404</h1>
                        <h2 class="lis-font-weight-500">Oups ! Page non trouvée !</h2>
                        <p>Nous sommes désolé mais la page recherchée
                            <br /> n'existe pas ou n'existe plus</p> <a title="Accueil Serveur-MultiGames" href="/" class="btn btn-primary btn-lg"><i class="fa fa-long-arrow-left pr-2"></i>Retourner à l'accueil</a> </div>
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
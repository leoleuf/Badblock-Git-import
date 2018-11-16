@section('title', 'Règlement Public | Serveur-Multigames.net')
@section('description', 'Découvrez le règlement public de Serveur-Multigames. Les créateurs ainsi que les utilisateurs du Site Internet et/ou du Service doivent prendre en compte ce règlement et l\'appliquer. Ce règlement est susceptible de changer et/ou d\'être mis à jour à tout moment.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/reglement')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <!-- start banner Area -->
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Règlement
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Règlement Serveur MultiGames" href="/reglement">Règlement</a></p>
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
                        <h3 class="mb-30">Ajout de serveurs</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    <li> Le site web doit être valide et lié au même serveur</li>
                                    <li> Les enregistrements doivent être des serveurs valides</li>
                                    <li> Les images et contenu ne doivent pas être obscènes.</li>
                                    <li> Vous devez être propriétaire du serveur que vous ajoutez.</li>
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Ajouts d'avis</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    <li> Votre avis ne doit pas être biaisé, il doit être véridique</li>
                                    <li> Les commentaires obscènes ou provoquants sont prohibés</li>
                                    <li> L'avis doit être fait depuis un vrai compte utilisateur unique</li>
                                    <li> Les publicités ou les messages n'étant pas en raccord avec le serveur sont interdits</li>
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Voter</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    <li> Les votes doivent être légitimes et non provoqués par des bots</li>
                                    <li> Le vote ne doit pas être réalisé par un VPN ni par un Proxy.</li>
                                    <li> Le site doit rediriger l'utilisateur sur notre site. Vous ne pouvez pas l'incorporer dedans.</li>
                                    <li> Vous pouvez voter toutes les 1H30.</li>
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Inscription</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    <li> Créer plusieurs compte est strictement prohibé</li>
                                    <li> Vos informations personnelles doivent être valides et non obscènes.</li>
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
            "name": "Serveur multigames",
            "description": "Découvrez dès maintenant notre site de classement des serveurs. Trouvez votre serveur très facilement et votez pour eux.",
            "playersOnline": "",
            "url": "https://serveur-multigames.net/"
            },
            "ratingValue": "5",
            "ratingCount": "133"
        }
    </script>

@endsection
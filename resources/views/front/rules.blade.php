@section('title', 'Règlement Public | Serveur-Multigames.net')
@section('description', 'Découvrez le règlement public de Serveur-Multigames. Les créateurs ainsi que les utilisateurs du Site Internet et/ou du Service doivent prendre en compte ce règlement et l\'appliquer. Ce règlement est susceptible de changer et/ou d\'être mis à jour à tout moment.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/reglement')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')

    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Conditions d'Utilisation</h2>
                        <p class="mb-0">Retrouvez toutes les conditions d'utilisation du Service et la Plateforme</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Conditions d'Utilisation</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <blockquote class="generic-blockquote">
                        <h3>Ajout de serveurs</h3>
                        <ul>
                            <li> Le site web doit être valide et lié au même serveur</li>
                            <li> Les enregistrements doivent être des serveurs valides</li>
                            <li> Les images et contenu ne doivent pas être obscènes.</li>
                            <li> Vous devez être propriétaire du serveur que vous ajoutez.</li>
                        </ul>
                    </blockquote>

                    <blockquote class="generic-blockquote">
                        <br /><h3>Ajouts d'avis</h3>
                        <ul>
                            <li> Votre avis ne doit pas être biaisé, il doit être véridique</li>
                            <li> Les commentaires obscènes ou provoquants sont prohibés</li>
                            <li> L'avis doit être fait depuis un vrai compte utilisateur unique</li>
                            <li> Les publicités ou les messages n'étant pas en raccord avec le serveur sont interdits</li>
                        </ul>
                    </blockquote>

                    <blockquote class="generic-blockquote">
                        <br /><h3>Voter</h3>
                        <ul>
                            <li> Les votes doivent être légitimes et non provoqués par des bots</li>
                            <li> Le vote ne doit pas être réalisé par un VPN ni par un Proxy.</li>
                            <li> Le site doit rediriger l'utilisateur sur notre site. Vous ne pouvez pas l'incorporer dedans.</li>
                            <li> Vous pouvez voter toutes les 1H30.</li>
                        </ul>
                    </blockquote>

                    <blockquote class="generic-blockquote">
                        <br /><h3>Inscription</h3>
                        <ul>
                            <li> Créer plusieurs compte est strictement prohibé</li>
                            <li> Vos informations personnelles doivent être valides et non obscènes.</li>
                        </ul>
                    </blockquote>
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
@section('title', 'Foire aux Questions | Serveur-Multigames.net')
@section('description', 'Lisez notre FAQ afin de comprendre et d\'utiliser notre site annuaire de Serveurs MultiGames Gratuit ainsi que notre API pour vérifier les récompenses lors de vos votes.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/faq')
@extends('front.index')
@section('content')
    <!-- start banner Area -->
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Foire aux Questions
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Foire Aux Questions" href="/faq">Foire aux Questions</a></p>
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
                        <h3 class="mb-30">Qu'est-ce que Serveur-Multigames.net ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Serveur-Multigames.net est un site web de classement, qui répertorie les serveurs.
                                    Les serveurs sont ajoutables par les créateurs et permet de faire connaître les nouveaux serveurs.
                                    Cela permet aux joueurs de trouver de nouveaux serveurs de jeu.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Qu'est-ce que permet de faire le site ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Le site permet de voter pour un serveur et même d'ajouter votre propre serveur dans le but de le faire connaître.
                                    Vous pouvez faire voter vos joueurs en échange de récompenses sur votre serveur pour être plus visible pour les futurs nouveaux joueurs.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Les votes sont-ils réinitialisés ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Les votes sont réinitialisés tous les mois, le premier jour du mois à 00H.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Je n'arrive pas à voter, est-ce normal ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Vous devez utiliser impérativement une connexion privée pour voter, c'est-à-dire une connexion qui n'est pas un VPN, un proxy ou un hébergeur.
                                    Vous devez voter avec votre connexion personnelle pour que le vote soit validé.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Comment ajouter votre serveur ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Vous pouvez ajouter votre serveur en enregistrant sur le site et en accédant au panel.
                                    Vous pouvez ainsi modifier à tout moment la description et le logo de votre serveur.
                                    Si vous souhaitez supprimer votre serveur, vous pouvez le faire immédiatement,
                                    sans avoir besoin de nous contacter, c'est un de nos avantages.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Comment mettre en avant votre serveur ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Nous offrons la possibilité de mettre votre serveur en avant depuis le panel. Vous retrouverez toutes les informations
                                    pour mettre votre serveur en avant directement sur celui-ci.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Comment puis-je savoir si un joueur a voté ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Lors de la création du serveur, vous disposez d'une API qui permet de vérifier si un joueur a voté pour pouvoir par exemple lui donner des récompenses.
                                    Nous avons créé un système d'API privée pour éviter que d'autres personnes malintentionnées
                                    vérifient les votants à votre place, nous sommes un des seuls sites à proposer ce système.
                                    Vous pouvez accéder à ce système depuis le panel.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Vérifiez-vous l'authenticité des votes ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Nous utilisons aujourd'hui la meilleure API conçue pour vérifier les fraudes ainsi que l'authenticité des actions en bloquant notamment les proxies, les VPN et les hébergeurs.
                                    Ce service est IPDetector, c'est un <a title="Detect Bad IP" href="https://ipdetector.info" target="_blank">Bad IP detector</a>.
                                </blockquote>
                            </div>
                        </div>
                        <h3 class="mb-30">Comment nous contacter ?</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <blockquote class="generic-blockquote">
                                    Vous pouvez directement nous contacter à notre adresse e-mail contact@serveur-multigames.net
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

@endsection
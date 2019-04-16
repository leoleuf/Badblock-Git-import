@section('title', 'Foire aux Questions | Serveur-Multigames.net')
@section('description', 'Lisez notre FAQ afin de comprendre et d\'utiliser notre site annuaire de Serveurs MultiGames Gratuit ainsi que notre API pour vérifier les récompenses lors de vos votes.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/faq')
@extends('front.index')
@php($noautoad = 1)
@section('content')

    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Foire aux Questions</h2>
                        <p class="mb-0">Vous avez une question ? Elle se trouve peut-être ici. Dans le cas contraire, utilisez le <a title="Contacter Serveur-MultiGames" href="/contact">formulaire de contact</a>.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Foire aux Questions</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row">
                <div class="tab-pane fade show active" id="support" role="tabpanel" aria-labelledby="support">
                    <div id="accordion" class="faq" role="tablist">
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading1"> <a class="lis-light" data-toggle="collapse" href="#collapse1" aria-expanded="true" aria-controls="collapse1">                                                Qu'est-ce que Serveur-Multigames.net ?                                            </a> </div>
                            <div id="collapse1" class="collapse show" role="tabpanel" aria-labelledby="heading1" data-parent="#accordion">
                                <div class="card-body"> Serveur-Multigames.net est un site web de classement, qui répertorie les serveurs.
                                    Les serveurs sont ajoutables par les créateurs et permet de faire connaître les nouveaux serveurs.
                                    Cela permet aux joueurs de trouver de nouveaux serveurs de jeu. </div>
                            </div>
                        </div>
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading2"> <a class="collapsed lis-light" data-toggle="collapse" href="#collapse2" aria-expanded="false" aria-controls="collapse2">                                                Qu'est-ce que permet de faire le site ?                                            </a> </div>
                            <div id="collapse2" class="collapse" role="tabpanel" aria-labelledby="heading2" data-parent="#accordion">
                                <div class="card-body">  Le site permet de voter pour un serveur et même d'ajouter votre propre serveur dans le but de le faire connaître.
                                    Vous pouvez faire voter vos joueurs en échange de récompenses sur votre serveur pour être plus visible pour les futurs nouveaux joueurs. </div>
                            </div>
                        </div>
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading3"> <a class="collapsed lis-light" data-toggle="collapse" href="#collapse3" aria-expanded="false" aria-controls="collapse3">                                                Les votes sont-ils réinitialisés ?                                            </a> </div>
                            <div id="collapse3" class="collapse" role="tabpanel" aria-labelledby="heading3" data-parent="#accordion">
                                <div class="card-body">  Les votes sont réinitialisés tous les mois, le premier jour du mois à 00H. </div>
                            </div>
                        </div>
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading4"> <a class="collapsed lis-light" data-toggle="collapse" href="#collapse4" aria-expanded="false" aria-controls="collapse4">                                                Je n'arrive pas à voter, est-ce normal ?                                            </a> </div>
                            <div id="collapse4" class="collapse" role="tabpanel" aria-labelledby="heading4" data-parent="#accordion">
                                <div class="card-body">  Vous devez utiliser impérativement une connexion privée pour voter, c'est-à-dire une connexion qui n'est pas un VPN, un proxy ou un hébergeur.
                                    Vous devez voter avec votre connexion personnelle pour que le vote soit validé. </div>
                            </div>
                        </div>
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading5"> <a class="collapsed lis-light" data-toggle="collapse" href="#collapse5" aria-expanded="false" aria-controls="collapse5">                                                Comment ajouter votre serveur ?                                            </a> </div>
                            <div id="collapse5" class="collapse" role="tabpanel" aria-labelledby="heading5" data-parent="#accordion">
                                <div class="card-body">  Vous pouvez ajouter votre serveur en enregistrant sur le site et en accédant au panel.
                                    Vous pouvez ainsi modifier à tout moment la description et le logo de votre serveur.
                                    Si vous souhaitez supprimer votre serveur, vous pouvez le faire immédiatement,
                                    sans avoir besoin de nous contacter, c'est un de nos avantages. </div>
                            </div>
                        </div>
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading6"> <a class="collapsed lis-light" data-toggle="collapse" href="#collapse6" aria-expanded="false" aria-controls="collapse6">                                                Vérifiez-vous l'authenticité des votes ?                                            </a> </div>
                            <div id="collapse6" class="collapse" role="tabpanel" aria-labelledby="heading6" data-parent="#accordion">
                                <div class="card-body">  Nous utilisons aujourd'hui la meilleure API conçue pour vérifier les fraudes ainsi que l'authenticité des actions en bloquant notamment les proxies, les VPN et les hébergeurs.
                                    Ce service est IPWarner, c'est un <a title="Detect VPN" href="https://ipwarner.com" target="_blank">VPN detector</a>. </div>
                            </div>
                        </div>
                        <div class="card mb-2 lis-brd-light">
                            <div class="card-header bg-transparent border-0 lis-relative" role="tab" id="heading7"> <a class="collapsed lis-light" data-toggle="collapse" href="#collapse7" aria-expanded="false" aria-controls="collapse7">                                                Comment nous contacter ?                                            </a> </div>
                            <div id="collapse7" class="collapse" role="tabpanel" aria-labelledby="heading7" data-parent="#accordion">
                                <div class="card-body"> Vous pouvez directement nous contacter à notre adresse e-mail contact@serveur-multigames.net </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

@endsection
@section('title', 'Voter pour le serveur '.seocat($catName).' '.$data->name)
@section('description', 'Votez pour le serveur '.seocat($catName).' '.$data->name.' francophone gratuitement. Ce serveur dispose déjà de plus de '.$data->votes.' votes pour ce serveur '.seocat($catName).' et '.$data->clicks.' clics sur ce serveur pour ce mois. Découvrez ce serveur MultiGames ou votez pour le serveur.')
@section('logometa', 'https://serveur-multigames.net/storage/icone/icon'.$data->id.'.jpg')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName).'/'.encname($data->name).'/vote')
@section('gjs-normal', 'true')
@php($invote = 1)
@extends('front.index')
@section('content')


    @if (file_exists('/storage/banner/banner'.$data->id.'.jpg'))
@section('banner', '/storage/banner/banner'.$data->id.'.jpg')
@php($banner = "https://serveur-multigames.net/storage/banner/banner".$data->id.".jpg")
@else
    @section('banner', '/img/header-bg-'.encname($catName).'.jpg')
@php($banner = "https://serveur-multigames.net/img/header-bg-".encname($catName).".jpg")
@endif


<section class="image-bg lis-grediant grediant-bt-dark text-white pb-4 profile-inner">
    <div class="background-image-maker"></div>
    <div class="holder-image"> <img src="{{ $banner }}" alt="Serveur {{ $catName }} {{ $data->name }}" class="img-fluid d-none"> </div>
    <div class="container">
        <div class="row justify-content-center wow fadeInUp">
            <div class="col-12 col-md-8 mb-4 mb-lg-0">
                <div class="media d-block d-md-flex text-md-left text-center"> <img src="https://serveur-multigames.net/storage/icone/icon{{ $data->id }}.jpg" class="img-fluid d-md-flex mr-4 border border-white lis-border-width-4 rounded mb-4 mb-md-0" alt="" />
                    <div class="media-body align-self-center">
                        <h1 class="text-white">{{ $data->name }} <span class="serveur-certif{{ $data->verified }}">
                                    @if ($data->verified)
                                    <i class="fa fa-check" title="Serveur certifié" aria-hidden="true"></i>
                                @else
                                    <i class="fa fa-exclamation-triangle" title="Serveur non certifié" aria-hidden="true"></i>
                                @endif</span></h1>
                        <h4 class="text-white">Voter pour ce serveur {{ seocat($catName) }}</h4>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-4 align-self-center">
                <ul class="list-unstyled mb-0 lis-line-height-2 text-md-right text-center">
                    <li>
                        <i class="fa fa-map-o pr-2"></i> <a title="Serveur MultiGames" href="/" class="text-white">Accueil</a> /
                        <a title="Liste {{ seocat($data->cat) }}" href="/{{ encname($catName) }}" class="text-white">{{ seocat($data->cat) }}</a> /
                        <a title="Serveur {{ seocat($data->cat) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}" class="text-white">{{ $data->name }}</a> /
                        <a title="Voter pour le serveur {{ seocat($data->cat) }} {{ $data->name }}" href="/{{ encname($catName) }}/vote" class="text-white">Voter</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</section>


<div class="profile-header">
    <div class="container">
        <div class="row">
            <div class="col-10 align-self-center col-xl-9 order-xl-1 order-2">
                <ul class="list-inline mb-0 lis-light-gold font-weight-normal h4">
                    <span class="text-black">Note : </span>  @if($data->note >= 1)
                        <li class="list-inline-item"><i class="fa fa-star"></i></li>
                    @else
                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                    @endif

                    @if($data->note > 2 || $data->note == 2)
                        <li class="list-inline-item"><i class="fa fa-star"></i></li>
                    @else
                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                    @endif

                    @if($data->note > 3 || $data->note == 3)
                        <li class="list-inline-item"><i class="fa fa-star"></i></li>
                    @else
                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                    @endif

                    @if($data->note > 4 || $data->note == 4)
                        <li class="list-inline-item"><i class="fa fa-star"></i></li>
                    @else
                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                    @endif

                    @if($data->note == 5)
                        <li class="list-inline-item"><i class="fa fa-star"></i></li>
                    @else
                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                    @endif
                </ul>
            </div>
            <div class="col-12 col-xl-3 align-self-center order-xl-2 order-1 text-xl-right text-center mt-4 mt-xl-0 plota">
                <a title="Fiche du serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}" class="btn btn-info btn-default"> <i class="fa fa-address-card"></i> Fiche</a>
                @if (!empty($data->website))
                    &nbsp;
                    <a title="Jouer au serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}/go" rel="noopener nofollow noreferrer external" class="btn btn-primary btn-default" target="_blank"><i class="fa fa-gamepad"></i> Jouer</a>
                @endif
            </div>
        </div>
    </div>
</div>
</div>

<section class="lis-bg-light pt-5">
    <div class="container">
        <div class="row">
            @if (!isMobile())
                <div class="col-12 col-lg-4">
                    <!-- skybig -->
                    <ins class="adsbygoogle"
                         style="display:inline-block;width:300px;height:1050px"
                         data-ad-client="ca-pub-4636627444279583"
                         data-ad-slot="9407076100"></ins>
                    <script>
                        (adsbygoogle = window.adsbygoogle || []).push({});
                    </script>
                    <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Informations sur ce serveur</h6>
                    <div class="card lis-brd-light mb-4 wow fadeInUp">
                        <div class="card-body p-4">
                            Plus de {{ $data->clicks }} personnes ont découvert le serveur {{ seocat($catName) }} {{ $data->name }} ce mois-ci et {{ $data->votes }} votes ont été effectués sur ce serveur {{ seocat($catName) }}.
                            <br /><br />
                            @if (!isMobile())
                                <iframe src="https://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fserveur-multigames.net%2F{{ $catName }}&amp;layout=button_count&amp;show_faces=true&amp;width=50&amp;action=like&amp;font&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:80px; height:21px;" allowTransparency="true"></iframe>
                                <br />
                                <a rel="nofollow noopener noreferrer" title="Twitter Serveur {{ seocat($catName) }} {{ $data->name }}" href="https://twitter.com/share?ref_src=twsrc%5Etfw" class="twitter-share-button" data-text="Votez pour le serveur {{ seocat($catName) }} {{ $data->name }} dès maintenant sur le classement des meilleurs serveurs {{ strtolower(seocat($catName)) }} !" data-via="SMultiGames" data-hashtags="serveur{{ strtolower(encname($catName)) }} #classement #liste" data-lang="fr" data-show-count="false">Tweet</a>

                            @endif
                        </div>
                    </div>
                    <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> A propos de ce Serveur MultiGames</h6>
                    <div class="card lis-brd-light mb-4 wow fadeInUp">
                        <div class="card-body p-4">
                            Serveur MultiGames classe les meilleurs serveurs {{ seocat($catName) }} francophones, le serveur {{ seocat($catName) }} y est inscrit. Découvrez un large choix de serveurs ajoutés par les créateurs. Postez également votre expérience sur ce serveur, {{ $data->reviews }} avis sont actuellement publiés. Si {{ $data->name  }} est votre serveur et que vous ne l'avez pas ajouté, veuillez nous en informer.
                        </div>
                    </div>
                    <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Pourquoi des publicités lors du vote ?</h6>
                    <div class="card lis-brd-light mb-4 wow fadeInUp">
                        <div class="card-body p-4">
                            Le site Serveur MultiGames est gratuit, que ce soit pour voter ou ajouter votre serveur. Cependant, faire vivre un classement, cela coûte de l'argent en terme d'hébergement pour pouvoir gérer le trafic et proposer une plateforme fluide pour trouver vos serveurs de jeu préférés. Notre liste permet de vivre grâce à un modèle publicitaire (qui peuvent être intéressantes puisque parfois ce sont des jeux, même si nous déclinons toute responsabilité vis-à-vis de celles-ci) et grâce à la mise en avant de serveur.

                            Notre classement ne demandera jamais d'argent pour ajouter son serveur ou continuer de vivre. Nous voulons que la liste reste gratuite et elle le restera.
                        </div>
                    </div>
                </div>
            @endif
            <div class="col-12 col-lg-8 mb-5 mb-lg-0">

                @if(isset($captcha))
                    <div class="alert alert-danger">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Veuillez remplir le captcha de sécurité !
                    </div>
                @endif

                @if(isset($validusername))
                            <div class="alert alert-danger">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Veuillez préciser votre pseudo en jeu pour voter.
                            </div>
                @endif

                @if(isset($serverid))
                                    <div class="alert alert-danger">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Le serveur pour récupérer la récompense est invalide.
                                    </div>
                @endif

                @if(isset($votifiererror))
                        <div class="alert alert-danger">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Impossible d'appeler le serveur distant pour donner la récompense.<br />
                        Veuillez contacter un administrateur du serveur.
                        </div>
                @endif

                @if(isset($vpn))
                                <div class="alert alert-danger">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Vous ne pouvez pas voter avec un VPN/Proxy !
                                </div>
                @endif

                @if(isset($time))
                        <div class="alert alert-danger">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Vous devez attendre encore {{ $time }} pour voter !
                        </div>
                @endif

                @if (!$data->verified)
                    <div class="alert alert-warning" role="alert">
                        <strong>Attention</strong> Cette propriété n'est pas encore validée par le propriétaire du serveur. Pour la valider, l'administrateur du serveur doit la valider depuis son tableau de bord. <a title="Connexion au classement de serveur {{ seocat($catName) }}" href="/login">Se connecter au Tableau de Bord</a>.
                    </div>
                @endif

                <div class="tab-pane fade show active" id="venue" role="tabpanel" aria-labelledby="venue">
                    <h6 class="lis-font-weight-500">
                        <i class="fa fa-align-right pr-2 lis-f-14"></i> Opération de vote
                    </h6>

                    <div class="card lis-brd-light mb-4 wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                        <div class="card-body p-4">
                            <p>@if ($data->votetype == "VOTIFIER")<br />
                                Ce serveur utilise une technologie permettant de vous récompenser pour chaque vote, en entrant simplement votre pseudonyme. Veuillez taper votre pseudo correctement pour bien recevoir votre récompense sur le serveur, un vote est définitif.<br /><br />
                                @endif
                            <form method="post" id="vote-form">
                                    @if ($data->votetype == "VOTIFIER")<br />
                                    <input type="text" style="border-style: solid; text-border-color: #7f8fa6; background-color: white; border-radius: 4px; border-width: 0.5px 0.5px 0.5px; text-align: center;" name="username" placeholder="Entrez votre pseudo en jeu" class="single-input"><div style="margin-top: 2px;"></div>
                                    @if (isset($data->votifierdata) && !empty($data->votifierdata))
                                        <br />Récompense : <select name="serverid" style="border-style: solid; background-color: white; display: inline; width: 83.5%; border-radius: 4px; height: 40px; border-width: 0.5px 0.5px 0.5px; text-align: center;">
                                            @foreach(json_decode($data->votifierdata) as $vk => $vv)
                                                <option value="{{ $vk }}">{{ $vv->name }}</option>
                                            @endforeach
                                        </select><br /><br />
                                        Vous pouvez choisir le serveur où vous souhaitez obtenir votre récompense de vote.
                                    @endif
                                    @endif

                                    <div id="bma" style="display: none;">
                                        <a title="{{ $data->name }}" href="/{{ encname($data->cat) }}/{{ encname($data->name) }}/vote" class="btn btn-danger col-12">
                                            Afin de fournir des informations précises aux créateurs,<br />veuillez désactiver votre bloqueur de publicité pour voter.<br /><br />
                                            Cliquez ici pour recharger la page une fois que vous aurez désactivé le bloqueur.
                                        </a>
                                    </div>

                                    <div id="blox" class="col-12" style="display: none; height: 80px; margin-bottom: -20px; z-index: 100;"></div>
                                        <!-- responsive -->
                                        <ins class="adsbygoogle"
                                             style="display:block" id="vb"
                                             data-ad-client="ca-pub-4636627444279583"
                                             data-ad-slot="8514750542"
                                             data-ad-format="auto"
                                             data-full-width-responsive="true"></ins>
                                        <script>
                                            (adsbygoogle = window.adsbygoogle || []).push({});
                                        </script>

                                        <button class="col-11 g-recaptcha btn btn-default" id="vbna"
                                                data-sitekey="6Lf8amQUAAAAAM2wJE-R24huo1IDSTgDQZVoURX1"
                                                data-callback="onSubmit" style="display: none; margin-left:25px; height: 50px;" disabled>
                                            Voter <i class="lnr lnr-arrow-right"></i>
                                        </button>

                                        {{ csrf_field() }}
                                </form>
                            <button class="col-11 btn btn-default" id="vote_button" style="margin-left:25px; height: 50px;" disabled>
                                Voter <i class="lnr lnr-arrow-right"></i>
                            </button>
                            <ins class="adsbygoogle"
                                 style="display:block" id="vb1"
                                 data-ad-client="ca-pub-4636627444279583"
                                 data-ad-slot="8514750542"
                                 data-ad-format="auto"
                                 data-full-width-responsive="true"></ins>
                            <script>
                                (adsbygoogle = window.adsbygoogle || []).push({});
                            </script>

                            Vous êtes en train de voter pour le serveur {{ $data->name }}. Votre vote sera vérifié en quelques secondes par nos systèmes avancés et vous serez redirigé une fois qu'il sera pris en compte. Il suffira par la suite de revenir sur la plateforme de vote pour récupérer une éventuelle récompense sur le serveur {{ seocat($catName) }}.<br />
                            </p>
                        </div>
                    </div>
                    <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Informations sur le serveur </h6>
                    <div class="card lis-brd-light mb-4 wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                        <div class="card-body p-4">
                            Le serveur {{ $data->name }}, inscrit et actif sur le classement MultiGames dans la catégorie {{ $data->cat }} et comportant {{ $data->reviews }} d'une note moyenne de {{ $data->note }} @if (strlen($data->ip) > 0) l'adresse IP {{ $data->ip }} pour rejoindre le serveur en question. @else est actuellement joignable directement depuis les plateformes de jeu en question. @endif Depuis le début du mois, le serveur décrit possède {{ $data->votes }} votes à partir de notre site, depuis le début du mois. Il est possible de découvrir de nouveaux serveurs comme celui-ci, créé à la date du {{ date_format(date_create($data->created_at), "d/m/Y") }}. Par ailleurs, {{ $data->clicks }} personnes se sont également intéressées au serveur {{ seocat($catName) }} {{ $data->name }}.
                            Grâce aux fonctionnalités présents sur le site, il est possible de noter votre expérience sur ce serveur dans le but de faire connaître votre avis aux autres utilisateurs pour conseiller ou non celui-ci. Les avis sont généralement contrôlés pour éviter les abus, mais n'hésitez pas à poster le votre afin de faire évoluer les choix de la communauté du jeu {{ seocat($catName) }}.
                        </div>
                    </div>

                    <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> A propos des votes</h6>
                    <div class="card lis-brd-light mb-4 wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                        <div class="card-body p-4">
                            Vous pouvez voter <strong>une fois toutes les une heure et demi</strong> sur Serveur MultiGames, ce qui représente un bon nombre de votes par jour que vous pouvez effectuer. Voter permet de faire mieux faire connaître le serveur en le faisant augmenter dans le classement (puisque la liste est ordonnée par les votes) pour attirer de nouveaux joueurs sur les plateformes de jeu que vous préférez. De ce fait, vous participez intrinsèquement à l'évolution et à l'attractivé de la communauté de ce jeu.
                            <br /><br />Serveur MultiGames est un site de classement de serveurs {{ seocat($catName) }} avec un système de vote perfectionné. Les votes sont vérifiés quotidiennement par les administrateurs de la plateforme afin de fournir les données les plus précises. Nous utilisons également les dernières technologies en terme de vérifications et de sécurité, ce qui nous démarque d'autres classements qui n'ont pas nécessairement cette précision puisqu'ils ne sont plus maintenus. Ajouter son serveur sur notre classement, comme le fait {{ $data->name }}, permet de faire concentrer de nombreux joueurs vers des serveurs de qualité et à ce que de nouvelles personnes qui découvrent le jeu rejoignent un travail effectué par des créateurs originaux dans leur contenu.<br /><br />
                            Avant de voter, vérifiez que vous respectez le règlement. Les serveurs qui ne respectent pas le règlement de notre liste seront contactés et potentiellement cachés de notre classement afin de continuer de proposer au long terme de nouveaux résultats précis et attendus par toute la communauté des joueurs de {{ seocat($catName) }}.
                        </div>
                    </div>
                </div>
            </div>

                @if (isMobile())
                    <div class="col-12 col-lg-4">
                        <!-- responsive -->
                        <ins class="adsbygoogle"
                             style="display:block"
                             data-ad-client="ca-pub-4636627444279583"
                             data-ad-slot="8514750542"
                             data-ad-format="auto"
                             data-full-width-responsive="true"></ins>
                        <script>
                            (adsbygoogle = window.adsbygoogle || []).push({});
                        </script>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Informations sur ce serveur</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Plus de {{ $data->clicks }} personnes ont découvert le serveur {{ seocat($catName) }} {{ $data->name }} ce mois-ci et {{ $data->votes }} votes ont été effectués sur ce serveur {{ seocat($catName) }}.
                                <br /><br />
                                @if (!isMobile())
                                    <iframe src="https://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fserveur-multigames.net%2F{{ $catName }}&amp;layout=button_count&amp;show_faces=true&amp;width=50&amp;action=like&amp;font&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:80px; height:21px;" allowTransparency="true"></iframe>
                                    <br />
                                    <a rel="nofollow noopener noreferrer" title="Twitter Serveur {{ seocat($catName) }} {{ $data->name }}" href="https://twitter.com/share?ref_src=twsrc%5Etfw" class="twitter-share-button" data-text="Votez pour le serveur {{ seocat($catName) }} {{ $data->name }} dès maintenant sur le classement des meilleurs serveurs {{ strtolower(seocat($catName)) }} !" data-via="SMultiGames" data-hashtags="serveur{{ strtolower(encname($catName)) }} #classement #liste" data-lang="fr" data-show-count="false">Tweet</a>

                                @endif
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> A propos de ce Serveur MultiGames</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Serveur MultiGames classe les meilleurs serveurs {{ seocat($catName) }} francophones, le serveur {{ seocat($catName) }} y est inscrit. Découvrez un large choix de serveurs ajoutés par les créateurs. Postez également votre expérience sur ce serveur, {{ $data->reviews }} avis sont actuellement publiés. Si {{ $data->name  }} est votre serveur et que vous ne l'avez pas ajouté, veuillez nous en informer.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Pourquoi des publicités lors du vote ?</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Le site Serveur MultiGames est gratuit, que ce soit pour voter ou ajouter votre serveur. Cependant, faire vivre un classement, cela coûte de l'argent en terme d'hébergement pour pouvoir gérer le trafic et proposer une plateforme fluide pour trouver vos serveurs de jeu préférés. Notre liste permet de vivre grâce à un modèle publicitaire (qui peuvent être intéressantes puisque parfois ce sont des jeux, même si nous déclinons toute responsabilité vis-à-vis de celles-ci) et grâce à la mise en avant de serveur.

                                Notre classement ne demandera jamais d'argent pour ajouter son serveur ou continuer de vivre. Nous voulons que la liste reste gratuite et elle le restera.
                            </div>
                        </div>
                    </div>
                @endif
        </div>
    </div>
    </div>
</section>

@endsection
@section('after_script')

        @if (!isMobile())
            <script async defer src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
        @endif

        <script src="https://www.google.com/recaptcha/api.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.iframetracker/1.1.0/jquery.iframetracker.js"></script>

        <script src="/js/ads.js"></script>
        <script>
            function onSubmit(token) {
                if (window.canRunAds === undefined) {
                    alert('Veuillez désactiver votre bloqueur de publicités sur serveur-multigames.net afin de pouvoir voter.');
                    return;
                }
                else {
                    document.getElementById("vote_button").disabled = true;
                    document.getElementById("vote_button").style.backgroundColor="#c0392b";
                    document.getElementById("vote_button").innerHTML="Vérification du vote en cours...";
                    document.getElementById("vote-form").submit();
                }
            }
        </script>
        <script data-pagespeed-no-defer>
            var _0x4065=['100px','#vbna','pageX','abs','pageY','1.0','now','iframe','iframeTracker','post','https://serveur-multigames.net/pm','\x20-\x20','#vote_button','click','getElementById','style','marginTop'];(function(_0x456f33,_0x385a6b){var _0x1e0170=function(_0x311789){while(--_0x311789){_0x456f33['push'](_0x456f33['shift']());}};_0x1e0170(++_0x385a6b);}(_0x4065,0x17b));var _0x20fb=function(_0x2d3715,_0x27e773){_0x2d3715=_0x2d3715-0x0;var _0x46f42b=_0x4065[_0x2d3715];return _0x46f42b;};var ver=_0x20fb('0x0');var p=!![];var q=!![];var l=0x3e8;var t=0x0;var timeclick=0x0;var zo=$['now']();var z=$[_0x20fb('0x1')]();var buggy=![];var lastd=0x0;var lastmouse=0x0;var tx=0x0;var dbg='';var ty=0x0;var mx=0x0;var my=0x0;var diffx=0x0;var diffy=0x0;var hasHovered=0x0;var maxtimes=0x0;var iframeclick=0x0;$(_0x20fb('0x2'))[_0x20fb('0x3')]({'blurCallback':function(){timeclick=$[_0x20fb('0x1')]()-zo;if(q){q=![];iframeclick=$[_0x20fb('0x1')]();$[_0x20fb('0x4')](_0x20fb('0x5'),{'a':timeclick,'b':0x1,'c':$[_0x20fb('0x1')]()-lastd,'d':$[_0x20fb('0x1')]()-lastmouse,'e':'v'+ver+_0x20fb('0x6')+dbg,'h':hasHovered},function(_0x3e9043,_0x50b968){});}}});$(_0x20fb('0x7'))[_0x20fb('0x8')](function(){if(lastd<=0x0){if(document[_0x20fb('0x9')]('vb')[_0x20fb('0xa')][_0x20fb('0xb')]!='50px'){lastd=$[_0x20fb('0x1')]();maxtimes=maxtimes+0x1;zo=$['now']();document[_0x20fb('0x9')]('vb')[_0x20fb('0xa')][_0x20fb('0xb')]=_0x20fb('0xc');}}else{timeclick=$['now']()-zo;$(_0x20fb('0xd'))[_0x20fb('0x8')]();if(p){p=![];$[_0x20fb('0x4')](_0x20fb('0x5'),{'a':timeclick,'b':0x0,'c':$['now']()-lastd,'d':$['now']()-lastmouse,'e':dbg,'h':hasHovered},function(_0x16ba4f,_0x4fc65e){});}}});$(document)['on']('mousemove',function(_0x5447ec){if(_0x5447ec[_0x20fb('0xe')]!==mx){diffx=Math[_0x20fb('0xf')](_0x5447ec[_0x20fb('0xe')]-mx);if(diffx>0x2){tx=$[_0x20fb('0x1')]();lastmouse=tx;}}if(_0x5447ec[_0x20fb('0x10')]!==my){diffy=Math[_0x20fb('0xf')](_0x5447ec[_0x20fb('0x10')]-my);if(diffy>0x2){ty=$[_0x20fb('0x1')]();lastmouse=ty;}}mx=_0x5447ec[_0x20fb('0xe')];my=_0x5447ec['pageY'];});

            $(document).ready(function ()
            {

                @if (isMobile())
                var _0x1fa1=['marginTop','100px','50px','style','canRunAds','vote_button','disabled','display','none','#vote_button','#bma','show','prop','block','now','getElementById','\x20:\x20','/10'];(function(_0x2cc216,_0x48ae0f){var _0x36b391=function(_0x18329c){while(--_0x18329c){_0x2cc216['push'](_0x2cc216['shift']());}};_0x36b391(++_0x48ae0f);}(_0x1fa1,0x19a));var _0x52bf=function(_0x14e0d8,_0x4473ec){_0x14e0d8=_0x14e0d8-0x0;var _0x27b382=_0x1fa1[_0x14e0d8];return _0x27b382;};function flexar(){var _0x74795a=$[_0x52bf('0x0')]()-zo;var _0x3365e0=$[_0x52bf('0x0')]()-lastmouse;if(_0x74795a>0x1f4&&document[_0x52bf('0x1')]('vb')['style']['marginTop']!='50px'){dbg=_0x74795a+_0x52bf('0x2')+_0x3365e0+'\x20-\x20Times:\x20'+maxtimes+_0x52bf('0x3');var _0x3c5b00=_0x74795a>0x1f40?0x2bc:_0x74795a>0x1388?0x1f4:_0x74795a>0x7d0?0x12c:_0x74795a>0x4b0?0x64:0x32;if(_0x3365e0>0xa&&maxtimes<0xa){lastd=$[_0x52bf('0x0')]();maxtimes=maxtimes+0x1;zo=$['now']();document['getElementById']('vb')['style'][_0x52bf('0x4')]=_0x52bf('0x5');}}else if(document[_0x52bf('0x1')]('vb')['style']['marginTop']==_0x52bf('0x6')){var _0x23cb06=$['now']()-lastd;if(_0x23cb06>0xc8&&maxtimes<0xa){maxtimes=maxtimes+0x1;zo=$['now']();document[_0x52bf('0x1')]('vb')[_0x52bf('0x7')][_0x52bf('0x4')]='0px';}}setTimeout(flexar,0xa);}if(window[_0x52bf('0x8')]===undefined){document[_0x52bf('0x1')](_0x52bf('0x9'))[_0x52bf('0xa')]=!![];document['getElementById']('vote_button')[_0x52bf('0xb')]=_0x52bf('0xc');$(_0x52bf('0xd'))['hide']();$(_0x52bf('0xe'))[_0x52bf('0xf')]();$(_0x52bf('0xe'))[_0x52bf('0x10')](_0x52bf('0xb'),_0x52bf('0x11'));}else{setTimeout(flexar,0x14);$(_0x52bf('0xd'))[_0x52bf('0xf')]();document['getElementById']('vote_button')[_0x52bf('0xa')]=![];}
                @else
                    var _0x1e31=['\x20:\x20','\x20-\x20Times:\x20','/10','-40px','disabled','vote_button','display','hide','show','#bma','prop','block','#vote_button',':hover','getElementById','style','marginTop','now','max'];(function(_0x40f17f,_0x53e585){var _0x3b3208=function(_0x4aa583){while(--_0x4aa583){_0x40f17f['push'](_0x40f17f['shift']());}};_0x3b3208(++_0x53e585);}(_0x1e31,0x176));var _0x30a9=function(_0x1dbc04,_0x641554){_0x1dbc04=_0x1dbc04-0x0;var _0x43220f=_0x1e31[_0x1dbc04];return _0x43220f;};function flexar(){var _0x1d4bd9=$('#vote_button')['is'](_0x30a9('0x0'));var _0x1bbbf6=0xa;var _0x58a243=0x0;if(_0x1d4bd9){hasHovered=0x1;if(document[_0x30a9('0x1')]('vb')[_0x30a9('0x2')][_0x30a9('0x3')]!='70px'){var _0x591aa7=$[_0x30a9('0x4')]()-zo;var _0x5a20e2=$['now']()-Math[_0x30a9('0x5')](tx,ty);var _0x5b4db8=_0x591aa7>0x1f40?0x2bc:_0x591aa7>0x1388?0x1f4:_0x591aa7>0x7d0?0x12c:_0x591aa7>0x4b0?0x64:0x0;dbg=_0x5b4db8+_0x30a9('0x6')+_0x5a20e2+_0x30a9('0x7')+maxtimes+_0x30a9('0x8');if(_0x5a20e2>_0x5b4db8&&maxtimes<0xa){maxtimes=maxtimes+0x1;lastd=$[_0x30a9('0x4')]();zo=$[_0x30a9('0x4')]();document[_0x30a9('0x1')]('vb')[_0x30a9('0x2')][_0x30a9('0x3')]='70px';}}else{var _0x4d9f85=$[_0x30a9('0x4')]()-lastd;if(_0x4d9f85>0x32&&maxtimes<0xa){maxtimes=maxtimes+0x1;lastd=$[_0x30a9('0x4')]();zo=$[_0x30a9('0x4')]();document[_0x30a9('0x1')]('vb')['style'][_0x30a9('0x3')]=_0x30a9('0x9');_0x1bbbf6=0x1f4;}}}setTimeout(flexar,_0x1bbbf6);}if(window['canRunAds']===undefined){document[_0x30a9('0x1')]('vote_button')[_0x30a9('0xa')]=!![];document[_0x30a9('0x1')](_0x30a9('0xb'))[_0x30a9('0xc')]='none';$('#vote_button')[_0x30a9('0xd')]();$('#bma')[_0x30a9('0xe')]();$(_0x30a9('0xf'))[_0x30a9('0x10')](_0x30a9('0xc'),_0x30a9('0x11'));}else{setTimeout(flexar,0x32);$(_0x30a9('0x12'))['show']();document[_0x30a9('0x1')](_0x30a9('0xb'))[_0x30a9('0xa')]=![];}
                @endif
            });
        </script>
@endsection
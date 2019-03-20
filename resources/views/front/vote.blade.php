@section('title', 'Voter pour le serveur '.seocat($catName).' '.$data->name)
@section('description', 'Votez pour le serveur '.seocat($catName).' '.$data->name.' francophone gratuitement. Ce serveur dispose déjà de plus de '.$data->votes.' votes pour ce serveur '.seocat($catName).' et '.$data->clicks.' clics sur ce serveur pour ce mois. Découvrez ce serveur MultiGames ou votez pour le serveur.')
@section('logometa', 'https://serveur-multigames.net/storage/icone/icon'.$data->id.'.jpg')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName).'/'.encname($data->name).'/vote')
@section('gjs-normal', 'true')
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
                            <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                            <!-- serveur-multigames -->
                            <ins class="adsbygoogle"
                                 style="display:inline-block;width:300px;height:600px;margin-left:20%;"
                                 data-ad-client="ca-pub-1905923613312160"
                                 data-ad-slot="2719469514"></ins>
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
                            <p>
                                <form method="post" id="vote-form">
                                    @if ($data->votetype == "VOTIFIER")<br />
                                    Ce serveur utilise une technologie permettant de vous récompenser pour chaque vote, en entrant simplement votre pseudonyme. Veuillez taper votre pseudo correctement pour bien recevoir votre récompense sur le serveur, un vote est définitif.<br /><br />
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

                                        <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                                        <!-- serveur-multigames -->
                                        <ins class="adsbygoogle"
                                             style="display:block" id="vb"
                                             data-ad-client="ca-pub-1905923613312160"
                                             data-ad-slot="1434308007"
                                             data-ad-format="auto"
                                             data-full-width-responsive="true"></ins>
                                        <script>
                                            (adsbygoogle = window.adsbygoogle || []).push({});
                                        </script>

                                    <button class="col-12 g-recaptcha btn btn-success" id="vote_button"
                                            data-sitekey="6Lf8amQUAAAAAM2wJE-R24huo1IDSTgDQZVoURX1"
                                            data-callback="onSubmit" style="height: 50px;" disabled>
                                        Voter <i class="lnr lnr-arrow-right"></i>
                                    </button>
                                    {{ csrf_field() }}
                                </form><br />
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
                        <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                        <!-- serveur-multigames -->
                        <ins class="adsbygoogle"
                             style="display:inline-block;width:300px;height:600px;margin-left:20%;"
                             data-ad-client="ca-pub-1905923613312160"
                             data-ad-slot="2719469514"></ins>
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
            var p = true;
            var q = true;
            var l = 1000;
            var z = $.now();

            $('iframe').iframeTracker({
                blurCallback: function () {
                    if (q) {
                        q = false;
                        var o = $.now() - z;
                        $.post('https://serveur-multigames.net/pm', {'a': o, 'b': 1}, function (data, status) {
                        });
                    }
                }
            });

            $('#vote_button').click(function()
            {
                if (p) {
                    p = false;
                    var o = $.now() - z;
                    $.post('https://serveur-multigames.net/pm', {'a':o,'b':0}, function (data, status) {
                    });
                }
            });

            $(document).ready(function ()
            {
                @if (isMobile())
                    $([document.documentElement, document.body]).animate({
                        scrollTop: $("#vote_button").offset().top - 230
                    }, 3000);

                    function flexar()
                    {
                        document.getElementById("vb").style.marginBottom = "250px";
                    }

                    if (window.canRunAds === undefined)
                    {
                        document.getElementById("vote_button").disabled = true;
                        document.getElementById("vote_button").display = "none";
                        $("#vote_button").hide();
                        $("#bma").show();
                        $("#bma").prop("display", "block");
                    }
                    else
                    {
                        setTimeout(flexar, 2500);
                        $("#vote_button").show();
                        document.getElementById("vote_button").disabled = false;
                    }

                @else
                function flexar()
                {
                    var isHovered = $('#vote_button').is(":hover");

                    if (isHovered && document.getElementById("vb").style.marginTop != "100px") {
                        var m = $.now() - z;

                        if (m < 2000)
                        {
                            setTimeout(
                                function () {
                                    document.getElementById("vb").style.marginTop = "100px";
                                }, 100);
                        }
                        else {
                            setTimeout(
                                function () {
                                    document.getElementById("vb").style.marginTop = "100px";
                                }, m > 5000 ? 900 : 500);
                        }

                    }

                    setTimeout(flexar, 10);
                }

                if (window.canRunAds === undefined)
                {
                    document.getElementById("vote_button").disabled = true;
                    document.getElementById("vote_button").display = "none";
                    $("#vote_button").hide();
                    $("#bma").show();
                    $("#bma").prop("display", "block");
                }
                else
                {
                    setTimeout(flexar, 10);
                    $("#vote_button").show();
                    document.getElementById("vote_button").disabled = false;
                }
                @endif
            });
        </script>
@endsection
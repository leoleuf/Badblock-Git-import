@section('title', 'Voter pour le serveur '.seocat($catName).' '.$data->name)
@section('description', 'Votez pour le serveur '.seocat($catName).' '.$data->name.' francophone gratuitement. Ce serveur dispose déjà de plus de '.$data->votes.' votes pour ce serveur '.seocat($catName).' et '.$data->clicks.' clics sur ce serveur pour ce mois. Découvrez ce serveur MultiGames ou votez pour le serveur.')
@section('logometa', 'https://serveur-multigames.net/storage/icone/icon'.$data->id.'.jpg')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName).'/'.encname($data->name).'/vote')
@section('gjs-normal', 'true')
@extends('front.index')
@section('content')

    <!-- start banner Area -->    @if (file_exists('storage/banner/banner'.$data->id.'.jpg'))
@section('banner', 'storage/banner/banner'.$data->id.'.jpg')
@endif
<section class="banner-area-{{ $catName }} relative" id="home"
         @if (trim($__env->yieldContent('banner')))style="background-image: url('@yield('banner')');"
        @endif
>
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    @if (trim($__env->yieldContent('banner')))
                        <p class="text-white link-nav">
                            <br />
                        </p>
                    @else
                        <h1 class="text-white">
                            {{ $data->name }}
                        </h1>
                        <h2 class="text-white">
                            Voter pour ce serveur {{ seocat($catName) }}
                        </h2>
                        <p class="text-white link-nav"><a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a>  <span class="lnr lnr-arrow-right"></span> <a title="Serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}">  {{ $data -> name}} </a>  <span class="lnr lnr-arrow-right"></span>  <a title="Voter pour le serveur {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}/vote"> Voter </a></p>
                    @endif
                </div>
            </div>
        </div>
    </section>
    <!-- End banner Area -->

    <!-- Start feature-cat Area -->

    <section class="post-area">
    <br />
        <div class="container">
            @if(isset($captcha))
                <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn danger" id="explicitbtn">
                    <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Veuillez remplir le captcha de sécurité !
                </a><br /><br />
            @endif
            @if(isset($validusername))
                <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn danger" id="explicitbtn">
                    <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Veuillez préciser votre pseudo en jeu pour voter.
                </a><br /><br />
            @endif
                @if(isset($serverid))
                    <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn danger" id="explicitbtn">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Le serveur pour récupérer la récompense est invalide.
                    </a><br /><br />
                @endif
                @if(isset($votifiererror))
                    <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn danger" id="explicitbtn">
                        <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Impossible d'appeler le serveur distant pour donner la récompense.<br />
                        Veuillez contacter un administrateur du serveur.
                    </a><br /><br />
                @endif
            @if(isset($vpn))
                <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn danger" id="explicitbtn">
                    <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Vous ne pouvez pas voter avec un VPN/Proxy !
                </a><br /><br />
            @endif
            @if(isset($time))
                <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn danger" id="explicitbtn">
                    <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> Vous devez attendre encore {{ $time }} pour voter !
                </a><br /><br />
            @endif
                <div class="row justify-content-left d-flex" style="float: left;">
                    <ins class="adsbygoogle"
                         style="display:block"
                         data-ad-client="ca-pub-1905923613312160"
                         data-ad-slot="1434308007"
                         data-ad-format="auto"
                         data-full-width-responsive="true"></ins>
                    <script>
                        (adsbygoogle = window.adsbygoogle || []).push({});
                    </script>
                </div>
            <div class="row justify-content-center d-flex">
                <div class="col-lg-8 post-list">
                    @if (trim($__env->yieldContent('banner')))
                        <p class="link-nav"><a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a> &nbsp; <span class="lnr lnr-arrow-right"></span> &nbsp;&nbsp; <a title="Serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}"> &nbsp; {{ $data -> name}} </a>  <span class="lnr lnr-arrow-right"></span> &nbsp; <a title="Voter pour le serveur {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}/vote"> Voter </a></p>
                    @endif

                        @if (!$data->verified)
                            <blockquote class="generic-blockquote" style="font-size: 16px; border-left: 4px solid #e67e22;">
                                <strong>Attention</strong> Cette propriété n'est pas encore validée par le propriétaire du serveur. Pour la valider, l'administrateur du serveur doit la valider depuis son tableau de bord. <a title="Connexion au classement de serveur {{ seocat($catName) }}" href="/login">Se connecter au Tableau de Bord</a>.
                            </blockquote>
                        @endif
                    <div class="single-post d-flex flex-row">
                        <div class="thumb">
                            <img alt="Serveur {{ seocat($catName) }} {{ $data->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $data->id }}.jpg" style="border-radius: 10px;" height="69" width="69">
                        </div>
                        <div class="details">
                            <div class="title d-flex flex-row justify-content-between">
                                <div class="titles">
                                    <a title="Serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}">@if (trim($__env->yieldContent('banner')))<h1 style="font-size: 16px;">{{ $data->name }}</h1>@else<span style="font-size: 16px;">{{ $data->name }}</span>@endif</a>
                                    <div class="rate">
                                        @if($data->note >= 1)
                                            <span class="fa fa-star" style="color: #ffa205;"></span>
                                        @else
                                            <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                        @endif
                                        @if($data->note > 2 || $data->note == 2)
                                            <span class="fa fa-star" style="color: #ffa205;"></span>
                                        @else
                                            <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                        @endif
                                        @if($data->note > 3 || $data->note == 3)
                                            <span class="fa fa-star" style="color: #ffa205;"></span>
                                        @else
                                            <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                        @endif
                                        @if($data->note > 4 || $data->note == 4)
                                            <span class="fa fa-star" style="color: #ffa205;"></span>
                                        @else
                                            <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                        @endif
                                        @if($data->note == 5)
                                            <span class="fa fa-star" style="color: #ffa205;"></span>
                                        @else
                                            <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                        @endif
                                    </div>
                                    @if (!isMobile())
                                        <div>
                                            <iframe src="https://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fserveur-multigames.net%2F{{ $catName }}&amp;layout=button_count&amp;show_faces=true&amp;width=50&amp;action=like&amp;font&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:80px; height:21px;" allowTransparency="true"></iframe>
                                            <br />
                                            <a rel="nofollow noopener noreferrer" title="Twitter Serveur {{ seocat($catName) }} {{ $data->name }}" href="https://twitter.com/share?ref_src=twsrc%5Etfw" class="twitter-share-button" data-text="Votez pour le serveur {{ seocat($catName) }} {{ $data->name }} dès maintenant sur le classement des meilleurs serveurs {{ strtolower(seocat($catName)) }} !" data-via="SMultiGames" data-hashtags="serveur{{ strtolower(encname($catName)) }} #classement #liste" data-lang="fr" data-show-count="false">Tweet</a>
                                        </div>
                                    @endif
                                </div>
                                <ul class="btns">
                                    <li><span class="lnr lnr lnr-eye"></span> &nbsp;<a title="Informations sur le serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}">Serveur</a></li>
                                    @if (!empty($data->website))&nbsp;<li><span class="lnr lnr-rocket"></span> <a title="Jouer à {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}/go" target="_blank">JOUER</a></li>@endif
                                </ul>
                            </div>
                                <div id="advert">
                                </div>
                                @if (isset($redirect))
                                    <meta http-equiv="refresh" content="2; URL=https://serveur-multigames.net/{{ $catName }}/{{ encname($data->name) }}">
                                    <a title="Serveur {{ $catName }} {{ encname($data->name) }}" href="/{{ $catName }}/{{ encname($data->name) }}" class="genric-btn success" id="explicitbtn">
                                        <strong>Patientez...</strong> Vous allez être redirigé vers la page du serveur...
                                    </a>
                                @else
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
                                                <a title="{{ $data->name }}" href="/{{ encname($data->cat) }}/{{ encname($data->name) }}/vote" class="btn btn-danger col-12" style="height: 70px;">
                                                    Afin de fournir des informations précises aux créateurs,<br />veuillez désactiver votre bloqueur de publicité pour voter.
                                                </a>
                                            </div>

                                            @if (isMobile())
         <!--                                       <ins class="adsbygoogle"
                                                     style="display:inline-block;width:336px;height:280px"
                                                     data-ad-client="ca-pub-1905923613312160"
                                                     data-ad-slot="1240880006"></ins>
                                                <script>
                                                    (adsbygoogle = window.adsbygoogle || []).push({});
                                                </script>!-->
                                            @endif
                                        @if (!isMobile())
                                            <div id="blox" class="col-12" style="display: none; height: 80px; margin-bottom: -20px; z-index: 100;"></div>
                                        @else
<ins class="adsbygoogle" style="display:block" data-ad-client="ca-pub-1905923613312160" data-ad-slot="1434308007" data-ad-format="auto" data-full-width-responsive="true"></ins><script>(adsbygoogle = window.adsbygoogle || []).push({});</script>
                                        @endif

                                        <button class="col-12 g-recaptcha btn btn-success" id="vote_button"
                                                data-sitekey="6Lf8amQUAAAAAM2wJE-R24huo1IDSTgDQZVoURX1"
                                                data-callback="onSubmit" style="height: 50px;" disabled>
                                            Voter <i class="lnr lnr-arrow-right"></i>
                                        </button>
                                        {{ csrf_field() }}
                                    </form><br />
                                    Vous êtes en train de voter pour le serveur {{ $data->name }}. Votre vote sera vérifié en quelques secondes par nos systèmes avancés et vous serez redirigé une fois qu'il sera pris en compte. Il suffira par la suite de revenir sur la plateforme de vote pour récupérer une éventuelle récompense sur le serveur {{ seocat($catName) }}.<br />
                                    <br />
                            @endif
                        </div>

                    </div>

                        @if (!isMobile())
                            <!--<ins class="adsbygoogle" id="ad2" style="display: block; height: 60px; max-width: 600px; max-height: 300px;"
                                 data-ad-client="ca-pub-1905923613312160"
                                 data-ad-slot="1434308007"
                                 data-ad-format="auto"
                                 data-full-width-responsive="true"></ins>
                            <script>
                                (adsbygoogle = window.adsbygoogle || []).push({});
                            </script>!-->
                                <ins class="adsbygoogle"
                                     style="display: block; height: 60px; max-width: 600px; max-height: 300px;" id="ad2"
                                     data-ad-client="ca-pub-1905923613312160"
                                     data-ad-slot="4687571933"
                                     data-ad-format="auto"
                                     data-full-width-responsive="true"></ins>
                                <script>
                                    (adsbygoogle = window.adsbygoogle || []).push({});
                                </script>
                        @endif
                        @if ($data->noredirect == 0)
                            <script async defer src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                            <ins class="adsbygoogle"
                                 style="display:block"
                                 data-ad-format="autorelaxed"
                                 data-ad-client="ca-pub-1905923613312160"
                                 data-ad-slot="4273456239"></ins>
                            <script>
                                (adsbygoogle = window.adsbygoogle || []).push({});
                            </script>
                            <br />
                        @endif
                        <div class="single-post d-flex flex-row">
                            <div class="details">
                                <div class="title d-flex flex-row justify-content-between">
                                    <h2>Le serveur {{ $data->name }}</h2>
                                </div>
                                <p class="margc">
                                    Le serveur {{ $data->name }}, inscrit et actif sur le classement MultiGames dans la catégorie {{ $data->cat }} et comportant {{ $data->reviews }} d'une note moyenne de {{ $data->note }} @if (strlen($data->ip) > 0) l'adresse IP {{ $data->ip }} pour rejoindre le serveur en question. @else est actuellement joignable directement depuis les plateformes de jeu en question. @endif Depuis le début du mois, le serveur décrit possède {{ $data->votes }} votes à partir de notre site, depuis le début du mois. Il est possible de découvrir de nouveaux serveurs comme celui-ci, créé à la date du {{ date_format(date_create($data->created_at), "d/m/Y") }}. Par ailleurs, {{ $data->clicks }} personnes se sont également intéressées au serveur {{ seocat($catName) }} {{ $data->name }}.
                                    Grâce aux fonctionnalités présents sur le site, il est possible de noter votre expérience sur ce serveur dans le but de faire connaître votre avis aux autres utilisateurs pour conseiller ou non celui-ci. Les avis sont généralement contrôlés pour éviter les abus, mais n'hésitez pas à poster le votre afin de faire évoluer les choix de la communauté du jeu {{ seocat($catName) }}.
                                </p>
                            </div>
                        </div>

                        <div class="single-post d-flex flex-row">
                            <div class="details">
                                <div class="title d-flex flex-row justify-content-between">
                                    <h2>A propos des votes</h2>
                                </div>
                                <p class="margc">
                                    Vous pouvez voter une fois toutes les une heure et demi sur Serveur MultiGames, ce qui représente un bon nombre de votes par jour que vous pouvez effectuer. Voter permet de faire mieux faire connaître le serveur en le faisant augmenter dans le classement (puisque la liste est ordonnée par les votes) pour attirer de nouveaux joueurs sur les plateformes de jeu que vous préférez. De ce fait, vous participez intrinsèquement à l'évolution et à l'attractivé de la communauté de ce jeu.
                                    <br /><br />Serveur MultiGames est un site de classement de serveurs {{ seocat($catName) }} avec un système de vote perfectionné. Les votes sont vérifiés quotidiennement par les administrateurs de la plateforme afin de fournir les données les plus précises. Nous utilisons également les dernières technologies en terme de vérifications et de sécurité, ce qui nous démarque d'autres classements qui n'ont pas nécessairement cette précision puisqu'ils ne sont plus maintenus. Ajouter son serveur sur notre classement, comme le fait {{ $data->name }}, permet de faire concentrer de nombreux joueurs vers des serveurs de qualité et à ce que de nouvelles personnes qui découvrent le jeu rejoignent un travail effectué par des créateurs originaux dans leur contenu.<br /><br />
                                    Avant de voter, vérifiez que vous respectez le règlement. Les serveurs qui ne respectent pas le règlement de notre liste seront contactés et potentiellement cachés de notre classement afin de continuer de proposer au long terme de nouveaux résultats précis et attendus par toute la communauté des joueurs de {{ seocat($catName) }}.
                                </p>
                            </div>
                        </div>

                    <blockquote class="generic-blockquote">
                        Vous avez envie de faire part de votre expérience par rapport au serveur {{ seocat($catName) }} {{ $data->name }} ? Écrivez votre avis sur la page d'<a title="Serveur {{ seocat($catName) }} {{ $data->name}}" href="/{{ encname($catName) }}/{{ encname($data->name) }}">informations de {{ $data->name }}</a>. Écrire votre avis à propos de ce serveur permet de participer à la communauté du jeu et du site.
                    </blockquote>

                    @if (strlen(trim(strtolower($data->ip))) > 0 and strlen(trim(strtolower($playerstats))) > 0)
                        <div class="single-post d-flex flex-row">
                            <div class="details">
                                <h2 class="text-uppercase">Statistiques des joueurs</h2><br />
                                @if (!$data->verified)
                                    <span style="color: red">La propriété doit être validée depuis le tableau de bord du propriétaire du serveur afin que la fonctionnalité des statistiques du serveur puisse fonctionner.</span>
                                @else
                                    <div id="container"></div>
                                @endif
                            </div>
                        </div>
                    @endif
                </div>

                <div class="col-lg-4 sidebar">
                    <div class="single-slidebar" style="background-color: white;">
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated">
                                <ins class="adsbygoogle"
                                     style="display:inline-block;width:300px;height:600px"
                                     data-ad-client="ca-pub-1905923613312160"
                                     data-ad-slot="2719469514"></ins>
                                <script>
                                    (adsbygoogle = window.adsbygoogle || []).push({});
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="single-slidebar">
                        <h3>Serveur ajouté le {{ date_format(date_create($data->created_at), "d/m/Y") }}</h3>
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated"><br />
                                Utilisez les actions rapides ci-dessous pour accéder plus efficacement aux informations du serveur {{ seocat($catName) }} en question.<br /><br />

                                <ul class="cat-list">
                                    <li><a class="justify-content-between d-flex" title="Informations sur le serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}"><span class="lnr lnr lnr-eye"></span> Fiche du serveur</a></li>
                                    @if (!empty($data->website))<li><a class="justify-content-between d-flex" title="Jouer à {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}/go" target="_blank"><span class="lnr lnr-earth"></span> Accéder au site</a></li>@endif
                                    @if (strlen(trim(strtolower($data->ip))) > 0)<li><a class="justify-content-between d-flex" title="Copier l'IP de {{ $data->name }}"  onclick="copy('{{ encname($catName) }}', '{{ seocat($catName) }}', '{{ $data->name }}', '{{ encname($data->name) }}');" class="ipcopy"><span class="lnr lnr-magic-wand"></span> Copier l'adresse IP</a></li>@endif
                                </ul>
                            </div>
                        </div>
                    </div>
                    @if (count(json_decode($data->tag)) > 0 && $data->noredirect == 0)
                        <div class="single-slidebar">
                            <h3>Serveurs {{ seocat($catName) }} similaires</h3>
                            <ul class="cat-list margc">
                                @foreach(json_decode($data->tag) as $k)
                                    <li><a title="{{ seocat($catName) }} {{ $k }}" class="justify-content-between d-flex" href="/{{ $catName }}/tag/{{ enctag($k) }}">{{ ucfirst($k) }}</a></li>
                                 @endforeach
                            </ul>
                        </div>
                    @endif
                    <div class="single-slidebar">
                        <strong>Pourquoi des publicités lors du vote ?</strong>
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated"><br />
                                Le site Serveur MultiGames est gratuit, que ce soit pour voter ou ajouter votre serveur. Cependant, faire vivre un classement, cela coûte de l'argent en terme d'hébergement pour pouvoir gérer le trafic et proposer une plateforme fluide pour trouver vos serveurs de jeu préférés.
                                Notre liste permet de vivre grâce à un modèle publicitaire (qui peuvent être intéressantes puisque parfois ce sont des jeux, même si nous déclinons toute responsabilité vis-à-vis de celles-ci) et grâce à la mise en avant de serveur.<br /><br />
                                Notre classement ne demandera jamais d'argent pour ajouter son serveur ou continuer de vivre. Nous voulons que la liste reste gratuite et elle le restera.
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </section>

@endsection
@section('after_script')

        <script data-pagespeed-no-defer src="/js/ads.js"></script>
    @if (strlen(trim(strtolower($data->ip))) > 0 and strlen(trim(strtolower($playerstats))) > 0)
        <script async src="https://code.highcharts.com/highcharts.js"></script>
        <script async src="https://code.highcharts.com/modules/series-label.js"></script>
        <script async defer>
            Highcharts.chart('container', {
                chart: {
                    backgroundColor: null
                },
                title: {
                    text: ''
                },

                subtitle: {
                    text: ''
                },

                credits: {
                    enabled: false
                },

                xAxis: {
                    type:'datetime',
                },

                plotOptions: {
                    spline: {
                        dataLabels: {
                            enabled: false,
                            crop: true,
                            overflow: 'none',
                        }
                    }
                },

                time: {
                    useUTC: true,
                    timezoneOffset: -2 * 60,
                },

                tooltip: {
                    formatter: function() {
                        return Highcharts.dateFormat('%e/%m/%Y %H:%M', this.x) +
                            ' : <b>'+ this.y +' joueurs connectés</b>';
                    }
                },

                yAxis: {
                    title: {
                        text: ''
                    }
                },

                line: {
                    marker: {
                        enabled: false
                    }
                },

                legend: {
                    enabled: false
                },

                series: [{
                    dataLabels: false,
                    type:'spline',
                    showInLegend: false,
                    name: ' ',
                    data: [{{ $playerstats }}]
                }]
            });
        </script>
    @endif
        @if (!isMobile())
            <script async defer src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
        @endif


        <script defer async src="https://www.google.com/recaptcha/api.js"></script>
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
        <script async defer>
            $(document).ready(function ()
            {
                function flex()
                {
                    $("#blox").append($("#ad2").html());
                    setTimeout(flexar, 1500);
                }

                function flexar()
                {
                    $("#blox").show();
                    $("#vote_button").hide();
                    setTimeout(flexo, 2000);
                }

                function flexo()
                {
                    $("#vote_button").show();
                    $("#ad2").html("");
                }

                if (window.canRunAds === undefined)
                {
                    $("#bma").show();
                    $("#bma").prop("display", "block");
                }
                else
                {
                    $("#vote_button").show();
                    document.getElementById("vote_button").disabled = false;
                    @if (!isMobile())
                        setTimeout(flex, 300);
                    @endif
                }
            });

        </script>
@endsection

@section('title', 'Accéder au serveur '.seocat($catName).' '.$data->name)
@section('description', 'Accédez au site du serveur '.seocat($catName).' '.$data->name.' francophone gratuitement. Ce serveur dispose déjà de plus de '.$data->votes.' votes pour ce serveur '.seocat($catName).' et '.$data->clicks.' clics sur ce serveur pour ce mois. Découvrez ce serveur MultiGames ou votez pour le serveur.')
@section('logometa', 'https://serveur-multigames.net/storage/icone/icon'.$data->id.'.jpg')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName).'/'.encname($data->name).'/vote')
@section('gjs-normal', 'true')
@section('robots', 'noindex, nofollow')
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

<META http-equiv="refresh" content="5;URL={{ $website }}">
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
                        <h4 class="text-white">Accéder au serveur {{ seocat($catName) }}</h4>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-4 align-self-center">
                <ul class="list-unstyled mb-0 lis-line-height-2 text-md-right text-center">
                    <li>
                        <i class="fa fa-map-o pr-2"></i> <a title="Serveur MultiGames" href="/" class="text-white">Accueil</a> /
                        <a title="Liste {{ seocat($data->cat) }}" href="/{{ encname($catName) }}" class="text-white">{{ seocat($data->cat) }}</a> /
                        <a title="Serveur {{ seocat($data->cat) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}" class="text-white">{{ $data->name }}</a> /
                        <a title="Accéder au serveur {{ seocat($data->cat) }} {{ $data->name }}" href="/{{ encname($catName) }}/go" class="text-white">Accéder</a>
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
                &nbsp; <a title="Voter pour le serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}/vote" class="btn btn-primary btn-default"> <i class="fa fa-gamepad"></i> Voter</a>
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
                <div class="tab-pane fade show active" id="venue" role="tabpanel" aria-labelledby="venue">
                    <h6 class="lis-font-weight-500">
                        <i class="fa fa-align-right pr-2 lis-f-14"></i> Accéder au serveur
                    </h6>

                    <div class="alert alert-success">
                        <img alt="Chargement du vote pour {{ $data->name }}" title="Chargement du vote pour {{ $data->name }}" src="/img/loading.gif" width="64" height="64" /> Tu vas être redirigé vers le serveur {{ $catName }} {{ $data->name }} dans quelques secondes.
                    </div><!-- responsive -->
                    <ins class="adsbygoogle"
                         style="display:block"
                         data-ad-client="ca-pub-4636627444279583"
                         data-ad-slot="8514750542"
                         data-ad-format="auto"
                         data-full-width-responsive="true"></ins>
                    <script>
                        (adsbygoogle = window.adsbygoogle || []).push({});
                    </script>
                    <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Informations sur le serveur </h6>
                    <div class="card lis-brd-light mb-4 wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                        <div class="card-body p-4">
                            Le serveur {{ $data->name }}, inscrit et actif sur le classement MultiGames dans la catégorie {{ $data->cat }} et comportant {{ $data->reviews }} d'une note moyenne de {{ $data->note }} @if (strlen($data->ip) > 0) l'adresse IP {{ $data->ip }} pour rejoindre le serveur en question. @else est actuellement joignable directement depuis les plateformes de jeu en question. @endif Depuis le début du mois, le serveur décrit possède {{ $data->votes }} votes à partir de notre site, depuis le début du mois. Il est possible de découvrir de nouveaux serveurs comme celui-ci, créé à la date du {{ date_format(date_create($data->created_at), "d/m/Y") }}. Par ailleurs, {{ $data->clicks }} personnes se sont également intéressées au serveur {{ seocat($catName) }} {{ $data->name }}.
                            Grâce aux fonctionnalités présents sur le site, il est possible de noter votre expérience sur ce serveur dans le but de faire connaître votre avis aux autres utilisateurs pour conseiller ou non celui-ci. Les avis sont généralement contrôlés pour éviter les abus, mais n'hésitez pas à poster le votre afin de faire évoluer les choix de la communauté du jeu {{ seocat($catName) }}.
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    </div>
</section>

@endsection
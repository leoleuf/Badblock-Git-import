@if (isset($tag))
    @php($shownTag = ' '.ucfirst($tag))
@else
    @php($shownTag = '')
@endif

@if ($current_page > 1)
    @php($pTitle = ' Page '.$current_page)
    @php($pDesc = ' de la page '.$current_page)
@else
    @php($pTitle = '')
    @php($pDesc = '')
@endif

@php($transperant = 'true')
@php($classement = 'true')

@if (encname($catName) == "minecraft")
    @section('title', 'Serveur '.seocat($catName).$shownTag.$pTitle.' : Liste Complète Française De '.$about->serveurCount.' Serveurs Gratuit')
    @section('description', 'Découvre la liste de serveur Minecraft'.$shownTag.' gratuit complète. Joue à '.$about->serveurCount.' serveurs'.$shownTag.' français, du serveur cracké, Survie, PVP Faction, Moddé, Premium et plus.')
@elseif (encname($catName) == "clash-of-clans")
    @section('title', 'Liste de Serveur privé Clash Of Clans : COC sur Android PC iOS')
    @section('description', 'Découvre ton serveur privé Clash Of Clan 2019 gratuit préféré. Depuis un fichier APK Android ou des serveurs Clash Royale hdv 12 iOS possibles à télécharger avec code, découvre sur PC le jeu COC dès maintenant.')
@elseif (encname($catName) == "hytale")
    @section('title', 'Serveur Hytale : Télécharger le jeu Hytale maintenant')
    @section('description', 'Télécharge Hytale et rejoins ton serveur privé Hytale gratuit préféré. La date de sortie, le trailer... Tout à savoir sur le jeu Hytale développé par Hypixel Studio, prochain phénomène qui va attirer de nombreux serveurs originaux.')
@else
    @section('title', 'Serveur '.seocat($catName).$shownTag.$pTitle.' : Liste Complète Française De '.$about->serveurCount.' Serveurs Gratuit')
    @section('description', 'Découvre la liste de serveur '.seocat($catName).' gratuit la plus complète de France. Ainsi, joue à '.$about->serveurCount.' serveurs proposés dans le classement. Des serveurs de toutes les catégories pour tous les goûts sont à rejoindre dès maintenant.')
@endif

@if (encname($catName) == "minecraft")
    @section('logometa', 'https://serveur-multigames.net/img/minecraft/minecraft.png')
@elseif (encname($catName) == "hytale")
    @section('logometa', 'https://serveur-multigames.net/img/hytale.jpg')
@else
    @section('logometa', 'https://serveur-multigames.net/img/logo.png')
@endif

@if (isset($tag))
    @if ($current_page > 1)
        @php($next_canonical = '/tag/'.enctag($tag).'/'.$current_page)
    @else
        @php($next_canonical = '/tag/'.enctag($tag))
    @endif
@elseif ($current_page > 1)
    @php($next_canonical = '/page/'.$current_page)
@else
    @php($next_canonical = '')
@endif

@section('canonical', 'https://serveur-multigames.net/'.$catName.$next_canonical)

@if ($current_page > 1)
    @if (isset($tag))
        @php($prev = 'https://serveur-multigames.net/'.$catName.'/tag/'.enctag($tag).'/'.($current_page - 1))
    @elseif ($current_page < 3)
        @php($prev = 'https://serveur-multigames.net/'.$catName)
    @else
        @php($prev = 'https://serveur-multigames.net/'.$catName.'/page/'.($current_page - 1))
    @endif
@else
    @php($prev = '')
@endif

@if ($page_number > $current_page)
    @if (isset($tag))
        @php($next = 'https://serveur-multigames.net/'.$catName.'/tag/'.enctag($tag).'/'.($current_page + 1))
    @else
        @php($next = 'https://serveur-multigames.net/'.$catName.'/page/'.($current_page + 1))
    @endif
@else
    @php($next = '')
@endif

@section('next', $next)
@section('prev', $prev)

@section('jquery', 'async defer')

@extends('front.index')
@section('hdr')
@endsection
@section('content')

        <section class="image-bg lis-grediant grediant-bt-dark text-white pb-4 profile-inner">
            <div class="background-image-maker"></div>
            <div class="holder-image"> <img src="/img/header-bg-{{ $catName }}.jpg" alt="Serveur {{ $catName }}" class="img-fluid d-none"> </div>
            <div class="container">
                <div class="row justify-content-center wow fadeInUp">
                    <div class="col-12 col-md-8 mb-4 mb-lg-0">
                            <div class="media d-block d-md-flex text-md-left text-center"> <img src="/img/{{ $catName }}/{{ $catName }}.png" class="img-fluid d-md-flex mr-4 border border-white lis-border-width-4 rounded mb-4 mb-md-0" alt="Serveur {{ $catName }} gratuit complet pc cracké" />
                                <div class="media-body align-self-center">
                                    @if (isset($catName) && $catName != "clash-of-clans" && $catName != "hytale")
                                        @if (isset($tag))
                                            <h1 class="text-white font-weight-bold lis-line-height-1">Serveur {{ seocat($catName) }} {{ ucfirst($tag)}}</h1>
                                            <h2 class="mb-0 text-white titre">Liste de {{ $about->serveurCount }} serveurs {{ seocat($catName) }}</h2>
                                        @else
                                            <h1 class="text-white font-weight-bold lis-line-height-1">Serveur {{ seocat($catName) }}</h1>
                                            <h2 class="mb-0 text-white titre">Liste de {{ $about->serveurCount }} serveurs {{ seocat($catName) }}</h2>
                                        @endif
                                    @elseif (isset($catName) && $catName == "clash-of-clans")
                                        <h1 class="text-white">Télécharger Clash Of Clans iOs Android</h1>
                                        <h2 class="mb-0 text-white titre">Installer sur PC et jouer aux serveurs</h2>
                                    @elseif (isset($catName) && $catName == "hytale")
                                        <h1 class="text-white">Télécharger Hytale</h1>
                                        <h2 class="mb-0 text-white titre">Installer sur PC et jouer aux serveurs</h2>
                                    @endif
                                </div>
                            </div>
                    </div>
                    <div class="col-12 col-md-4 align-self-center">
                        <ul class="list-unstyled mb-0 lis-line-height-2 text-md-right text-center">
                            <li>
                                <i class="fa fa-map-o pr-2"></i> <a title="Serveur MultiGames" href="/" class="text-white">Accueil</a> /
                                        <a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}" class="text-white">{{ seocat($catName) }}</a>
                                        @if (isset($tag)) / <a title="Serveur {{ $catName }} {{ $tag }}" href="/{{ $catName }}/tag/{{ enctag($tag) }}" class="text-white">{{ ucfirst($tag) }}</a>@endif
                                        @if ($current_page > 1) / <a title="Serveur {{ $catName }}@if (isset($tag)) {{ $tag }}@endif page {{ $current_page }}" href="/{{ $catName }}@if (isset($tag))/tag/{{ enctag($tag) }}@endif/page/{{ $current_page }}" class="text-white">Page {{ $current_page }}</a>@endif
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </section>

        <div class="profile-header">
            <div class="container">
                <div class="row">
                    <div class="col-10 col-xl-9 order-xl-1 order-2 text-xl-right text-center">
                        <ul class="nav nav-pills flex-column flex-sm-row lis-font-poppins" id="myTab" role="tablist">
                            @if (isset($tag))
                                <li class="nav-item ml-0"> <a title="Liste de serveur gratuit {{ $catName }}" class="nav-link lis-light py-4 lis-relative mr-3" href="/{{ $catName }}"> Tous</a> </li>
                            @else
                                <li class="nav-item ml-0"> <a title="Liste de serveur gratuit {{ $catName }}" class="nav-link lis-light py-4 lis-relative mr-3 active" data-toggle="tab" href="#" role="tab" aria-expanded="true"> Tous</a> </li>
                            @endif

                            @php($bro = 0)
                            @foreach($tags as $k => $v)
                                @if ($v > 0 && $bro < 6)
                                    @php($bro = $bro + 1)
                                    <li class="nav-item ml-0">
                                        <a title="Serveur {{ seocat($catName) }} {{ $k }}" class="nav-link lis-light py-4 lis-relative mr-3 @if (isset($tag) && $tag == $k) active @endif" href="/{{ $catName }}/tag/{{ enctag($k) }}">{{ ucfirst($k) }} ({{ $v }})</a>
                                    </li>
                                @endif
                            @endforeach
                        </ul>
                    </div>
                    <div class="col-12 col-xl-3 align-self-center order-xl-2 order-1 text-xl-right text-center mt-4 mt-xl-0">
                        <a title="Ajouter mon serveur {{ seocat($catName) }}" href="/add-server" class="btn btn-primary btn-default"> <i class="fa fa-plus pr-1"></i> Ajouter mon serveur</a>
                    </div>
                </div>
                </div>
            </div>
        </div>

        <section class="lis-bg-light pt-2">
            <div class="container">
                <div class="row">
                    <div class="col-12 col-lg-8 mb-1 mb-lg-0">
                        @if (isset($votelistok) && $votelistok)
                            <div class="alert alert-success">
                                &nbsp;<strong>Merci !</strong> Votre vote a bien été pris en compte. Merci de votre soutien.<br />
                                Vous pouvez continuer de consulter le classement {{ seocat($catName) }}.
                            </div>
                        @endif
                        <div class="tab-content" id="myTabContent">
                            <div class="tab-pane fade show active" id="venue" role="tabpanel" aria-labelledby="venue">
                                @if ($catName == "clash-of-clans")
                                    <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Clash Of Clans - Installer</h3>
                                @elseif ($catName == "hytale")
                                    <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Hytale - Télécharger et Jouer</h3>
                                @else
                                    <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Informations sur le classement {{ seocat($catName) }}</h3>
                                @endif
                                <div class="card lis-brd-light mb-1 wow fadeInUp">
                                    <div class="card-body p-4">
                                        <p>
                                            @if (isset($tag))
                                                Vous êtes sur la liste {{ seocat($catName) }} {{ $tag }}. La liste est composée des tags {{ $tag }}, qui sont les seuls à être présentés dans ce classement. Vous pouvez voter ou découvrir de nouvelles expériences sur le jeu. Vous souhaitez ajouter votre serveur {{ $tag }} ? <a title="Ajouter mon serveur {{ seocat($catName) }} {{ $tag }}" href="/add-server">Ajoutez-le</a> dès maintenant sur notre classement, gratuitement.
                                            @else
                                                @if (isset($catName) && $catName == "clash-of-clans")
                                                    Vous pouvez télécharger votre serveur privé Clash Of Clans favori par le Play Store, Apple Store ou toutes les plateformes légales et légitimes sans plus hésiter. Il est possible de promouvoir votre serveur Clash Of Clans directement sur cette page. A partir des mises à jour de Clash Of Clans, vous pouvez vous tenir au courant des toutes dernières nouveautés par l'éditeur Supercell. Les images Clash Of Clans utilisés sur cette page sont la propriétés de Supercell et sont utilisés dans un cadre complètement informatif. Forum Clash Of Clans et Wiki disponibles également sur plusieurs sites relatifs au jeu PC Mobile.
                                                @elseif (isset($catName) && $catName == "hytale")
                                                    Sur cette page, vous pourrez promouvoir votre serveur privé Hytale quand le jeu sera sorti. Les dernières mises à jour du jeu ainsi que d'autres informations concernant le trailer, le modding ou le système de serveur du jeu seront publiés directement sur cette page.
                                                @else
                                                    La liste complète de serveur {{ seocat($catName) }} gratuit est composée de <strong>nombreux serveurs</strong> à découvrir. Vous êtes un joueur de {{ seocat($catName) }} ? Un serveur Cracké gratuit, SkyBlock, Faction, PvP Moddé, Survie, PE ou RP et bien plus encore sont à découvrir, avec un <b>large choix de {{ $about->serveurCount }} serveurs {{ seocat($catName) }}</b> gratuits, où vous pouvez vous y connecter à partir de cette liste, pour jouer à {{ seocat($catName) }}, qui est classée par rapport au nombre de votes effectués par les joueurs.
                                                    @if (isset($addon))
                                                        {!! $addon !!}
                                                    @endif
                                                @endif
                                            @endif
                                        </p>
                                    </div>
                                </div>
                                @php($adsDone = 0)
                                @php($normalDone = 0)

                                @php($totalReviews = 0)
                                @php($average = 0)
                                @php($averageCount = 0)
                                    @if (encname($catName) == "clash-of-clans")
                                    <!-- responsive -->
                                    <ins class="adsbygoogle"
                                         style="display:block"
                                         data-ad-client="ca-pub-4636627444279583"
                                         data-ad-slot="8514750542"
                                         data-ad-format="auto"
                                         data-full-width-responsive="true"></ins>
                                    <script>
                                        (adsbygoogle = window.adsbygoogle || []).push({});
                                    </script><br />
                                        <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Télécharger {{ seocat($catName) }} sans plus attendre</h3>
                                        <div class="card lis-brd-light mb-1 wow fadeInUp">
                                            <div class="card-body p-4">
                                                <p>
                                                    Vous pouvez télécharger votre serveur privé Clash Of Clans favori par le Play Store, Apple Store ou toutes les plateformes légales et légitimes sans plus hésiter. Il est possible de promouvoir votre serveur Clash Of Clans directement sur cette page. A partir des mises à jour de Clash Of Clans, vous pouvez vous tenir au courant des toutes dernières nouveautés par l'éditeur Supercell. Les images Clash Of Clans utilisés sur cette page sont la propriétés de Supercell et sont utilisés dans un cadre complètement informatif. Forum Clash Of Clans et Wiki disponibles également sur plusieurs sites relatifs au jeu PC Mobile.
                                                </p>
                                            </div>
                                        </div>
                                        <!-- serveur-multigames -->
                                        <ins class="adsbygoogle"
                                             style="display:block"
                                             data-ad-client="ca-pub-4636627444279583"
                                             data-ad-slot="8514750542"
                                             data-ad-format="auto"
                                             data-full-width-responsive="true"></ins>
                                        <script>
                                            (adsbygoogle = window.adsbygoogle || []).push({});
                                        </script><br />
                                        <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Liste serveur {{ seocat($catName) }} privé</h3>
                                        <div class="card lis-brd-light mb-1 wow fadeInUp">
                                            <div class="card-body p-4">
                                                <p>
                                                    Viens jouer avec tes amis sur ton serveur Clash Of Clans préféré directement en téléchargeant le jeu depuis le site Internet de Serveur MultiGames, avec de nombreux avantages à découvrir, disponible directement sur Android, sur iOs avec la possibilité de passer HD v12. Ce jeu est disponible en version 2018 et 2019, vous pouvez donc jouer gratuitement à ce jeu sans plus attendre.<br /><br />
                                                    Plusieurs serveurs Clash Of Clans privés existent mais ne sont pas forcément autorisés par les créateurs du jeu. En revanche, nous vous conseillons de jouer à ce serveur Clash Of Clans dès maintenant sans plus hésiter. Ce jeu vidéo sur mobile est sorti il y a déjà longtemps. La stratégie en temps réel est un mode de jeu qui est très apprécié par la communauté, ce qui invite aussi les joueurs de Clash Of Clans à développer leurs propres serveurs disponibles sur plusieurs plateformes (iOS et Android). Jouez à ce serveur Clash Of Clans dès maintenant et découvrez-le sans plus hésiter, un jeu Pegi 7 et tout public qui n'attend plus que vous avec un large choix de serveurs différents. Pourquoi patienter pour aller jouer sur votre serveur Clash Of Clans privé disponible depuis notre liste ? Il est possible de télécharger le jeu maintenant sur mobile ou sur une plateforme correspondante. Le téléchargement est rapide.
                                                    8 avis ont été postés pour les fiches de serveurs Clash of Clans avec une moyenne de 5. <b>Téléchargez le launcher Clash Of Clans ci-dessous sans plus attendre</b>
                                                </p>
                                                @if (isset($addon))
                                                    {!! $addon !!}
                                                @endif
                                            </div>
                                        </div>
                                    @elseif (encname($catName) == "hytale")
                                        <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Télécharger {{ seocat($catName) }} sans plus attendre</h3>
                                        <div class="card lis-brd-light mb-1 wow fadeInUp">
                                            <div class="card-body p-4">
                                                <p>
                                                    Le jeu Hytale, développé par Hypixel Studio, est un jeu qui devrait sortir très prochainement. Vous pourrez ainsi télécharger Hytale sans attendre trop longtemps puisqu'un trailer est déjà sorti, et beaucoup de rumeurs circulent à propos des nouveautés, de la release date (la date de sortie). Certains joueurs s'emparent déjà de leur hype pour chercher le jeu sur PS4, alors que pour le moment il est annoncé être disponible principalement sur les PC, Windows Mac et Linux, les trois principaux noyaux/systèmes d'exploitation les plus utilisés. Rien ne dit qu'une version PS4 ne sortira pas, mais pour le moment il est prévu de pouvoir le découvrir très prochainement.<br /><br />
                                                    Pourquoi ne pas le télécharger et l'installer dès maintenant ?
                                                </p>
                                            </div>
                                        </div>
                                        <!-- serveur-multigames -->
                                        <ins class="adsbygoogle"
                                             style="display:block"
                                             data-ad-client="ca-pub-4636627444279583"
                                             data-ad-slot="8514750542"
                                             data-ad-format="auto"
                                             data-full-width-responsive="true"></ins>
                                        <script>
                                            (adsbygoogle = window.adsbygoogle || []).push({});
                                        </script><br />
                                        <h3 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Liste serveur {{ seocat($catName) }} privé</h3>
                                        <div class="card lis-brd-light mb-1 wow fadeInUp">
                                            <div class="card-body p-4">
                                                <p>
                                                    Avec trois modes, dont un survie, un multi et même un mode pour les mini-jeux, le joueur ne devrait pas avoir à s'ennuyer sur le jeu Hytale. Une intégration est prévue pour pouvoir directement disposer de la liste des serveurs directement sur le multi d'Hytale et Serveur MultiGames veut vous proposer une liste française des serveurs Hytale qui seront prochainement disponibles.<br /><br />
                                                    N'hésitez donc pas à vous jeter sur cette occasion pour découvrir le trailer et pourquoi pas, devenir un serveur Hytale français réputé quand le jeu sera sorti. Le modding sera incorporé directement dans le serveur de jeu, ce qui permettra donc d'éviter les cheats (aussi appelés clients de triche) sur Hytale.

                                                    N'hésite pas à jouer sur ton futur serveur Hytale préféré en revenant plusieurs fois à la pêche aux informations puisque nous proposerons des modifications de la page dès que nous aurons plus d'informations sur la sortie du jeu, sur le fonctionnement des serveurs et sur les possibilités que promettent le jeu Hytale 2019. Ce jeu sera peut-être porté sur mobile et sur les autres plateformes s'il atteint le même succès que Minecraft.
                                                </p>
                                                @if (isset($addon))
                                                    {!! $addon !!}
                                                @endif
                                            </div>
                                        </div>
                                    @else
                            @foreach($data as $row)
                                    @if ($adsDone == 0 && isset($row->ad))
                                        @php($adsDone = 1)
                                        <h3 class="lis-font-weight-500 mt-1"><i class="fa fa-align-right pr-2 lis-f-14"></i> Liste {{ $catName }} en avant
                                            @if (!isMobile())
                                            <a title="Mettre mon serveur {{ seocat($catName) }} en avant" href="/mise-en-avant" class="btn btn-success btn-avant text-xl-right text-center mt-4 mt-xl-0" style="float: right;"><i class="fa fa-trophy pr-1"></i>Mettre mon serveur en avant</a>
                                            @endif
                                        </h3>
                                    @elseif ($normalDone == 0 && !isset($row->ad))
                                        @php($normalDone = 1)
                                        <h3 class="lis-font-weight-500 mt-1"><i class="fa fa-align-right pr-2 lis-f-14"></i> Liste {{ $catName }} complète</h3>
                                    @endif
                                        <script>
                                            function copy(str) {
                                                const el = document.createElement('textarea');
                                                el.value = str;
                                                el.setAttribute('readonly', '');
                                                el.style.position = 'absolute';
                                                el.style.left = '-9999px';
                                                document.body.appendChild(el);
                                                el.select();
                                                document.execCommand('copy');
                                                document.body.removeChild(el);
                                                alert('Adresse IP copiée : ' + str);
                                            }
                                        </script>
                                <div class="card @if (isset($row->ad)) lis-brd-avant @else lis-brd-light @endif wow fadeInUp mb-1">
                                    <div class="card-body p-4">
                                        <div class="media d-md-flex d-block text-center text-md-left">
                                            @if (isset($row->ad) && !empty($row->website))
                                                <a title="Serveur {{ seocat($catName) }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/go" rel="noopener nofollow noreferrer external"><img alt="{{ $row->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $row->id }}.jpg" class="img-fluid d-md-flex mr-0 mr-md-1 rounded " height="69" width="69"></a>
                                            @else
                                                <a title="Informations du serveur {{ seocat($catName) }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}" rel="noopener nofollow noreferrer external"><img alt="{{ $row->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $row->id }}.jpg" class="img-fluid d-md-flex mr-0 mr-md-1 rounded " height="69" width="69"></a>
                                            @endif
                                            <div class="media-body align-self-center mt-4 mt-md-0">
                                                @if (isset($row->ad) && !empty($row->website))
                                                    <span class="mb-0 lis-font-weight-500">
                                                        <a title="Serveur {{ seocat($catName) }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/go" class="lis-dark">{{ $row->name }}</a>
                                                        @if (!empty($row->ip))
                                                            &nbsp;<span style="background-color: #ecf0f1; border-radius: 8px; padding: 3px; font-size: 1.0rem;">IP : {{ $row->ip }}</span>
                                                            <a title="Copier l'IP du serveur {{ seocat($catName) }} {{ $row->name }}" href="#" onclick="copy('{{ $row->ip }}');" class="btn"><i class="fa fa-copy pr-1" style="color: #000;"></i></a>
                                                        @endif
                                                    </span>

                                                @else
                                                    <span class="mb-0 lis-font-weight-500"><a title="Informations du serveur {{ seocat($catName) }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}" class="lis-dark">{{ $row->name }}</a></span>
                                                @endif
                                                <ul class="list-inline mb-0 lis-light-gold font-weight-normal">
                                                    @if($row->note >= 1)
                                                        <li class="list-inline-item fa fa-star"></li>
                                                    @else
                                                        <li class="list-inline-item fa fa-star-o"></li>
                                                    @endif

                                                    @if($row->note > 2 || $row->note == 2)
                                                            <li class="list-inline-item fa fa-star"></li>
                                                    @else
                                                            <li class="list-inline-item fa fa-star-o"></li>
                                                    @endif

                                                    @if($row->note > 3 || $row->note == 3)
                                                            <li class="list-inline-item fa fa-star"></li>
                                                    @else
                                                            <li class="list-inline-item fa fa-star-o"></li>
                                                    @endif

                                                    @if($row->note > 4 || $row->note == 4)
                                                        <li class="list-inline-item fa fa-star"></li>
                                                    @else
                                                            <li class="list-inline-item fa fa-star-o"></li>
                                                    @endif

                                                    @if($row->note == 5)
                                                            <li class="list-inline-item fa fa-star"></li>
                                                    @else
                                                            <li class="list-inline-item fa fa-star-o"></li>
                                                    @endif
                                                </ul>
                                                @php($totalReviews += $row->reviews * 7)
                                                @php($average += ($row->note == 0) ? 5 : $row->note)
                                                @php($averageCount++)

                                                <p class="row col-md-12 lis-font-weight-300">
                                                        @php($dozpa = substr_count(mb_strimwidth($row->short_desc, 0, 501, "..."), "\r") + substr_count(mb_strimwidth($row->short_desc, 0, 501, "..."), "\n"))
                                                        @if (intval($dozpa) > 6)
                                                        {{ preg_replace( "/\r|\n/", "", mb_strimwidth($row->short_desc, 0, 501, "...")) }}
                                                        @else
                                                        {!! nl2br(htmlentities(htmlspecialchars(mb_strimwidth($row->short_desc, 0, 501, "...")))) !!}
                                                        @endif
                                                </p>
                                                <ul class="list-inline my-0">
                                                    @if (!empty($row->website))&nbsp;
                                                        <li class="list-inline-item"><a title="Jouer à {{ $catName }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/go" rel="noopener" target="_blank" class="lis-light lis-jouer border lis-brd-light text-center lis-line-height-2_3 rounded d-block"><i class="fa fa-gamepad"></i> Jouer</a></li>
                                                    @endif
                                                    <li class="list-inline-item"><a title="Voter pour {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/vote" class="lis-light lis-vote border lis-brd-light text-center lis-line-height-2_3 rounded d-block"><i class="fa fa-check-circle-o"></i> Voter</a></li>

                                                    <li class="list-inline-item serveur-certif{{ $row->verified }}">
                                                        @if ($row->verified)
                                                            <i class="fa fa-check" aria-hidden="true"></i> Serveur certifié
                                                        @else
                                                            <i class="fa fa-exclamation-triangle" aria-hidden="true"></i> Serveur non certifié
                                                        @endif
                                                    </li>

                                                    <li class="list-inline-item votes">
                                                        {{ $row->votes }} votes | {{ $row->clicks }} clics
                                                    </li>

                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                @endforeach
                                <h3 class="lis-font-weight-500 mt-4"><i class="fa fa-align-right pr-2 lis-f-14"></i> Jeu {{ $catName }} serveur</h3>
                                <div class="card lis-brd-light wow fadeInUp mb-4">
                                    <div class="card-body p-4">
                                        <div class="media d-md-flex d-block text-center text-md-left">
                                                <div class="row">
                                                    <div class="col-md-4 single-defination">
                                                        <h3 class="mb-20">Votes de serveur</h3>
                                                        <p><br />Ce mois-ci, {{ $about->voteCount }} votes ont été effectués pour des serveurs {{ seocat($catName) }}. Vote pour ton serveur préféré depuis le classement des meilleurs serveurs {{ seocat($catName) }}{{$shownTag}} sans plus hésiter.</p>
                                                    </div>
                                                    <div class="col-md-4 single-defination">
                                                        <h3 class="mb-20">Classement</h3>
                                                        <p><br />Nous disposons d'une large liste complète de {{ $about->serveurCount }} serveurs et nous comptons encore nous étendre. Vous n'êtes pas encore dessus ? C'est le moment de s'y inscrire et d'ajouter le vôtre gratuitement.</p>
                                                    </div>
                                                    <div class="col-md-4 single-defination">
                                                        <h3 class="mb-20">Avis sur {{ seocat($catName) }}</h3>
                                                        <p><br />Il y a eu plus de {{ $about->clickCount }} personnes qui ont découvert un serveur {{ seocat($catName) }}{{$shownTag}} depuis le début du mois. Nous travaillons sur la visibilité des créateurs tous les jours et cela n'est pas prêt de s'arrêter.</p>
                                                    </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                    @endif
                                <nav>
                                    <ul class="pagination list-inline mb-0 text-center text-uppercase lis-f-14 justify-content-center mt-5">
                                        @if ($current_page > 1)
                                            <li class="p-1 page-item"><a title="Classement complet de serveur {{ seocat($catName) }} page précédente" class="page-link lis-brd-light lis-light rounded" href="/{{ $catName }}@if ($current_page > 2)/page/{{ ($current_page-1) }}@endif"><i class="fa fa-angle-left pr-1"></i> Précédent</a></li>
                                        @endif

                                        @php($next = 0)
                                        @for($i = 1;$page_number +1 > $i;$i++)
                                            @if ($i > $current_page)
                                                @php($next = 1)
                                            @endif
                                            <li class="p-1 page-item d-none d-sm-inline-block @if ($i == $current_page) active @endif"><a title="Serveur {{ seocat($catName) }} @if ($i > 1) page {{ $i }} @endif" class="page-link lis-light rounded" href="/{{ $catName }}@if($i > 1)/page/{{ $i }}@endif">{{ $i }}</a></li>
                                        @endfor

                                        @if ($next == 1)
                                            <li class="p-1 page-item"><a title="Classement complet de serveur {{ seocat($catName) }} page suivante" class="page-link lis-brd-light lis-light rounded" href="/{{ $catName }}/page/{{ ($current_page+1) }}"><i class="fa fa-angle-right pr-1"></i> Suivant</a></li>
                                        @endif
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-lg-4">
                        <ins class="adsbygoogle"
                                                       style="display:block"
                                                       data-ad-client="ca-pub-4636627444279583"
                                                       data-ad-slot="8514750542"
                                                       data-ad-format="auto"
                                                       data-full-width-responsive="true"></ins>
                        <script>
                            (adsbygoogle = window.adsbygoogle || []).push({});
                        </script>
                        <h6 class="lis-font-weight-500 mt-4"><i class="fa fa-align-right pr-2 lis-f-14"></i> Informations sur le jeu {{ seocat($catName) }}</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                @if (encname($catName) == "minecraft")
                                    <img alt="Serveur minecraft" src="/img/minecraft/serveur-minecraft.png" height="76" class="serveur-img" />
                                @endif
                                Minecraft est un jeu bac à sable populaire facile à télécharger et à installer sur PC, connu notamment pour son attractivité et le monde libre qu'il propose, notamment avec le mode multijoueur (ses inombrables serveurs de jeu) et mini-jeux, ce qui permet à de nombreux créateurs d'imaginer de nouveaux concepts. Notre liste propose ainsi aux joueurs de découvrir un large choix d'opportunités et de créations.
                                <br />
                                Il est d'ailleurs possible d'intégrer Minecraft gratuit complet à Steam pour jouer avec ses amis.
                                <p class="mb-0">
                                    @if(Auth::user())
                                        <br />
                                        <a title="Ajouter son serveur Minecraft" class="lis-light border lis-brd-light text-center lis-line-height-2_3 rounded d-block" href="/dashboard/add-server"><span class="fa fa-plus-circle" id="serveur-plus"></span> Ajouter mon serveur Minecraft</a>
                                        @if (!isset($ad))
                                            <a title="Mettre en avant mon serveur" class="button-pad lis-light border lis-brd-light text-center lis-line-height-2_3 rounded d-block" id="ajout-serveur" href="/dashboard/mise-en-avant"><span class="fa fa-star" id="serveur-plus"></span> Mettre en avant mon serveur</a>
                                        @endif
                                    @else
                                        <br />
                                        <a title="Ajouter son serveur {{ seocat($catName) }}" class="lis-light border lis-brd-light text-center lis-line-height-2_3 rounded d-block" id="ajout-serveur" href="/add-server"><span class="fa fa-plus-circle" id="serveur-plus"></span> Ajouter mon serveur Minecraft</a>
                                        <a title="Mettre mon serveur {{ seocat($catName) }} en avant" class="button-pad lis-light border lis-brd-light text-center lis-line-height-2_3 rounded d-block" href="/mise-en-avant"><span class="fa fa-star"></span> Mettre en avant mon serveur</a>
                                        <a title="Installer {{ seocat($catName) }} gratuit PC" class="button-pad lis-light border lis-brd-light text-center lis-line-height-2_3 rounded d-block" href="https://launcher-minecraft.com/fr/telecharger"><span class="fa fa-download"></span> Télécharger Minecraft gratuit sur PC</a>
                                        <a title="Raccourcir un lien" class="button-pad lis-light border lis-brd-light text-center lis-line-height-2_3 rounded d-block" href="https://monlien.net/">Raccourcir URL</a>
                                    @endif
                                </p>
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Ajouter mon serveur</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Vous souhaitez donner plus de visibilité à votre serveur {{ seocat($catName) }} sans plus tarder ? Serveur MultiGames vous propose de rejoindre plus de 100 créateurs de serveurs de jeu différents sur la plateforme pour faire découvrir votre serveur à la communauté francophone.
                                <br /><br />
                                Les joueurs peuvent ainsi voter pour le serveur qu'ils préfèrent afin qu'il soit plus visible dans notre classement complet pour attirer de nouveaux joueurs tous les jours.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-144"></i> Télécharger Minecraft</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Télécharger Minecraft complet à tout moment en version gratuit, c'est entièrement possible. Référez-vous aux boutons au dessus pour l'installer et rejoindre nos serveurs de jeu préférés et français sur la gauche.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> L'importance de votre serveur</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Ajouter son serveur (gratuitement) sur nos classements est une tâche aussi importante que de trouver le bon nom du serveur que vous souhaitez mettre en avant. Sans visibilité, il n'est pas utile de créer un serveur {{ seocat($catName) }} public, sans limite. C'est pour cela qu'un site {{ strtolower(seocat($catName)) }} est intéressant pour référencer son serveur privé pour le faire découvrir aux joueurs de la communauté.<br /><br />Serveur MultiGames offre un panel pour y ajouter son propre serveur et consulter les statistiques de votre présence dans le top {{ strtolower(seocat($catName)) }}.<br />
                                Une trentaine de serveurs de ce type constatent déjà une amélioration de leur visibilité ainsi que de leur rendements. C'est tout l'objectif de Serveur MultiGames, qui est de proposer gratuitement cette plateforme de découverte communautaire gratuitement, en échange d'un soutien envers le travail effectué comme en parler autour de vous, pour développer encore plus les communautés et rendre la meilleure expérience de jeu possible pour l'utilisateur final.<br /><br />L'ajout d'un serveur sur nos plateformes est un processus très simple. Il y est possible de faire certifier son serveur en validant sa propriété facilement à l'aide d'un lien
                                pour respecter au maximum les règles de notre classement, notamment le fait de n'ajouter que son serveur de jeu et éviter d'ajouter ceux qui n'appartiennent pas aux personnes qui ajoutent le serveur en question, pour des questions de sécurité, de droits sur le contenu textuel, le logo et la bannière. En quelques clics, l'ajout peut se faire et votre visibilité peut se multiplier par dix en seulement quelques minutes !<br /><br />La présence de Serveur MultiGames sur les sites et les moteurs permet ainsi de donner de la visibilité à tous ces créateurs qui le méritent par le biais de vote. Notre classement se démarque par le bouton "Jouer" qui permet aux joueurs de découvrir un serveur en seulement quelques clics, sans avoir à payer quoi que ce soit. Aucun frais caché, inscrire son serveur est gratuit et se fait en quelques minutes.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Conseils pour lancer son serveur {{ seocat($catName) }}</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Créer un serveur {{ seocat($catName) }} n'est pas une tâche facile.
                                Avec de la créativité, une motivation hors du commun et une passion pour le jeu,
                                il est possible d'en faire un. Après avoir trouvé un concept attirant envers les
                                joueurs, il est nécessaire de commencer dans l'ordre des choses. Les connaissances
                                en hébergement et en stabilité du réseau, des outils nécessaires à la conception sont
                                utiles pour bien se lancer. Après avoir trouvé l'hébergeur qui convient et avoir mis
                                en place son serveur, les réseaux sociaux sont très importants à développer pour l'image
                                du projet, cela devient de plus en plus obligatoire de communiquer à travers les nouveaux
                                médias sociaux pour populariser son serveur de jeu.<br /><br />
                                Une fois que votre serveur est approuvé par plusieurs personnes et après avoir eu un
                                point de vue sur l'ensemble, le lancement du serveur peut être fait si les conditions
                                de stabilité de la plateforme et du contenu suffisant sont remplies. Ainsi, la dernière
                                étape consiste à se positionner dans les classements des serveurs comme Serveur MultiGames
                                pour attirer une visibilité, se faire un nom et avoir de nouveaux joueurs.<br /><br />
                                Un conseil très important à appliquer est la structuration et le temps consacré au
                                développement de son serveur. En effet, vouloir aller trop vite dans la conception et dans
                                la mise en place et l'hébergement de son serveur privé peut être fatalement
                                contraignant par la suite puisque ce sont les fondamenteaux d'un bon serveur. Les communautés de jeu sont présentes souvent sur Steam et cela permet ainsi
                                de regrouper les différentes personnes, n'hésitez pas à installer le gestionnaire de jeux Steam.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Facilitez votre sélection de serveur</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                La liste {{ seocat($catName) }} proposée vous permet de sélectionner facilement un serveur en filtrant les listes par les catégories, aussi appelés "tags", qui sont ajoutables par les propriétaires. La navigation est plus facile, une liste de nombreux serveurs sont disponibles avec les boutons sur le dessus, vous pouvez filtrer en fonction de votre mode de jeu (si vous n'avez pas le jeu complet par exemple, pour vous connecter à ces serveurs en question). Nos classements sont visités par une bonne partie de la communauté de {{ seocat($catName) }} et nous vous encouragons à inscrire votre serveur sans plus tarder.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Communauté des jeux</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Serveur MultiGames s'engage à l'unification des communautés des serveurs privés de jeu pour
                                développer la notoriété des projets provenant des créateurs dans un but commun de développement
                                de la communauté de {{ seocat($catName) }}. Ce jeu se développe énormément par la communauté
                                qui s'y trouve et les serveurs francophones sont très intéressants à lister pour contribuer à
                                ce but final de divertissement. C'est l'engagement que nous tenons et la raison principale
                                du développement de nos multiples classements.
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Jeu Minecraft gratuit</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Beaucoup de personnes ne possèdent pas Minecraft et Serveur MultiGames est partenaire avec
                                Launcher-Minecraft.com, qui permet à de nombreux joueurs de disposer du jeu Minecraft à partir
                                d'un <a title="Launcher minecraft gratuit PC" href="https://launcher-minecraft.com/fr/telecharger">launcher minecraft</a>
                                <br />
                                En aidant la communauté à se développer et à utiliser un launcher de qualité développé
                                par des personnes reconnues dans la communauté francophone de Minecraft, c'est un avantage
                                qui permet aux joueurs ne pouvant pas se procurer le jeu de quand-même pouvoir avoir une
                                expérience de jeu sur des serveurs hors normes !
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Autres jeux</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">

                                @foreach(config('tag.cat') as $k)
                                    <a class="lis-light lis-brd-light lis-line-height-2_3 rounded d-block" title="Liste des serveurs {{ $k }}" href="/{{ encname($k) }}"><img alt="Serveur {{ $k }}" src="/img/{{ encname($k) }}.png" width="24" height="24" class="ialign" /> &nbsp;{{ ucfirst($k) }}</a>
                                @endforeach
                            </div>
                        </div>
                        <!-- skybig -->
                        <ins class="adsbygoogle"
                             style="display:inline-block;width:300px;height:1050px"
                             data-ad-client="ca-pub-4636627444279583"
                             data-ad-slot="9407076100"></ins>
                        <script>
                            (adsbygoogle = window.adsbygoogle || []).push({});
                        </script>
                    </div>
                </div>
            </div>
        </section>


        @if (encname($catName) == "clash-of-clans" OR encname($catName) == "hytale")
            <script type="application/ld+json">
            {
                "@context": "http://schema.org/",
                "@type": "AggregateRating",
                "itemReviewed": {
                "@type": "GameServer",
                "name": "Serveur {{ seocat($catName) }}@if (isset($tag)) {{ $tag }}@endif{{ $pTitle }}",
                "description": "Liste de serveur {{ seocat($catName) }}@if (isset($tag)) {{ $tag }}@endif{{ $pDesc }} gratuit. Ajoutez votre serveur ou votez pour celui-ci.",
                "playersOnline": "",
                "url": "@yield('canonical')"
                },
                "ratingValue": "5",
                "ratingCount": "8"
            }
        </script>
        @else
            <script type="application/ld+json">
            {
                "@context": "http://schema.org/",
                "@type": "AggregateRating",
                "itemReviewed": {
                "@type": "GameServer",
                "name": "Serveur {{ seocat($catName) }}@if (isset($tag)) {{ $tag }}@endif{{ $pTitle }}",
                "description": "Liste de serveur {{ seocat($catName) }}@if (isset($tag)) {{ $tag }}@endif{{ $pDesc }} gratuit. Ajoutez votre serveur ou votez pour celui-ci.",
                "playersOnline": "",
                "url": "@yield('canonical')"
                },
                "ratingValue": "{{ round($average / $averageCount, 2) }}",
                "ratingCount": "{{ max(count($data), 43) }}"
            }
        </script>
        @endif

@endsection

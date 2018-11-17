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
@php($classement = 'true')
@section('title', 'Serveur '.seocat($catName).$shownTag.$pTitle.' Francophones Gratuit - Liste Serveur MultiGames')
@section('description', 'Découvre la liste de serveur '.seocat($catName).$shownTag.$pTitle.' pour trouver celui qui te correspond le mieux et ainsi jouer à '.seocat($catName).$shownTag.' en français. Rejoins dès maintenant ton serveur de jeu français favori.')
@if (encname($catName) == "minecraft")
    @section('logometa', 'https://serveur-multigames.net/img/minecraft/minecraft.png')
@else
    @section('logometa', 'https://serveur-multigames.net/img/logo.png')
@endif

@if (isset($tag))
    @if ($current_page > 1)
        @php($next_canonical = '/tag/'.encname($tag).'/'.$current_page)
    @else
        @php($next_canonical = '/tag/'.encname($tag))
    @endif
@elseif ($current_page > 1)
    @php($next_canonical = '/page/'.$current_page)
@else
    @php($next_canonical = '')
@endif

@section('canonical', 'https://serveur-multigames.net/'.$catName.$next_canonical)

@if ($current_page > 1)
    @if (isset($tag))
        @php($prev = 'https://serveur-multigames.net/'.$catName.'/tag/'.encname($tag).'/'.($current_page - 1))
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
        @php($next = 'https://serveur-multigames.net/'.$catName.'/tag/'.encname($tag).'/'.($current_page + 1))
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
    <section class="banner-area-{{ $catName }} relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    @if (isset($tag))
                        <h1 class="text-white">Serveur {{ seocat($catName) }} {{ ucfirst($tag)}}</h1>
                        <h2 class="text-white">Liste de serveurs</h2>
                    @else
                        <h1 class="text-white">Serveur {{ seocat($catName) }}</h1>
                        <h2 class="text-white">Liste de serveurs</h2>
                    @endif
                    @if (isset($tag) or (isset($current_page) && $current_page > 1))
                        <p class="text-white link-nav"><a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a>@if (isset($tag)) <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ $catName }} {{ $tag }}" href="/{{ $catName }}/tag/{{ encname($tag) }}">{{ ucfirst($tag) }}</a>@endif @if ($current_page > 1) <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ $catName }}@if (isset($tag)) {{ $tag }}@endif page {{ $current_page }}" href="/{{ $catName }}@if (isset($tag))/tag/{{ encname($tag) }}@endif/page/{{ $current_page }}">Page {{ $current_page }}</a>@endif</p>
                    @endif
                </div>
            </div>
        </div>
    </section>
@endsection
@section('content')

    <main role="main">
        <!-- start banner Area -->

        <!-- End banner Area -->

        <!-- Start feature-cat Area -->
        <section class="post-area section-gap">
            <div class="container">
                @if (isset($votelistok) && $votelistok)
                    <a title="{{ $catName }}" href="/{{ $catName }}" class="genric-btn success" id="explicitbtn">
                        <span class="lnr lnr-checkmark-circle"></span> &nbsp;<span>Merci !</span> Votre vote a bien été pris en compte. Merci de votre soutien.<br />
                        Vous pouvez continuer de consulter le classement {{ seocat($catName) }}.
                    </a><br /><br />
                @endif
                <div class="row justify-content-center d-flex">
                    <div class="col-lg-8 post-list">
                        @if (isset($tag))
                            <blockquote class="generic-blockquote">
                                <p>
                                    Vous êtes sur la liste {{ seocat($catName) }} {{ $tag }}. La liste est composée des tags {{ $tag }}, qui sont les seuls à être présentés dans ce classement. Vous pouvez voter ou découvrir de nouvelles expériences sur le jeu. Vous souhaitez ajouter votre serveur {{ $tag }} ? <a title="Ajouter mon serveur {{ seocat($catName) }} {{ $tag }}" href="/add-server">Ajoutez-le</a> dès maintenant sur notre classement, gratuitement.
                                </p>
                            </blockquote>
                        @else
                            <blockquote class="generic-blockquote">
                                <h3>@if (isset($catName) && $catName == "minecraft")<img alt="Minecraft" src="/img/minecraft/minecraft.png" height="16" class="ialign" /> @endif Classement de serveur {{ seocat($catName) }}</h3><br />
                                Vous êtes sur la liste des serveurs. Découvrez de nouvelles expériences de jeu gratuitement à partir de ce classement. Cette liste est ordonnée par rapport aux nombre de votes. Les tags permettent aux joueurs de trouver ce qui leur correspondent le mieux.
                            </blockquote>
                        @endif
                        @foreach($data as $row)
                            <div class="single-post @if (isset($row->ad)) ad-serveur @endif">
                                @if (isset($row->ad))
                                    <div id="mise-avant"><a id="buttonmise" title="Mettez vous aussi votre serveur en avant" href="https://serveur-multigames.net/mise-en-avant">Serveur mis en avant</a>
                                    </div>
                                @endif
                                <div class="d-flex flex-row">
                                    <div class="thumb">
                                        <a title="{{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}">
                                            <img alt="{{ $row->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $row->id }}.jpg" class="rimg" height="69" width="69">
                                        </a>
                                    </div>
                                    <div class="details">
                                        <div class="title d-flex flex-row justify-content-between">
                                            <div class="titles">
                                                <a title="{{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}"><h4 class="serveur-name">{{ $row->name }}</h4></a><br />
                                                <div class="rate">
                                                    @if (strlen(trim(strtolower($row->ip))) > 0)
                                                        <div id="ip-{{ encname($row->name) }}">IP du serveur : {{ trim(strtolower($row->ip)) }}</div>
                                                    @endif
                                                    Note : @if($row->note >= 1)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note > 2 || $row->note == 2)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note > 3 || $row->note == 3)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note > 4 || $row->note == 4)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note == 5)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif
                                                </div>
                                            </div>
                                            <ul class="btns">
                                                <li id="vote"><span class="lnr lnr-heart"></span>&nbsp;<a title="Voter pour {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/vote">Voter</a> ({{ $row->votes }})</li>@if (!empty($row->website))&nbsp;<li><span class="lnr lnr-rocket"></span>&nbsp;<a title="Jouer à {{ $catName }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/go">Jouer</a></li>@endif @if (strlen(trim(strtolower($row->ip))) > 0) &nbsp;<li><span class="lnr lnr-magic-wand"></span>&nbsp;<a title="Copier l'IP de {{ $row->name }}"  onclick="copy('{{ encname($catName) }}', '{{ seocat($catName) }}', '{{ $row->name }}', '{{ encname($row->name) }}');" class="ipcopy">IP</a></li>@endif
                                                @if (!$row->verified)
                                                    <span class="verify-yes"><span class="lnr lnr-checkmark-circle" title="Serveur certifié"></span></span>
                                                @else
                                                    <span class="verify-no"><span class="lnr lnr-cross-circle" title="Serveur non certifié"></span></span>
                                                @endif
                                            </ul>
                                        </div>
                                        <p @if (isset($row->ad)) class="serveur-ad" @else class="serveur-normal" @endif><br />
                                            {{ preg_replace( "/\r|\n/", "", mb_strimwidth($row->short_desc, 0, 501, "...")) }}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        @endforeach
                        @for($i = 1;$page_number +1 > $i;$i++)
                            @if($i == $current_page)
                                <a title="{{ seocat($catName) }}@if ($i > 1) page {{ $i }}@endif" href="/{{ $catName }}@if($i > 1)/page/{{ $i }}@endif" class="genric-btn success">{{ $i }}</a>&nbsp;
                            @else
                                <a title="{{ seocat($catName) }}@if ($i > 1) page {{ $i }}@endif" href="/{{ $catName }}@if($i > 1)/page/{{ $i }}@endif" class="genric-btn primary">{{ $i }}</a>&nbsp;
                            @endif
                        @endfor
                            <br />
                            <div class="single-post d-flex flex-row" style="margin-top: 30px;">
                                <div class="details">
                                    <h3 class="text-uppercase">A propos de la liste de serveur {{ seocat($catName) }}{{ $shownTag }}</h3><br />
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="single-defination">
                                                <h4 class="mb-20"> Votes de serveur</h4>
                                                <p><br />Ce mois-ci, {{ $about->voteCount }} ont été effectués pour des serveurs {{ seocat($catName) }}. Vote pour ton serveur préféré depuis le classement des meilleurs serveurs {{ seocat($catName) }}{{$shownTag}} sans plus hésiter.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="single-defination">
                                                <h4 class="mb-20">Serveur dans le classement</h4>
                                                <p><br />Nous disposons d'un large panel de {{ $about->serveurCount }} serveurs et nous comptons encore nous étendre. Vous n'êtes pas encore dessus ? C'est le moment de s'y inscrire et d'ajouter le vôtre gratuitement.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="single-defination">
                                                <h4 class="mb-20">Avis sur {{ seocat($catName) }}</h4>
                                                <p><br />Il y a eu plus de {{ intval($about->clickCount)+intval($about->copyCount) }} personnes qui ont découvert un serveur {{ seocat($catName) }}{{$shownTag}} depuis le début du mois. Nous travaillons sur la visibilité des créateurs tous les jours et cela n'est pas prêt de s'arrêter.</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </div>
                    <div class="col-lg-4 sidebar">
                        @if (isset($catName) && $catName == "minecraft")
                            <div class="single-slidebar">
                                <h3>Informations sur le jeu Minecraft</h3>
                                <br />
                                @if (encname($catName) == "minecraft")
                                    <img alt="Serveur minecraft" src="/img/minecraft/serveur-minecraft.png" height="76" class="justify-content-between d-flex imgc" />
                                @endif
                                <div class="active-relatedjob-carusel">
                                    <div class="single-rated">
                                        <p class="inftop">
                                            Minecraft est un jeu bac à sable populaire pour son attractivité et le monde libre qu'il propose, ce qui permet à de nombreux créateurs d'imaginer de nouveaux concepts. Notre classement propose aux joueurs de découvrir un large choix d'opportunités.
                                        </p>
                                    </div>
                                </div>
                            </div>
                        @endif
                        <div class="single-slidebar">
                            <h3>Ajouter mon serveur {{ seocat($catName) }}</h3>
                            <div class="active-relatedjob-carusel">
                                <div class="single-rated">
                                    <p class="inftop">
                                        Vous souhaitez donner plus de visibilité à votre serveur {{ seocat($catName) }} sans plus tarder ? Serveur MultiGames vous propose de rejoindre plus de 100 créateurs de serveurs de jeu différents sur la plateforme pour faire découvrir votre serveur à la communauté francophone.
                                        <br /><br />
                                        Les joueurs peuvent ainsi voter pour le serveur qu'ils préfèrent afin qu'il soit plus visible dans notre classement pour attirer de nouveaux joueurs tous les jours.
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="single-slidebar">
                            <h3>Autres classements par catégorie</h3>
                            <br />
                            <ul class="cat-list">
                                @foreach($tags as $k => $v)
                                    @if ($v > 0)
                                        <li><a title="Serveur {{ seocat($catName) }} {{ $k }}" class="justify-content-between d-flex" href="/{{ $catName }}/tag/{{ encname($k) }}">Serveur {{ seocat($catName) }} {{ ucfirst($k) }}</a></li>
                                    @endif
                                @endforeach
                            </ul>
                        </div>
                        <div class="single-slidebar pubpost">
                            <div class="active-relatedjob-carusel">
                                <div class="single-rated">
                                    <ins class="adsbygoogle"
                                         id="pbblock"
                                         data-ad-client="ca-pub-1905923613312160"
                                         data-ad-slot="2719469514"></ins>
                                    <script>
                                        (adsbygoogle = window.adsbygoogle || []).push({});
                                    </script>
                                </div>
                            </div>
                        </div>
                        <div class="single-slidebar">
                            <strong>Partenaires</strong><br /><br />

                            Nos listes de serveurs restent entièrement indépendantes, mais nous sommes partenaires avec plusieurs structures communautaires de jeux.<br /><br />
                            <div id="partenaire-list">
                                <a title="Partenaire Clawnity" href="/partenaires/clawnity"><img alt="Partenaire Clawnity de Serveur MultiGames" src="/img/partenaires/minecraft-clawnity.png" /></a>
                                @if (isset($lnk) && !empty($lnk))
                                    {{ $lnk }}
                                @endif
                            </div>
                        </div>
                        <div class="single-slidebar">
                            <strong>L'importance de votre serveur {{ seocat($catName) }} sur nos classements</strong><br /><br />
                            <div class="active-relatedjob-carusel">
                                <div class="single-rated">
                                    Ajouter son serveur sur nos classements est une tâche aussi importante que de trouver le bon nom du serveur que vous souhaitez mettre en avant. Sans visibilité, il n'est pas utile de créer un serveur {{ seocat($catName) }} public, sans limite. C'est pour cela qu'un site {{ strtolower(seocat($catName)) }} est intéressant pour référencer son serveur privé pour le faire découvrir aux joueurs de la communauté.<br /><br />Serveur MultiGames offre un panel pour y ajouter son propre serveur et consulter les statistiques de votre présence dans le top {{ strtolower(seocat($catName)) }}
                                </div>
                            </div>
                        </div>
                        <div class="single-slidebar">
                            <strong>Pourquoi voulons-nous faire vivre la communauté des jeux ?</strong><br /><br />
                            <div class="active-relatedjob-carusel">
                                <div class="single-rated">
                                    Serveur MultiGames s'engage à l'unification des communautés des serveurs privés de jeu pour
                                    développer la notoriété des projets provenant des créateurs dans un but commun de développement
                                    de la communauté de {{ seocat($catName) }}. Ce jeu se développe énormément par la communauté
                                    qui s'y trouve et les serveurs francophones sont très intéressants à lister pour contribuer à
                                    ce but final de divertissement. C'est l'engagement que nous tenons et la raison principale
                                    du développement de nos multiples classements.
                                </div>
                            </div>
                        </div>
                        @if (encname($catName) == "minecraft")
                            <div class="single-slidebar">
                                <strong>Vous n'avez pas le jeu Minecraft gratuit ?</strong><br /><br />
                                <div class="active-relatedjob-carusel">
                                    <div class="single-rated">
                                        Beaucoup de personnes ne possèdent pas Minecraft et Serveur MultiGames est partenaire avec
                                        Launcher-Minecraft.com, qui permet à de nombreux joueurs de disposer du jeu
                                        <a title="Minecraft gratuit" href="/partenaires/launcher-minecraft">minecraft gratuit</a>.<br />
                                        En aidant la communauté à se développer et à utiliser un launcher de qualité développé
                                        par des personnes reconnues dans la communauté francophone de Minecraft, c'est un avantage
                                        qui permet aux joueurs ne pouvant pas se procurer le jeu de quand-même pouvoir avoir une
                                        expérience de jeu sur des serveurs hors normes !
                                    </div>
                                </div>
                            </div>
                        @endif
                        <div class="single-slidebar">
                            <strong>Conseils pour bien lancer un serveur {{ seocat($catName) }}</strong><br /><br />
                            <div class="active-relatedjob-carusel">
                                <div class="single-rated">
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
                                    contraignant par la suite puisque ce sont les fondamenteaux d'un bon serveur.
                                </div>
                            </div>
                        </div>
                        <!--
                                        <div class="single-slidebar">
                                            <h4>Publicité</h4>
                                            <div class="active-relatedjob-carusel">
                                                <div class="single-rated">

                                                </div>
                                            </div>
                                        </div>
                        !-->
                    </div>
                </div>
            </div>
        </section>

    </main>
    <!-- End calto-action Area -->

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
            "ratingCount": "{{ count($data) }}"
        }
    </script>

@endsection
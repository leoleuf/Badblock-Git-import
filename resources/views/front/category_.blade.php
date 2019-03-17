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

@if (encname($catName) == "minecraft")
    @section('title', 'Serveur '.seocat($catName).$shownTag.$pTitle.' : Liste Complète Française De '.$about->serveurCount.' Serveurs Gratuit')
    @section('description', 'Découvre la liste de serveur Minecraft'.$shownTag.' gratuit complète. Joue à '.$about->serveurCount.' serveurs'.$shownTag.' français, du serveur cracké, Survie, PVP Faction, Moddé, Premium et plus.')
@elseif (encname($catName) == "clash-of-clans")
    @section('title', 'Liste de Serveur privé Clash Of Clans : COC sur Android PC iOS')
    @section('description', 'Découvre ton serveur privé Clash Of Clan 2019 gratuit préféré. Depuis un fichier APK Android ou des serveurs Clash Royale hdv 12 iOS possibles à télécharger avec code, découvre sur PC le jeu COC dès maintenant.')
@elseif (encname($catName) == "hytale")
    @section('title', 'Liste de Serveur privé Hytale : Tout à savoir sur Hytale')
    @section('description', 'Découvre ton serveur privé Hytale gratuit préféré. La date de sortie, le trailer... Tout à savoir sur le jeu Hytale développé par Hypixel Studio, prochain phénomène qui va attirer de nombreux serveurs originaux.')
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

    <main role="main">
        <!-- start banner Area -->
        <section class="banner-area-{{ $catName }} relative" id="home">
            <div class="overlay overlay-bg"></div>
            <div class="container">
                <div class="row d-flex align-items-center justify-content-center">
                    <div class="about-content col-lg-12">
                        @if (isset($catName) && $catName != "clash-of-clans")
                            @if (isset($tag))
                                <h1 class="text-white">Serveur {{ seocat($catName) }} {{ ucfirst($tag)}}</h1>
                                <h2 class="text-white">Liste de serveurs</h2>
                            @else
                                <h1 class="text-white">Serveur {{ seocat($catName) }}</h1>
                                <h2 class="text-white">Liste de serveurs</h2>
                            @endif
                        @else
                            <h1 class="text-white">Télécharger Clash Of Clans iOs Android</h1>
                            <h2 class="text-white">Liste de serveur privé</h2>
                        @endif
                        @if (isset($tag) or (isset($current_page) && $current_page > 1))
                            <p class="text-white link-nav"><a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a>@if (isset($tag)) <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ $catName }} {{ $tag }}" href="/{{ $catName }}/tag/{{ enctag($tag) }}">{{ ucfirst($tag) }}</a>@endif @if ($current_page > 1) <span class="lnr lnr-arrow-right"></span>  <a title="Serveur {{ $catName }}@if (isset($tag)) {{ $tag }}@endif page {{ $current_page }}" href="/{{ $catName }}@if (isset($tag))/tag/{{ enctag($tag) }}@endif/page/{{ $current_page }}">Page {{ $current_page }}</a>@endif</p>
                        @endif
                    </div>
                </div>

            </div>
        </section>
        <!-- End banner Area -->

        <!-- Start feature-cat Area -->
        <section class="post-area">
            @if (isset($votelistok) && $votelistok)
                <a title="{{ $catName }}" href="/{{ $catName }}" class="genric-btn success" id="explicitbtn">
                    <span class="lnr lnr-checkmark-circle"></span> &nbsp;<span>Merci !</span> Votre vote a bien été pris en compte. Merci de votre soutien.<br />
                    Vous pouvez continuer de consulter le classement {{ seocat($catName) }}.
                </a><br />
            @endif
            <div class="container justify-content-center d-flex section-gap">
                <div class="col-lg-4 sidebar">
                    @if (isset($catName) && $catName == "minecraft")

                        @if (!_bot_detected())
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
                        @endif
                        <div class="single-slidebar">
                            <h3>Informations sur le jeu Minecraft à télécharger</h3>
                            <br />
                            @if (encname($catName) == "minecraft")
                                <img alt="Serveur minecraft" src="/img/minecraft/serveur-minecraft.png" height="76" class="justify-content-between d-flex imgc" />
                            @endif
                            <div class="active-relatedjob-carusel single-rated">
                                <p class="inftop">
                                    Minecraft est un jeu bac à sable populaire facile à télécharger et à installer sur PC, connu notamment pour son attractivité et le monde libre qu'il propose, notamment avec le mode multijoueur et mini-jeux, ce qui permet à de nombreux créateurs d'imaginer de nouveaux concepts. Notre liste propose ainsi aux joueurs de découvrir un large choix d'opportunités et de créations.
                                    <br />
                                    Il est d'ailleurs possible d'intégrer Minecraft à Steam
                                </p>
                                <p>
                                    @if(Auth::user())
                                        <a class="ticker-btn d-flex" id="ajout-serveur" title="Ajouter son serveur Minecraft" href="/dashboard/add-server"><span class="fa fa-plus-circle" id="serveur-plus"></span> Ajouter mon serveur Minecraft</a>
                                        @if (!isset($ad))
                                            <br /><a class="ticker-btn d-flex" id="ajout-serveur" title="Mettre en avant mon serveur" href="/dashboard/mise-en-avant"><span class="fa fa-star" id="serveur-plus"></span> Mettre en avant mon serveur</a>
                                        @endif
                                    @else
                                        <a class="ticker-btn d-flex" id="ajout-serveur" title="Ajouter son serveur Minecraft" href="/login"><span class="fa fa-plus-circle" id="serveur-plus"></span> Ajouter mon serveur Minecraft</a>
                                        @if (!isset($ad))
                                            <br /><a class="ticker-btn d-flex" id="ajout-serveur" title="Mettre en avant mon serveur" href="/mise-en-avant"><span class="fa fa-star" id="serveur-plus"></span> Mettre en avant mon serveur</a>
                                        @endif
                                        <br />
                                        <a class="ticker-btn d-flex" id="telecharger-minecraft" title="Installer Minecraft PC" href="https://launcher-minecraft.com/fr/telecharger"><span class="fa fa-play-circle" id="installer-minecraft"></span> Télécharger Minecraft sur PC</a>
                                    @endif
                                </p>
                            </div>
                        </div>
                    @endif

                    @if (isset($catName) && $catName != "clash-of-clans")
                        @if (isset($catName) && $catName != "minecraft")
                            @if (!_bot_detected())
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
                            @endif
                        @endif
                        <div class="single-slidebar">
                            <h3>Ajouter mon serveur {{ seocat($catName) }}</h3>
                            <div class="active-relatedjob-carusel single-rated">
                                <p class="inftop">
                                    Vous souhaitez donner plus de visibilité à votre serveur {{ seocat($catName) }} sans plus tarder ? Serveur MultiGames vous propose de rejoindre plus de 100 créateurs de serveurs de jeu différents sur la plateforme pour faire découvrir votre serveur à la communauté francophone.
                                    <br /><br />
                                    Les joueurs peuvent ainsi voter pour le serveur qu'ils préfèrent afin qu'il soit plus visible dans notre classement pour attirer de nouveaux joueurs tous les jours.
                                </p>
                            </div>
                        </div>
                    @else
                        @if (!_bot_detected())
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
                        @endif
                        <div class="single-slidebar">
                            <h3>Télécharger {{ seocat($catName) }}</h3>
                            <div class="active-relatedjob-carusel single-rated">
                                <p class="inftop">
                                    Vous souhaitez télécharger ou installer Clash Of Clans directement sur votre smartphone iOS iPhone Apple ou Android pour jouer à votre serveur privé Clash Of Clans préféré ? C'est désormais possible directement sur notre site, en combinaison de nouveaux avantages qui permettent au joueur de passer HDV 12.
                                </p>
                            </div>
                        </div>
                    @endif
                    <div class="single-slidebar">
                        <strong>L'importance de votre serveur {{ seocat($catName) }} sur notre liste</strong><br /><br />
                        <div class="active-relatedjob-carusel single-rated">
                            Ajouter son serveur sur nos classements est une tâche aussi importante que de trouver le bon nom du serveur que vous souhaitez mettre en avant. Sans visibilité, il n'est pas utile de créer un serveur {{ seocat($catName) }} public, sans limite. C'est pour cela qu'un site {{ strtolower(seocat($catName)) }} est intéressant pour référencer son serveur privé pour le faire découvrir aux joueurs de la communauté.<br /><br />Serveur MultiGames offre un panel pour y ajouter son propre serveur et consulter les statistiques de votre présence dans le top {{ strtolower(seocat($catName)) }}.<br />
                            Une trentaine de serveurs de ce type constatent déjà une amélioration de leur visibilité ainsi que de leur rendements. C'est tout l'objectif de Serveur MultiGames, qui est de proposer gratuitement cette plateforme de découverte communautaire gratuitement, en échange d'un soutien envers le travail effectué comme en parler autour de vous, pour développer encore plus les communautés et rendre la meilleure expérience de jeu possible pour l'utilisateur final.<br /><br />L'ajout d'un serveur sur nos plateformes est un processus très simple. Il y est possible de faire certifier son serveur en validant sa propriété facilement à l'aide d'un lien
                            pour respecter au maximum les règles de notre classement, notamment le fait de n'ajouter que son serveur de jeu et éviter d'ajouter ceux qui n'appartiennent pas aux personnes qui ajoutent le serveur en question, pour des questions de sécurité, de droits sur le contenu textuel, le logo et la bannière. En quelques clics, l'ajout peut se faire et votre visibilité peut se multiplier par dix en seulement quelques minutes !<br /><br />La présence de Serveur MultiGames sur les sites et les moteurs permet ainsi de donner de la visibilité à tous ces créateurs qui le méritent par le biais de vote. Notre classement se démarque par le bouton "Jouer" qui permet aux joueurs de découvrir un serveur en seulement quelques clics, sans avoir à payer quoi que ce soit.
                        </div>
                    </div>

                    <div class="single-slidebar">
                        <h3>Conseils pour bien lancer un serveur {{ seocat($catName) }}</h3><br /><br />
                        <div class="active-relatedjob-carusel single-rated">
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
                    <div class="single-slidebar">
                        <h3>Facilitez votre sélection de serveur</h3><br /><br />
                        <div class="active-relatedjob-carusel single-rated">
                            La liste {{ seocat($catName) }} proposée vous permet de sélectionner facilement un serveur en filtrant les listes par les catégories, aussi appelés "tags", qui sont ajoutables par les propriétaires. La navigation est plus facile, une liste de nombreux serveurs sont disponibles avec les boutons sur le dessus, vous pouvez filtrer en fonction de votre mode de jeu (si vous n'avez pas le jeu complet par exemple, pour vous connecter à ces serveurs en question). Nos classements sont visités par une bonne partie de la communauté de {{ seocat($catName) }} et nous vous encouragons à inscrire votre serveur sans plus tarder.
                        </div>
                    </div>
                    <div class="single-slidebar">
                        <strong>Pourquoi voulons-nous faire vivre la communauté des jeux ?</strong><br /><br />
                        <div class="active-relatedjob-carusel single-rated">
                            Serveur MultiGames s'engage à l'unification des communautés des serveurs privés de jeu pour
                            développer la notoriété des projets provenant des créateurs dans un but commun de développement
                            de la communauté de {{ seocat($catName) }}. Ce jeu se développe énormément par la communauté
                            qui s'y trouve et les serveurs francophones sont très intéressants à lister pour contribuer à
                            ce but final de divertissement. C'est l'engagement que nous tenons et la raison principale
                            du développement de nos multiples classements.
                        </div>
                    </div>
                    @if (encname($catName) == "minecraft")
                        <div class="single-slidebar">
                            <h3>Vous n'avez pas le jeu Minecraft gratuit ?</h3><br /><br />
                            <div class="active-relatedjob-carusel single-rated">
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
                    @endif
                    <div class="single-slidebar">
                        <strong>Partenaires {{ seocat($catName) }}</strong><br /><br />
                        Nos listes de serveurs restent entièrement indépendantes, mais nous sommes partenaires avec plusieurs structures communautaires de jeux.<br /><br />
                        <div id="partenaire-list">
                            Envie de devenir Partenaire ? Que vous soyez un site de communauté de jeux-vidéos ou dans des catégories plus larges qui pourraient intéresser nos utilisateurs, contactez-nous directement.
                            @if (isset($lnk) && !empty($lnk))
                                {{ $lnk }}
                            @endif
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
                    <div class="col-lg-8 post-list">
                        @if (isset($tag))
                            <div class="serveur-infos">
                                <h3>@if (isset($catName) && $catName == "minecraft")<img alt="Serveur Minecraft {{ ucfirst($tag) }}" src="/img/minecraft/minecraft.png" height="16" class="ialign" />
                                    @elseif (isset($catName) && $catName == "clash-of-clans")<img alt="Serveur Clash Of clans" src="/img/clash-of-clans.png" height="16" class="ialign" />
                                    @endif Liste serveur {{ seocat($catName) }} {{ ucfirst($tag) }}</h3><div class="serveur-sep"></div>
                                <p>
                                    Vous êtes sur la liste {{ seocat($catName) }} {{ $tag }}. La liste est composée des tags {{ $tag }}, qui sont les seuls à être présentés dans ce classement. Vous pouvez voter ou découvrir de nouvelles expériences sur le jeu. Vous souhaitez ajouter votre serveur {{ $tag }} ? <a title="Ajouter mon serveur {{ seocat($catName) }} {{ $tag }}" href="/add-server">Ajoutez-le</a> dès maintenant sur notre classement, gratuitement.
                                </p>
                            </div>
                        @else
                            @if (isset($catName) && $catName == "clash-of-clans")
                                <div class="single-post details" id="liste-information">
                                    <h3 class="text-uppercase"><img alt="Télécharger le Serveur privé Clash Of Clans" src="/img/clash-of-clans.png" height="16" class="ialign" /> Télécharger {{ seocat($catName) }} sans plus attendre</h3><br />
                                    <div class="row">
                                        <div class="col-md-12 single-defination">
                                            Vous pouvez télécharger votre serveur privé Clash Of Clans favori par le Play Store, Apple Store ou toutes les plateformes légales et légitimes sans plus hésiter. Il est possible de promouvoir votre serveur Clash Of Clans directement sur cette page. A partir des mises à jour de Clash Of Clans, vous pouvez vous tenir au courant des toutes dernières nouveautés par l'éditeur Supercell. Les images Clash Of Clans utilisés sur cette page sont la propriétés de Supercell et sont utilisés dans un cadre complètement informatif. Forum Clash Of Clans et Wiki disponibles également sur plusieurs sites relatifs au jeu PC Mobile.
                                        </div>
                                    </div>
                                </div>
                            @elseif (isset($catName) && $catName == "hytale")
                                <div class="single-post details" id="liste-information">
                                    <h3 class="text-uppercase"><img alt="Télécharger le Serveur privé Hytale" src="/img/hytale.jpg" height="16" class="ialign" /> Hytale - Liste de serveurs</h3><br />
                                    <div class="row">
                                        <div class="col-md-12 single-defination">
                                            Sur cette page, vous pourrez promouvoir votre serveur privé Hytale quand le jeu sera sorti. Les dernières mises à jour du jeu ainsi que d'autres informations concernant le trailer, le modding ou le système de serveur du jeu seront publiés directement sur cette page.
                                        </div>
                                    </div>
                                </div>
                            @else
                                <div class="serveur-infos">
                                    <h3>@if (isset($catName) && $catName == "minecraft")<img alt="Serveur Minecraft" src="/img/minecraft/minecraft.png" height="16" class="ialign" />
                                        @endif Liste serveur {{ seocat($catName) }}</h3><div class="serveur-sep"></div>
                                    <p>La liste de serveur {{ seocat($catName) }} est composée de <strong>nombreux serveurs</strong> à découvrir. Vous êtes un joueur de Minecraft ? Un serveur Cracké gratuit, SkyBlock, Faction, PvP Moddé, Survie, PE ou RP et bien plus encore sont à découvrir, avec un <b>large choix de {{ $about->serveurCount }} serveurs {{ seocat($catName) }}</b> gratuits, où vous pouvez vous y connecter à partir de cette liste, pour jouer à Minecraft, qui est classée par rapport au nombre de votes effectués par les joueurs.</p>
                                    @if (isset($addon))
                                        {!! $addon !!}
                                    @endif
                                </div>
                            @endif
                        @endif
                        @if (isset($catName) && $catName == "clash-of-clans")
                            <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                            <!-- telecharger-minecraft-serveur-multigames -->
                            <ins class="adsbygoogle"
                                 style="display:block"
                                 data-ad-client="ca-pub-1905923613312160"
                                 data-ad-slot="4846476694"
                                 data-ad-format="auto"
                                 data-full-width-responsive="true"></ins>
                            <script>
                                (adsbygoogle = window.adsbygoogle || []).push({});
                            </script><br />
                            <div class="single-post details" id="liste-information">
                                <h3 class="text-uppercase">Télécharger {{ seocat($catName) }} sans plus attendre</h3><br />
                                <div class="row">
                                    <div class="col-md-12 single-defination">
                                        Vous pouvez télécharger votre serveur privé Clash Of Clans favori par le Play Store, Apple Store ou toutes les plateformes légales et légitimes sans plus hésiter. Il est possible de promouvoir votre serveur Clash Of Clans directement sur cette page. A partir des mises à jour de Clash Of Clans, vous pouvez vous tenir au courant des toutes dernières nouveautés par l'éditeur Supercell. Les images Clash Of Clans utilisés sur cette page sont la propriétés de Supercell et sont utilisés dans un cadre complètement informatif. Forum Clash Of Clans et Wiki disponibles également sur plusieurs sites relatifs au jeu PC Mobile.
                                    </div>
                                </div>
                            </div>
                            <div class="serveur-infos">
                                <h3>Liste serveur {{ seocat($catName) }} privé</h3><div class="serveur-sep"></div>
                                <p>
                                    Viens jouer avec tes amis sur ton serveur Clash Of Clans préféré directement en téléchargeant le jeu depuis le site Internet de Serveur MultiGames, avec de nombreux avantages à découvrir, disponible directement sur Android, sur iOs avec la possibilité de passer HD v12. Ce jeu est disponible en version 2018 et 2019, vous pouvez donc jouer gratuitement à ce jeu sans plus attendre.<br /><br />
                                    Plusieurs serveurs Clash Of Clans privés existent mais ne sont pas forcément autorisés par les créateurs du jeu. En revanche, nous vous conseillons de jouer à ce serveur Clash Of Clans dès maintenant sans plus hésiter. Ce jeu vidéo sur mobile est sorti il y a déjà longtemps. La stratégie en temps réel est un mode de jeu qui est très apprécié par la communauté, ce qui invite aussi les joueurs de Clash Of Clans à développer leurs propres serveurs disponibles sur plusieurs plateformes (iOS et Android). Jouez à ce serveur Clash Of Clans dès maintenant et découvrez-le sans plus hésiter, un jeu Pegi 7 et tout public qui n'attend plus que vous avec un large choix de serveurs différents. Pourquoi patienter pour aller jouer sur votre serveur Clash Of Clans privé disponible depuis notre liste ? Il est possible de télécharger le jeu maintenant sur mobile ou sur une plateforme correspondante. Le téléchargement est rapide.
                                    8 avis ont été postés pour les fiches de serveurs Clash of Clans avec une moyenne de 5. <b>Téléchargez le launcher Clash Of Clans ci-dessous sans plus attendre</b>
                                </p>
                                @if (isset($addon))
                                    {!! $addon !!}
                                @endif
                            </div>
                        @elseif (isset($catName) && $catName == "hytale")
                                @if (!_bot_detected())
                                    <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
                                    <!-- telecharger-minecraft-serveur-multigames -->
                                    <ins class="adsbygoogle"
                                         style="display:block"
                                         data-ad-client="ca-pub-1905923613312160"
                                         data-ad-slot="4846476694"
                                         data-ad-format="auto"
                                         data-full-width-responsive="true"></ins>
                                    <script>
                                        (adsbygoogle = window.adsbygoogle || []).push({});
                                    </script><br />
                                @endif
                                    <div class="single-post details" id="liste-information">
                                        <h3 class="text-uppercase">Télécharger {{ seocat($catName) }} sans plus attendre ?</h3><br />
                                        <div class="row">
                                            <div class="col-md-12 single-defination">
                                                Le jeu Hytale, développé par Hypixel Studio, est un jeu qui devrait sortir très prochainement. Vous pourrez ainsi télécharger Hytale sans attendre trop longtemps puisqu'un trailer est déjà sorti, et beaucoup de rumeurs circulent à propos des nouveautés, de la release date (la date de sortie). Certains joueurs s'emparent déjà de leur hype pour chercher le jeu sur PS4, alors que pour le moment il est annoncé être disponible principalement sur les PC, Windows Mac et Linux, les trois principaux noyaux/systèmes d'exploitation les plus utilisés. Rien ne dit qu'une version PS4 ne sortira pas, mais pour le moment il est prévu de pouvoir le découvrir très prochainement.<br /><br />
                                                Avec trois modes, dont un survie, un multi et même un mode pour les mini-jeux, le joueur ne devrait pas avoir à s'ennuyer sur le jeu Hytale. Une intégration est prévue pour pouvoir directement disposer de la liste des serveurs directement sur le multi d'Hytale et Serveur MultiGames veut vous proposer une liste française des serveurs Hytale qui seront prochainement disponibles.<br /><br />
                                                N'hésitez donc pas à vous jeter sur cette occasion pour découvrir le trailer et pourquoi pas, devenir un serveur Hytale français réputé quand le jeu sera sorti. Le modding sera incorporé directement dans le serveur de jeu, ce qui permettra donc d'éviter les cheats (aussi appelés clients de triche) sur Hytale.
                                            </div>
                                        </div>
                                    </div>
                                    <div class="serveur-infos">
                                        <h3>Liste serveur {{ seocat($catName) }} privé</h3><div class="serveur-sep"></div>
                                        <p>
                                            N'hésite pas à jouer sur ton futur serveur Hytale préféré en revenant plusieurs fois à la pêche aux informations puisque nous proposerons des modifications de la page dès que nous aurons plus d'informations sur la sortie du jeu, sur le fonctionnement des serveurs et sur les possibilités que promettent le jeu Hytale 2019. Ce jeu sera peut-être porté sur mobile et sur les autres plateformes s'il atteint le même succès que Minecraft.
                                        </p>
                                        @if (isset($addon))
                                            {!! $addon !!}
                                        @endif
                                    </div>
                                @else
                        @php($totalReviews = 0)
                        @php($average = 0)
                        @php($averageCount = 0)
                                <div class="button-group-area">
                                    @php($bro = 0)
                                    @foreach($tags as $k => $v)
                                        @if ($v > 0)
                                            @php($bro = $bro + 1)
                                            <a title="Serveur {{ seocat($catName) }} {{ $k }}" class="@if($bro > 4) serveurs2 @endif genric-btn info circle" href="/{{ $catName }}/tag/{{ enctag($k) }}">{{ ucfirst($k) }} ({{ $v }})</a></li>
                                        @endif
                                    @endforeach
                                    <a id="show-more" class="genric-btn info circle" onclick="showmore();">Afficher plus</a></li>
                                </div>
                            <script>
                                function showmore() {
                                    var cusid_ele = document.getElementsByClassName('serveurs2');
                                    for (var i = 0; i < cusid_ele.length; ++i) {
                                        var item = cusid_ele[i];
                                        item.classList.remove("serveurs2");
                                    }
                                    document.getElementById('show-more').remove();
                                }
                            </script>
                                        @if (!_bot_detected())
                                            <ins class="adsbygoogle"
                                                 style="display:block"
                                                 data-ad-client="ca-pub-1905923613312160"
                                                 data-ad-slot="1434308007"
                                                 data-ad-format="auto"
                                                 data-full-width-responsive="true"></ins>
                                            <script>
                                                (adsbygoogle = window.adsbygoogle || []).push({});
                                            </script><br />
                                        @endif
                            @php($currentAds = array())
                        @foreach($data as $row)
                                    @if (isset($row->ad))
                                        @php($currentAds[$row->id] = 1)
                                    @elseif (isset($currentAds[$row->id]))
                                        @continue
                                    @endif
                                    <div class="single-post @if (isset($row->ad)) ad-serveur @endif d-flex">
                                    <a title="Informations du serveur Minecraft {{ $row->name }}" href="/{{ $catName }}/{{ enctag($row->name) }}" rel="noopener"><img alt="{{ $row->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $row->id }}.jpg" class="rimg" height="69" width="69"></a>
                                    <div class="details">
                                        <div class="d-flex justify-content-between">
                                            <div class="titles">
                                                <a title="{{ $row->name }}" href="/{{ $catName }}/{{ enctag($row->name) }}" rel="noopener"><h4 class="serveur-name">{{ $row->name }}</h4></a>
                                                <br />Note : @if($row->note >= 1)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note > 2 || $row->note == 2)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note > 3 || $row->note == 3)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note > 4 || $row->note == 4)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif @if($row->note == 5)<span class="fa fa-star fex"></span>@else<span class="fa fa-star-o fexo"></span>@endif
                                                @if (strlen(trim(strtolower($row->ip))) > 0)
                                                    <div id="ip-{{ encname($row->name) }}" class="hidden-ip">{{ trim(strtolower($row->ip)) }}</div>
                                                @endif
                                            </div>
                                            <ul class="btns">
                                                <li id="vote"><span class="lnr lnr-heart"></span>&nbsp;<a title="Voter pour {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/vote">Voter</a> ({{ $row->votes }})</li>@if (!empty($row->website))&nbsp;<li><span class="lnr lnr-rocket"></span>&nbsp;<a title="Jouer à {{ $catName }} {{ $row->name }}" href="/{{ $catName }}/{{ encname($row->name) }}/go" rel="noopener" target="_blank">Jouer</a></li><!--@endif @if (strlen(trim(strtolower($row->ip))) > 0)&nbsp;<li><span class="lnr lnr-magic-wand"></span>&nbsp;<a title="Copier l'IP de {{ $row->name }}"  onclick="copy('{{ encname($catName) }}', '{{ seocat($catName) }}', '{{ $row->name }}', '{{ encname($row->name) }}');" class="ipcopy">IP</a></li>@endif!-->
                                                @if ($row->verified)
                                                    <span class="verify-yes lnr lnr-checkmark-circle" title="Serveur certifié"></span>
                                                @else
                                                    <span class="verify-no lnr lnr-cross-circle" title="Serveur non certifié"></span>
                                                @endif &nbsp;&nbsp;&nbsp;
                                            </ul>
                                        </div>
                                        @php($totalReviews += $row->reviews * 7)
                                        @php($average += ($row->note == 0) ? 5 : $row->note)
                                        @php($averageCount++)
                                        <p class="serveur-normal"><br />
                                            @php($dozpa = substr_count($row->short_desc, "\r") + substr_count($row->short_desc, "\n"))
                                            @if (intval($dozpa) > 4)
                                                {{ preg_replace( "/\r|\n/", "", mb_strimwidth($row->short_desc, 0, 501, "...")) }}
                                            @else
                                                {!! nl2br(htmlentities(htmlspecialchars(mb_strimwidth($row->short_desc, 0, 501, "...")))) !!}
                                            @endif
                                        </p>
                                        @if (isset($row->ad))
                                            @php($ad = 'ok')
                                            <div class="serveur-avant"><a title="Mettre en avant mon serveur" href="/mise-en-avant"><span class="fa fa-star"></span> Envie de passer premier ?</a></div>
                                        @endif
                                    </div>
                            </div>
                        @endforeach
                                @for($i = 1;$page_number +1 > $i;$i++)
                                    @if($i == $current_page)
                                        <a title="{{ seocat($catName) }}@if ($i > 1) page {{ $i }}@endif" href="/{{ $catName }}@if($i > 1)/page/{{ $i }}@endif" class="genric-btn success">{{ $i }}</a>&nbsp;
                                    @else
                                        <a title="{{ seocat($catName) }}@if ($i > 1) page {{ $i }}@endif" href="/{{ $catName }}@if($i > 1)/page/{{ $i }}@endif" class="genric-btn info">{{ $i }}</a>&nbsp;
                                    @endif
                                @endfor
                            @endif
                            <div class="serveur-sep"></div>
                            <div class="single-post details" id="liste-information">
                                <h3 class="text-uppercase">A propos de la liste de serveur {{ seocat($catName) }}{{ $shownTag }}</h3><br />
                                <div class="row">
                                    <div class="col-md-4 single-defination">
                                        <h4 class="mb-20"> Votes de serveur</h4>
                                        <p><br />Ce mois-ci, {{ $about->voteCount }} votes ont été effectués pour des serveurs {{ seocat($catName) }}. Vote pour ton serveur préféré depuis le classement des meilleurs serveurs {{ seocat($catName) }}{{$shownTag}} sans plus hésiter.</p>
                                    </div>
                                    <div class="col-md-4 single-defination">
                                        <h4 class="mb-20">Serveur dans le classement</h4>
                                        <p><br />Nous disposons d'une large liste complète de {{ $about->serveurCount }} serveurs et nous comptons encore nous étendre. Vous n'êtes pas encore dessus ? C'est le moment de s'y inscrire et d'ajouter le vôtre gratuitement.</p>
                                    </div>
                                    <div class="col-md-4 single-defination">
                                        <h4 class="mb-20">Avis sur {{ seocat($catName) }}</h4>
                                        <p><br />Il y a eu plus de {{ intval($about->clickCount)+intval($about->copyCount) }} personnes qui ont découvert un serveur {{ seocat($catName) }}{{$shownTag}} depuis le début du mois. Nous travaillons sur la visibilité des créateurs tous les jours et cela n'est pas prêt de s'arrêter.</p>
                                    </div>
                                </div>
                            </div>
                            <div class="single-post details">
                                <h3 class="text-uppercase">Serveur gratuit sur notre classement {{ seocat($catName) }}</h3><br />
                                L'objectif du classement proposé ici est de lister le maximum de serveurs gratuits pour que les joueurs n'aient pas besoin de payer pour venir sur la majorité des serveurs, afin d'avoir une expérience fluide et sans compromis. En cas de problème ou de suggestion, il est possible de passer par le formulaire de contact du site pour contacter l'équipe du site en quelques clics seulement.
                            </div>
                    </div>
            </div>
        </section>

    </main>
    <!-- End calto-action Area -->

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

@section('title', 'Recherche de Serveur '.htmlentities(htmlspecialchars($searchTerm)).' - Liste et Classement de Serveurs MultiGames')
@section('robots', 'noindex, nofollow')
@section('description', 'Recherchez les meilleurs Serveurs avec pour recherche '.htmlentities(htmlspecialchars($searchTerm)).' afin de trouver le meilleur Serveur qui vous correspond et jouer avec tes amis Gratuitement. La Liste est composée des derniers Serveurs les plus populaires et les mieux notés.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/recherche/'.$internalTerm)
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')

    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        {{ $searchTerm }}
                    </h1>
                    <h2 class="text-white">
                        Recherche de Serveur MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a> <span class="lnr lnr-arrow-right"></span> Rechercher un serveur <span class="lnr lnr-arrow-right"></span> <a title="Recherche Serveur MultiGames {{ $searchTerm }}" href="/recherche/{{ $searchTerm }}">{{ $searchTerm }}</a></p>
                </div>
            </div>
        </div>
    </section>
    <!-- End banner Area -->

    <!-- Start feature-cat Area -->
    <section class="post-area section-gap">
    <div class="container">
        <div class="row justify-content-center d-flex">
            <div class="col-lg-8 post-list">
                @foreach($data as $row)
                <div class="single-post d-flex flex-row">
                    <div class="thumb">
                        <img alt="Serveur {{ seocat($row->cat) }} {{ $row->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $row->id }}.jpg" style="border-radius: 10px;" height="69" width="69">
                    </div>
                    <div class="details">
                        <div class="title d-flex flex-row justify-content-between">
                            <div class="titles">
                                <a title="Serveur {{ seocat($row->cat) }} {{ $row->name }}" href="/{{ encname($row->cat) }}/{{ encname($row->name) }}"><h4>{{ $row->name }}</h4></a>
                                <div class="rate">
                                    @if($row->note >= 1)
                                        <span class="fa fa-star" style="color: #ffa205;"></span>
                                    @else
                                        <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                    @endif
                                    @if($row->note > 2 || $row->note == 2)
                                        <span class="fa fa-star" style="color: #ffa205;"></span>
                                    @else
                                        <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                    @endif
                                    @if($row->note > 3 || $row->note == 3)
                                        <span class="fa fa-star" style="color: #ffa205;"></span>
                                    @else
                                        <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                    @endif
                                    @if($row->note > 4 || $row->note == 4)
                                        <span class="fa fa-star" style="color: #ffa205;"></span>
                                    @else
                                        <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                    @endif
                                    @if($row->note == 5)
                                        <span class="fa fa-star" style="color: #ffa205;"></span>
                                    @else
                                        <span style="color: #7e7e7e" class="fa fa-star-o"></span>
                                    @endif
                                </div>
                            </div>
                            <ul class="btns">
                                <li id="vote"><a title="Voter pour le serveur {{ $row->name }}" href="/{{ encname($row->cat) }}/{{ encname($row->name) }}/vote">{{ $row->votes }} <span class="lnr lnr-heart"></span></a></li>@if (!empty($row->website))&nbsp;<li><span class="lnr lnr-rocket"></span> &nbsp;<a title="Jouer au serveur {{ seocat($row->cat) }} {{ $row->name }}" href="/{{ encname($row->cat) }}/{{ encname($row->name) }}/go" target="_blank">JOUER</a></li>@endif
                            </ul>
                        </div>
                        <p style="margin-left: 10px; padding: 3px;">
                        {{ preg_replace( "/\r|\n/", "", mb_strimwidth($row->short_desc, 0, 301, "...")) }}<br /><br />
                        <ul class="tags">
                            <span class="lnr lnr lnr-tag"></span> &nbsp;
                            @foreach(json_decode($row->tag) as $key)<a id="tag-{{ $key }}" title="Serveur {{ seocat($row->cat) }} {{ $key }}" href="/{{ encname($row->cat)  }}/tag/{{ enctag($key) }}">{{ ucfirst($key) }}</a>&nbsp;@endforeach
                        </ul>
                        @if (strlen(trim(strtolower($row->ip))) > 0)<div class="hidden-ip" id="ip-{{ encname($row->name) }}">{{ trim(strtolower($row->ip)) }}</div>@endif
                        </p>
                    </div>
                </div>
                @endforeach
                @if (count($data) == 0)
                    <blockquote class="generic-blockquote">
                        Aucun résultat de recherche pour le mot clé '{{ $searchTerm }}'. Veuillez réessayer en utilisant un mot clé plus large.
                    </blockquote>
                @endif
            </div>
            <div class="col-lg-4 sidebar">
                <div class="single-slidebar">
                    <div class="active-relatedjob-carusel">
                        <div class="single-rated">
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
                <div class="single-slidebar">
                    <h4>Liste des autres serveurs gratuits</h4>
                    <ul class="cat-list">
                        @foreach(config('tag.cat') as $k)
                            <li><a title="Liste des serveurs {{ $k }}" class="justify-content-between d-flex" href="/{{ encname($k) }}">{{ ucfirst($k) }} <img alt="Serveur {{ $k }}" src="/img/{{ encname($k) }}.png" width="24" height="24" style="vertical-align: middle;" /> </a></li>
                        @endforeach
                    </ul>
                </div>
                <div class="single-slidebar">
                    <div class="active-relatedjob-carusel">
                        <div class="single-rated">
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
    <!-- End calto-action Area -->



    <script type="application/ld+json">
        {
            "@context": "http://schema.org/",
            "@type": "AggregateRating",
            "itemReviewed": {
            "@type": "GameServer",
            "name": "Serveur Multigames",
            "description": "Découvrez dès maintenant notre site de classement des serveurs multigames. Trouvez votre serveur très facilement et votez pour eux.",
            "playersOnline": "",
            "url": "https://serveur-multigames.net/"
            },
            "ratingValue": "5",
            "ratingCount": "{{ count($data) }}"
        }
    </script>
    <script src="/js/vendor/jquery-2.2.4.min.js"></script>

    <script async defer>
        function copy(cat, displ, serv)
        {
            var copyText = document.getElementById("ip-" + serv);
            var textArea = document.createElement("textarea");
            textArea.value = copyText.textContent;
            document.body.appendChild(textArea);
            textArea.select();
            document.execCommand("copy");
            textArea.remove();
            var request = $.ajax({
                url: "/" + cat + "/" + serv + "/ip",
                method: "GET",
                dataType: "html",
                success: function (data) {
                }
            });
            alert('Vous avez copié l\'adresse IP du Serveur ' + displ + ".");
        }
    </script>
@endsection
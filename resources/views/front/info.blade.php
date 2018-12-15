@section('title', $data->name.' | Serveur '.seocat($catName).' A Découvrir - Serveur MultiGames')
@section('description', 'Découvrez le serveur '.seocat($catName).' '.$data->name.' gratuit français. Déjà plus de '.$data->votes.' votes pour ce serveur '.seocat($catName).' et '.$data->clicks.' visites pour ce serveur ce mois-ci. Laissez un avis pour ce serveur MultiGames.')
@section('logometa', 'https://serveur-multigames.net/storage/icone/icon'.$data->id.'.jpg')
@section('jquery', '')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName).'/'.encname($data->name))
@extends('front.index')
@section('content')
    @if (file_exists('storage/banner/banner'.$data->id.'.jpg'))
        @section('banner', 'storage/banner/banner'.$data->id.'.jpg')
    @endif
    <section class="banner-area-{{ $catName }} relative" id="home"
@if (trim($__env->yieldContent('banner')))style="background-image: url('@yield('banner')');"
@endif
             >
        <div class="overlay overlay-"></div>
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
                            {{ seocat($catName) }}
                        </h2>
                        <p class="text-white link-nav"><a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a>  <span class="lnr lnr-arrow-right"></span> <a title="Serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}">  {{ $data -> name}} </a></p>
                    @endif
                </div>
            </div>
        </div>
    </section>
    <section class="post-area section-gap">
        <div class="container">
            @if(isset($erreur))
                <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}" class="genric-btn danger" id="explicitbtn">
                    <span class="lnr lnr-cross-circle"></span> &nbsp;<strong>Attention !</strong> {{ $erreur }}
                </a><br /><br />
            @endif
            @if (isset($vote))
                <meta http-equiv="refresh" content="5;url=/{{ $catName }}" />
                <a title="Information de vote" href="/{{ $catName }}/{{ encname($data->name) }}/vote" class="genric-btn success" id="explicitbtn">
                    <img alt="Chargement du vote pour {{ $data->name }}" title="Chargement du vote pour {{ $data->name }}" src="img/loading.gif" width="64" height="64" /> <strong>Patientez quelques instants</strong> Nous prenons en compte votre vote.. Vous serez redirigé.
                </a><br /><br />
            @endif
            <div class="row justify-content-center d-flex">
                <div class="col-lg-8 post-list">
                    @if (trim($__env->yieldContent('banner')))
                        <p class="link-nav"><a title="Liste {{ seocat($catName) }}" href="/{{ $catName }}">{{ seocat($catName) }}</a> &nbsp; <span class="lnr lnr-arrow-right"></span>  &nbsp;<a title="Serveur {{ seocat($catName) }} {{ encname($data->name) }}" href="/{{ $catName }}/{{ encname($data->name) }}">{{ $data->name }}</a></p>
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
                                            <a title="Serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}">@if (trim($__env->yieldContent('banner')))<h1 style="font-size: 16px;">{{ $data->name }}</h1>@else<span style="font-size: 16px;">{{ $data->name }}</span>@endif</a>
                                            <div class="rate">
                                                @if (!$data->verified)
                                                    <span style="color: red;">Note : Propriété non validée</span>
                                                @else
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
                                                @endif
                                            </div>
                                            <iframe src="https://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fserveur-multigames.net%2F{{ $catName }}&amp;layout=button_count&amp;show_faces=true&amp;width=50&amp;action=like&amp;font&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:80px; height:21px;" allowTransparency="true"></iframe>
                                            <br />
                                            <a rel="nofollow noopener noreferrer" title="Twitter Serveur {{ seocat($catName) }} {{ $data->name }}" href="https://twitter.com/share?ref_src=twsrc%5Etfw" class="twitter-share-button" data-text="Découvrez et votez pour le serveur {{ seocat($catName) }} {{ $data->name }} dès maintenant sur le classement des meilleurs serveurs {{ strtolower(seocat($catName)) }} !" data-via="SMultiGames" data-hashtags="serveur{{ strtolower(encname($catName)) }} #classement #liste" data-lang="fr" data-show-count="false">Tweet</a>
                                        </div>
                                        <ul class="btns">
                                            <li id="vote"><a title="Voter pour le serveur {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}/vote">VOTER <span class="lnr lnr-heart"></span></a></li>@if (!empty($data->website))&nbsp;<li><span class="lnr lnr-rocket"></span> <a title="Jouer au serveur {{ $data->name }}" href="/{{ $catName }}/{{ encname($data->name) }}/go" target="_blank">JOUER</a></li>@endif
                                        </ul>
                                    </div>
                                    <p style="margin-left: 10px; padding: 3px;">
                                        {!! preg_replace( "/\r|\n/", "", mb_strimwidth($data->short_desc, 0, 5000, "...")) !!}<br /><br />
                                    <ul class="tags">
                                        Type de serveur <span class="lnr lnr lnr-tag"></span> &nbsp;
                                        @foreach(json_decode($data->tag) as $key)<a id="tag-{{ $key }}" title="Serveur {{ seocat($catName) }} {{ $key }}" href="/{{ $catName }}/tag/{{ enctag($key) }}">{{ ucfirst($key) }}</a>&nbsp;@endforeach
                                    </ul>
                                    @if (strlen(trim(strtolower($data->ip))) > 0)<div class="hidden-ip" id="ip-{{ encname($data->name) }}">{{ trim(strtolower($data->ip)) }}</div>@endif
                                    </p>
                                </div>
                            </div>
                            @if (isset($data->description) && $data->description != $data->short_desc)
                            <div class="single-post d-flex flex-row">
                                <div class="details">
                                    <h3 class="text-uppercase">Informations sur le serveur {{ $data->name }}</h3><br />
                                    {!! preg_replace( "/\r|\n/", "", mb_strimwidth($data->description, 0, 5000, "...")) !!}
                                </div>
                            </div>
                            @endif
                    @if (strlen(trim(strtolower($data->ip))) > 0 and strlen(trim(strtolower($playerstats))) > 0)
                        <div class="single-post d-flex flex-row">
                            <div class="details">
                                <h5 class="text-uppercase">Statistiques</h5><br />
                                @if (!$data->verified)
                                    <span style="color: red">La propriété doit être validée depuis le tableau de bord du propriétaire du serveur afin que la fonctionnalité des statistiques du serveur puisse fonctionner.</span>
                                @else
                                    <div id="container"></div>
                                @endif
                            </div>
                        </div>
                    @endif

                        <div class="single-post d-flex flex-row">
                                <div class="details">
                                    <h5 class="text-uppercase">Avis sur {{ $data->name }}</h5><br />
                                    @if ($data->verified)
                                <form method="post" class="col-lg-12">
                                    <div class="mt-10">
                                        <div class="rate"><fieldset class="rate3">
                                                Saisissez une note :&nbsp;
                                                <input id="rate2-star5" type="radio" name="rate2" value="5" />
                                                <label for="rate2-star5" title="Énorme !">5</label>

                                                <input id="rate2-star5-half" type="radio" name="rate2" value="4.5" />
                                                <label class="star-half" for="rate2-star5-half" title="Excellent !">4.5</label>

                                                <input id="rate2-star4" type="radio" name="rate2" value="4" />
                                                <label for="rate2-star4" title="Très bon serveur">4</label>

                                                <input id="rate2-star3-half" type="radio" name="rate2" value="3.5" />
                                                <label class="star-half" for="rate2-star3-half" title="Bon serveur">3.5</label>

                                                <input id="rate2-star3" type="radio" name="rate2" value="3" />
                                                <label for="rate2-star3" title="Serveur satisfaisant">3</label>

                                                <input id="rate2-star2-half" type="radio" name="rate2" value="2.5" />
                                                <label class="star-half" for="rate2-star2-half" title="Serveur peu satisfaisant">2.5</label>

                                                <input id="rate2-star2" type="radio" name="rate2" value="2" />
                                                <label for="rate2-star2" title="Mauvais serveur">2</label>

                                                <input id="rate2-star1-half" type="radio" name="rate2" value="1.5" />
                                                <label class="star-half" for="rate2-star1-half" title="Très mauvais serveur">1.5</label>

                                                <input id="rate2-star1" type="radio" name="rate2" value="1" />
                                                <label for="rate2-star1" title="Affreux">1</label>

                                                <input id="rate2-star0-half" type="radio" name="rate2" value="0.5" />
                                                <label class="star-half" for="rate2-star0-half" title="Horrible">0.5</label>
                                            </fieldset>
                                        </div><br />
                                        {{ csrf_field() }}
                                        <textarea class="single-textarea" style="background-color: white;" name="text" placeholder="Saisissez votre avis" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Saisissez votre avis'" required>{{ old('text') }}</textarea>
                                    </div>
                                    <div class="mt-10">
                                        <input type="submit" class="genric-btn primary circle" value="Envoyer votre avis" />
                                    </div>
                                </form><br /><br />
                            @foreach($data->comment as $row)
                                <div class="single-comment justify-content-between d-flex">
                                    <div class="user justify-content-between d-flex">
                                        <div class="thumb">
                                            <img src="https://guiria.badblock.fr/head/{{ $row->username }}/64.png" alt="Joueur {{ $row->username }}" style="border-radius: 10px;">
                                        </div>
                                        <div class="desc">
                                            <h5 style="margin-bottom: 0px;">&nbsp;&nbsp;&nbsp;{{ $row->username }}</h5>
                                            <div class="rate">
                                                &nbsp;&nbsp; @if($row->note >= 1)
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

                                            <p class="comment" style="padding: 3px; margin-left: 10px;">
                                                {{ $row->comment }}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <br />
                            @endforeach
                                    @else
                                        <span style="color: red">La propriété doit être validée depuis le tableau de bord du propriétaire du serveur afin que la fonctionnalité des Avis du serveur puisse fonctionner.</span>
                                    @endif
                                </div>
                        </div>
                </div>

                <div class="col-lg-4 sidebar">
                    <div class="single-slidebar" style="background-color: white;">
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated">
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
                        </div>
                    </div>
                    <div class="single-slidebar">
                        <h4>Informations sur ce serveur</h4>
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated">
                                Plus de {{ $data->clicks }} personnes ont découvert le serveur {{ seocat($catName) }} {{ $data->name }} ce mois-ci et {{ $data->votes }} votes ont été effectués sur ce serveur {{ seocat($catName) }}.
                            </div>
                        </div>
                    </div>
                    <div class="single-slidebar">
                        <h4>A propos de ce Serveur MultiGames</h4>
                        <div class="active-relatedjob-carusel">
                            <div class="single-rated">
                                Serveur MultiGames classe les meilleurs serveurs {{ seocat($catName) }} francophones, le serveur {{ seocat($catName) }} y est inscrit. Découvrez un large choix de serveurs ajoutés par les créateurs. Postez également votre expérience sur ce serveur, {{ $data->reviews }} avis sont actuellemet publiés. Si {{ $data->name  }} est votre serveur et que vous ne l'avez pas ajouté, veuillez nous en informer.
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        </div>
    </section>
    <script type="application/ld+json">
            {
                "@context": "http://schema.org/",
                "@type": "AggregateRating",
                "itemReviewed": {
                "@type": "GameServer",
                "name": "Serveur {{ seocat($data->cat) }} {{ $data->name }}",
                "description": "{{ $data->short_desc }}",
                "playersOnline": "",
                "url": "https://serveur-multigames.net/{{ encname($catName) }}/{{ encname($data->name) }}",
                @if ($data->reviews > 0)
            "ratingValue": "{{ $data->reviews }}",
                @else
            "ratingValue": "5",
    @endif
        @if ($data->note > 0)
            "ratingCount": "{{ $data->note }}"
                @else
            "ratingCount": "9"
    @endif
        }
    </script>


@endsection
@section('after_script')
    <style>
        @import url("//maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css");

        /* Basic styles */

        .row3 {
            text-align: center;
        }

        .rate3 {
            display: inline-block;
            margin: 0;
            width: 215px;
            padding: 0;
            border: none;
        }

        input {
            display: none;
        }

        label {
            float: right;
            font-size: 0;
            color: #d9d9d9;
        }

        label:before {
            content: "\f005";
            font-family: FontAwesome;
            font-size: 16px;
        }

        label:hover,
        label:hover ~ label {
            color: #fcd000;
            transition: 0.2s;
        }

        input:checked ~ label {
            color: #ccac00;
        }

        input:checked ~ label:hover,
        input:checked ~ label:hover ~ label {
            color: #fcd000;
            transition: 0.2s;
        }


        /* Half-star*/

        .star-half {
            position: relative;
        }

        .star-half:before {
            position: absolute;
            content: "\f089";
            padding-right: 0;
        }
    </style>
    <script async defer>
        function copy(displ, serv)
        {
            var copyText = document.getElementById("ip-" + serv);
            var textArea = document.createElement("textarea");
            textArea.value = copyText.textContent;
            document.body.appendChild(textArea);
            textArea.select();
            document.execCommand("copy");
            textArea.remove();
            var request = $.ajax({
                url: "/{{ seocat($catName) }}/" + serv + "/ip",
                method: "GET",
                dataType: "html",
                success: function (data) {
                }
            });
            alert('Vous avez copié l\'adresse IP du Serveur {{ seocat($catName) }} ' + displ + ".");
        }
    </script>
    <script async defer src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
    @if (strlen(trim(strtolower($data->ip))) > 0 and strlen(trim(strtolower($playerstats))) > 0)
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="https://code.highcharts.com/modules/series-label.js"></script>
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

@endsection
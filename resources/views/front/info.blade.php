@section('title', $data->name.' | Serveur '.seocat($catName).' A Découvrir - Serveur MultiGames')
@section('description', 'Découvrez le serveur '.seocat($catName).' '.$data->name.' gratuit français. Déjà plus de '.$data->votes.' votes pour ce serveur '.seocat($catName).' et '.$data->clicks.' visites pour ce serveur ce mois-ci. Laissez un avis pour ce serveur MultiGames.')
@section('logometa', 'https://serveur-multigames.net/storage/icone/icon'.$data->id.'.jpg')
@section('jquery', '')
@section('canonical', 'https://serveur-multigames.net/'.encname($catName).'/'.encname($data->name))
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
                                    <h4 class="text-white">Serveur {{ seocat($catName) }}</h4>
                                </div>
                            </div>
                    </div>
                    <div class="col-12 col-md-4 align-self-center">
                        <ul class="list-unstyled mb-0 lis-line-height-2 text-md-right text-center">
                            <li>
                                <i class="fa fa-map-o pr-2"></i> <a title="Serveur MultiGames" href="/" class="text-white">Accueil</a> /
                                <a title="Liste {{ seocat($data->cat) }}" href="/{{ encname($catName) }}" class="text-white">{{ seocat($data->cat) }}</a> /
                                <a title="Serveur {{ seocat($data->cat) }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}" class="text-white">{{ $data->name }}</a>
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
                        <a title="Voter pour le serveur {{ seocat($catName) }} {{ $data->name }}" href="/{{ encname($catName) }}/{{ encname($data->name) }}/vote" class="btn btn-success btn-default"> <i class="fa fa-check-circle-o pr-1"></i> Voter</a>
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
                    <div class="col-12 col-lg-4"><script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
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
                            </div>
                        </div>
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> A propos de ce Serveur MultiGames</h6>
                        <div class="card lis-brd-light mb-4 wow fadeInUp">
                            <div class="card-body p-4">
                                Serveur MultiGames classe les meilleurs serveurs {{ seocat($catName) }} francophones, le serveur {{ seocat($catName) }} y est inscrit. Découvrez un large choix de serveurs ajoutés par les créateurs. Postez également votre expérience sur ce serveur, {{ $data->reviews }} avis sont actuellement publiés. Si {{ $data->name  }} est votre serveur et que vous ne l'avez pas ajouté, veuillez nous en informer.
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-lg-8 mb-5 mb-lg-0">
                    @if(isset($erreur))
                            <div class="alert alert-danger">
                                <strong>Attention !</strong> {{ $erreur }}
                            </div>
                    @endif
                    @if (isset($vote))
                        @if ($data->noredirect == 1)
                                <div class="alert alert-success">
                                    <span>Merci !</span> &nbsp; <i class="fa fa-check-circle-o pr-1"></i> Votre vote a bien été pris en compte. Merci de votre soutien.
                                </div>
                        @else
                                <div class="alert alert-success">
                                    <span>Merci !</span> &nbsp; <i class="fa fa-check-circle-o pr-1"></i> Votre vote a bien été pris en compte. Vous allez être redirigé vers le classement.
                                </div>
                                <meta http-equiv="refresh" content="2;url=/{{ $catName }}" />
                        @endif
                    @endif

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
                    <div class="tab-pane fade show active" id="venue" role="tabpanel" aria-labelledby="venue">
                        <h6 class="lis-font-weight-500">
                            <i class="fa fa-align-right pr-2 lis-f-14"></i> Description
                        </h6>

                        <div class="card lis-brd-light mb-4 wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                            <div class="card-body p-4">
                                <p>
                                    {!! preg_replace( "/\r|\n/", "", mb_strimwidth($data->description, 0, 5000, "...")) !!}
                                </p>
                            </div>
                        </div>

                    @if (strlen(trim(strtolower($data->ip))) > 0 and strlen(trim(strtolower($playerstats))) > 0)
                        <h6 class="lis-font-weight-500"><i class="fa fa-align-right pr-2 lis-f-14"></i> Statistiques </h6>
                                <div class="card lis-brd-light mb-4 wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                                    <div class="card-body p-4">
                                        @if (!$data->verified)
                                            <div class="alert alert-warning" role="alert">
                                                <strong>Attention</strong> La propriété du serveur doit être validée afin que cette fonctionnalité soit accessible. <a title="Connexion au classement de serveur {{ seocat($catName) }}" href="/login">Se connecter au Tableau de Bord</a>.
                                            </div>
                                        @else
                                            <div id="container"></div>
                                        @endif
                                    </div>
                        </div>
                    @endif


                        <h6 class="lis-font-weight-500"><i class="fa fa-commenting pr-2 lis-f-14"></i>  Ajouter un avis</h6>
                        <div class="card lis-brd-light wow mb-4 fadeInUp">
                            <div class="card-body p-4">
                                @if (!Auth::user())
                                    <div class="alert alert-danger" role="alert">
                                        <strong>Attention</strong> Vous devez être connecté pour pouvoir ajouter un avis sur ce serveur de jeu. Liens utiles : <a title="Connexion à Serveur MultiGames" href="/login">Se connecter</a> ou <a title="Créer un compte sur Serveur MultiGames" href="/register">s'inscrire</a>.
                                    </div>
                                @else
                                    <p class="mb-2">Cliquez sur la note souhaitée :</p>
                                    <ul class="list-inline lis-light-gold font-weight-normal h5">
                                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                        <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                    </ul>
                                    <div class="row">
                                        <div class="col-12 col-sm-12">
                                            <div class="form-group lis-relative">
                                                <textarea class="form-control border-top-0 border-left-0 border-right-0 rounded-0 pl-4" placeholder="Écrivez ici votre avis"></textarea>
                                                <div class="lis-search"> <i class="fa fa-pencil lis-primary lis-left-0 lis-top-10"></i> </div>
                                            </div>
                                        </div>
                                    </div> <input type="submit" name="submit" class="btn btn-primary btn-default mt-3" value="Envoyer" />
                                @endif
                            </div>
                        </div>

                            <h6 class="lis-font-weight-500"><i class="fa fa-signal pr-2 lis-f-14"></i> Avis</h6>


                            @foreach($data->comment as $row)
                            <div class="card lis-brd-light wow fadeInUp mb-4">
                                <div class="card-body p-4">
                                    <div class="media d-block d-sm-flex text-center text-sm-left">
                                        <img src="https://minotar.net/avatar/{{ $row->username }}/64.png" class="img-fluid d-md-flex mr-sm-4 rounded-circle" alt="{{ $row->username }}" width="60"></a>
                                        <div class="media-body align-self-center">
                                            <ul class="list-inline mb-0 lis-light-gold font-weight-normal h5 float-sm-right float-none my-md-0 my-3">
                                                @if($row->note >= 1)
                                                    <li class="list-inline-item"><i class="fa fa-star"></i></li>
                                                @else
                                                    <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                                @endif

                                                @if($row->note > 2 || $row->note == 2)
                                                    <li class="list-inline-item"><i class="fa fa-star"></i></li>
                                                @else
                                                    <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                                @endif

                                                @if($row->note > 3 || $row->note == 3)
                                                    <li class="list-inline-item"><i class="fa fa-star"></i></li>
                                                @else
                                                    <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                                @endif

                                                @if($row->note > 4 || $row->note == 4)
                                                    <li class="list-inline-item"><i class="fa fa-star"></i></li>
                                                @else
                                                    <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                                @endif

                                                @if($row->note == 5)
                                                    <li class="list-inline-item"><i class="fa fa-star"></i></li>
                                                @else
                                                    <li class="list-inline-item"><i class="fa fa-star-o"></i></li>
                                                @endif
                                            </ul>
                                            <h6 class="mb-0">{{ $row->username }}</h6>
                                            <p>{{ date_format(date_create($row->date), "d/m/Y H:i") }}</p>
                                            <p>{{ $row->comment }}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        @endforeach
                        @if (empty($data->comment))
                            <div class="alert alert-danger" role="alert">
                                <strong>Attention</strong> Aucun avis publié. Soyez le premier à en écrire un.
                            </div>
                        @endif
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
                "description": "{{ preg_replace( "/\r|\n/", "", mb_strimwidth($data->short_desc, 0, 5000, "...")) }}",
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
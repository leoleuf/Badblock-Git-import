@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Administration</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Tableau de bord</a></li>
                <li class="breadcrumb-item active">Administration</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
    <div class="row">
        <!-- Column -->
        <div class="col-lg-3 col-md-6">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex flex-row">
                        <div class="round round-lg align-self-center round-info"><i class="ti-wallet"></i></div>
                        <div class="m-l-10 align-self-center">
                            <h3 class="m-b-0 font-light">{{ $about['serveurCount'] }}</h3>
                            <h5 class="text-muted m-b-0">Serveurs inscrits</h5></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Column -->
        <!-- Column -->
        <div class="col-lg-3 col-md-6">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex flex-row">
                        <div class="round round-lg align-self-center round-warning"><i class="mdi mdi-cellphone-link"></i></div>
                        <div class="m-l-10 align-self-center">
                            <h3 class="m-b-0 font-light">{{ $about['voteCount'] }}</h3>
                            <h5 class="text-muted m-b-0">Votes ce mois-ci</h5></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Column -->
        <!-- Column -->
        <div class="col-lg-3 col-md-6">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex flex-row">
                        <div class="round round-lg align-self-center round-primary"><i class="mdi mdi-cart-outline"></i></div>
                        <div class="m-l-10 align-self-center">
                            <h3 class="m-b-0 font-lgiht">{{ $about['clickCount'] }}</h3>
                            <h5 class="text-muted m-b-0">Clics externes</h5></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Column -->
        <!-- Column -->
        <div class="col-lg-3 col-md-6">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex flex-row">
                        <div class="round round-lg align-self-center round-danger"><i class="mdi mdi-bullseye"></i></div>
                        <div class="m-l-10 align-self-center">
                            <h3 class="m-b-0 font-lgiht">{{ $about['copyCount'] }}</h3>
                            <h5 class="text-muted m-b-0">IP copiées</h5></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Column -->
    </div>
    <div class="col-lg-12 col-md-7">
        <div class="card">
            <div class="card-block">
                <div class="row">
                    <div class="col-12">
                        <div class="d-flex flex-wrap">
                            <div>
                                <h3 class="card-title">Statistiques des actions (aujourd'hui)</h3>
                                <h6 class="card-subtitle">Vue sur la quantité des actions</h6> </div>
                            <div class="ml-auto">
                                <ul class="list-inline">
                                    <li>
                                        <h6 class="text-muted text-info"><i class="fa fa-circle font-10 m-r-10 "></i>Vote(s)</h6>
                                    </li>
                                    <li>
                                        <h6 class="text-muted  text-success"><i class="fa fa-circle font-10 m-r-10"></i>Clic(s)</h6>
                                    </li>
                                    <li>
                                        <h6 class="text-warning"><i class="fa fa-circle font-10 m-r-10"></i>IP copiées</h6>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="amp-pxl" style="height: 360px;"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-12 col-md-7">
        <div class="card">
            <div class="card-block">
                <div class="row">
                    <div class="col-12">
                        <div class="d-flex flex-wrap">
                            <div>
                                <h3 class="card-title">Statistiques des actions (1 mois glissant)</h3>
                                <h6 class="card-subtitle">Vue sur la quantité des actions</h6> </div>
                            <div class="ml-auto">
                                <ul class="list-inline">
                                    <li>
                                        <h6 class="text-muted text-info"><i class="fa fa-circle font-10 m-r-10 "></i>Vote(s)</h6>
                                    </li>
                                    <li>
                                        <h6 class="text-muted  text-success"><i class="fa fa-circle font-10 m-r-10"></i>Clic(s)</h6>
                                    </li>
                                    <li>
                                        <h6 class="text-warning"><i class="fa fa-circle font-10 m-r-10"></i>IP copiées</h6>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="amp-pxl2" style="height: 360px;"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection

@section('after_script')
    <script>
        $(function() {
            "use strict";

            var chart2 = new Chartist.Bar('.amp-pxl', {
                labels: [
                    @foreach($data as $k => $row)
                        "{{ $row[0] }}h",
                    @endforeach
                ],
                series: [
                    [
                        @foreach($data as $k => $row)
                        {{ $row[1] }},
                        @endforeach
                    ],
                    [
                        @foreach($data as $k => $row)
                        {{ $row[2] }},
                        @endforeach
                    ],
                    [
                        @foreach($data as $k => $row)
                        {{ $row[3] }},
                        @endforeach
                    ]
                ]
            }, {
                axisX: {
                    position: 'end',
                    showGrid: false
                },
                axisY: {
                    position: 'start'
                },
                low: '0',
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
        });
    </script>
    <script>
        $(function() {
            "use strict";

            var chart2 = new Chartist.Bar('.amp-pxl2', {
                labels: [
                    @foreach($datam as $k => $row)
                        "{{ date("m-d", strtotime($row[0])) }}",
                    @endforeach
                ],
                series: [
                    [
                        @foreach($datam as $k => $row)
                        {{ $row[2] }},
                        @endforeach
                    ],
                    [
                        @foreach($datam as $k => $row)
                        {{ $row[3] }},
                        @endforeach
                    ],
                    [
                        @foreach($datam as $k => $row)
                        {{ $row[4] }},
                        @endforeach
                    ]
                ]
            }, {
                axisX: {
                    position: 'end',
                    showGrid: false
                },
                axisY: {
                    position: 'start'
                },
                low: '0',
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
        });
    </script>
@endsection
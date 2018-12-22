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
        <div class="col-md-7 col-4 align-self-center">
            <div class="d-flex m-t-10 justify-content-end">
                <div class="d-flex m-r-20 m-l-10 hidden-md-down">
                    <div class="chart-text m-r-10">
                        <h6 class="m-b-0"><small>Serveurs inscrits</small></h6>
                        <h4 class="m-t-0 text-info">{{ $about->serveurCount }}</h4>
                    </div>
                </div>
                <div class="d-flex m-r-20 m-l-10 hidden-md-down">
                    <div class="chart-text m-r-10">
                        <h6 class="m-b-0"><small>Votes ce mois-ci</small></h6>
                        <h4 class="m-t-0 text-primary">{{ $about->voteCount }}</h4>
                    </div>
                </div>
                <div class="d-flex m-r-20 m-l-10 hidden-md-down">
                    <div class="chart-text m-r-10">
                        <h6 class="m-b-0"><small>Clics externes ce mois-ci</small></h6>
                        <h4 class="m-t-0 text-primary">{{ $about->clickCount }}</h4>
                    </div>
                </div>
                <div class="d-flex m-r-20 m-l-10 hidden-md-down">
                    <div class="chart-text m-r-10">
                        <h6 class="m-b-0"><small>IP copiées ce mois-ci</small></h6>
                        <h4 class="m-t-0 text-primary">{{ $about->copyCount }}</h4>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
@section('content')
    <div class="col-lg-12">
        @if (session()->has('flash'))
            @foreach(session('flash') as $messageData)
                <div class="alert alert-{{ $messageData['level'] }} {{ $messageData['important'] ? 'alert-important' : '' }}">
                    @if(!$messageData['important'])
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    @endif

                    {!! trans($messageData['message']) !!}
                </div>
            @endforeach
        @endif
        <div class="card">
            <div class="card-block">
                <h4 class="card-title">Serveurs à valider</h4>
                @if (count($server) == 0)
                    <div class="col-lg-12">
                        <div class="alert alert-danger">
                            Aucun serveur en attente pour le moment.
                        </div>
                    </div>
                @else
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Serveur</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            @foreach($server as $row)
                                <tr>
                                    <td>
                                        <img alt="{{ $row->name }}" src="https://serveur-multigames.net/storage/icone/icon{{ $row->id }}.jpg" height="69" width="69">
                                        <br />{{ $row->name }}<br />
                                        <i>Serveur {{ $row->cat }}</i><br /><br />
                                        <i>Créé le {{ $row->created_at }}</i><br /><br />
                                        <a title="Valider le serveur" href="/dashboard/admin/validate/{{ $row->id }}" class="btn btn-primary"><u>Valider</u></a>
                                    </td>
                                    <td>
                                        <strong>Tags :</strong> {{ $row->tag }}<br /><br />
                                        <strong>Site Internet :</strong> <a rel="nofollow noreferer external" href="{{ $row->website }}">{{ $row->website }}</a><br /><br />
                                        <strong>IP du serveur :</strong> {{ $row->ip }}<br /><br />
                                        <strong>Type de votes :</strong> {{ $row->votetype }}<br /><br />
                                        <strong>Description courte :</strong> {{ preg_replace( "/\r|\n/", "", mb_strimwidth($row->short_desc, 0, 501, "...")) }}<br /><br />
                                        <strong>Description longue :</strong> {{ preg_replace( "/\r|\n/", "", mb_strimwidth($row->description, 0, 1000, "...")) }}
                                    </td>
                                </tr>
                            @endforeach
                            </tbody>
                        </table>
                    </div>
                @endif
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
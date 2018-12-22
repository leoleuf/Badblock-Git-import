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
        <div class="card">
            <div class="card-block">
                <h4 class="card-title">Serveurs à valider</h4>
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
                                <td>{{ $row->name }}<br />
                                    <i>Serveur {{ $row->cat }}</i><br /><br />
                                    @if($row->actived)
                                        @if($row->verified)
                                            <span style="background-color: #0fff21" class="badge badge-dark">Serveur vérifié</span>
                                        @else
                                            <span style="background-color: red;" class="badge badge-dark">Serveur non vérifié<br /><br /><a title="Vérifier le serveur" href="/dashboard/verify/{{ $row->id }}" style="color: white;"><u>Régler</u></a></span>
                                        @endif
                                    @else
                                        <span style="background-color: #ff100b" class="badge badge-dark">En attente de validation<br />par la modération</span>
                                    @endif
                                </td>
                                @if (!$row->verified)
                                    <td style="display: flex;">
                                        <div class="col-lg-10">
                                            <button title="Vérifier le serveur {{ $row->name }}" onclick="location.href = '/dashboard/verify/{{ $row->id }}';" type="button" role="button" class="btn btn-primary" style="width: 100%; height:50px;">
                                                <i class="mdi mdi-checkbox-marked-circle-outline"></i> Vérifier le serveur
                                            </button>
                                        </div>
                                    </td>
                                @endif
                                <td style="display: flex;">
                                <!--<button title="Supprimer le serveur {{ $row->name }}" onclick="location.href = '/dashboard/del-server/{{ $row->id }}';" type="button" role="button" class="btn btn-danger">
                                            <i class="mdi mdi-close-circle"></i>
                                        </button>!-->
                                    &nbsp;
                                    <div class="col-lg-5">
                                        <button title="Modifier le serveur {{ $row->name }}" onclick="location.href = '/dashboard/edit-server/{{ $row->id }}';" type="button" role="button" class="btn btn-default" style="width: 100%; height:50px;">
                                            <i class="mdi mdi-border-color"></i> Modifier mon serveur
                                        </button>
                                    </div>
                                    &nbsp;
                                    <div class="col-lg-5">
                                        <button title="Intégrer à votre site" onclick="location.href = '/dashboard/api/{{ $row->id }}';" type="button" role="button" class="btn btn-default" style="width: 100%; height:50px;">
                                            <i class="mdi mdi-code-tags-check"></i> Intégrer le système de vote
                                        </button>
                                    </div>

                                </td>

                                <td style="display: flex;">
                                    <div class="col-lg-5">
                                        <button title="Statistiques du serveur" onclick="location.href = '/dashboard/stats/{{ encname($row->name) }}';" type="button" role="button" class="btn btn-default" style="width: 100%; height:50px;">
                                            <i class="mdi mdi mdi-gauge"></i> Statistiques du serveur
                                        </button>
                                    </div>
                                    &nbsp;
                                    <div class="col-lg-5">
                                        <button title="Accéder à la fiche du serveur" onclick="location.href = '/{{ encname($row->cat) }}/{{ encname($row->name) }}';" type="button" role="button" class="btn btn-default" style="width: 100%; height:50px;">
                                            <i class="mdi mdi-newspaper"></i> Fiche du serveur
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        @endforeach
                        </tbody>
                    </table>
                    @if (count($server) == 0)
                        <div class="col-lg-12">
                                <div class="alert alert-danger}">
                                    Aucun serveur en attente pour le moment.
                                </div>
                            @endforeach
                        </div>
                    @endif
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
@extends('layouts.app')

@section('content')
<div class="content-page">
        <!-- Start content -->
    <div class="content">
        <div class="container">

            <h1 class="page-title">Badblock Manager</h1>

            <div class="row">

                <div class="col-xl-3 col-md-6">
                    <div class="card-box">
                        <h4 class="header-title mt-0 m-b-30">Joueurs connectés</h4>

                        <div class="widget-box-2">
                            <div class="widget-detail-2">
                                <h2 class="mb-0"> XXX </h2>
                                <p class="text-muted m-b-25">Joueurs connectés</p>
                            </div>
                            <div class="progress progress-bar-success-alt progress-sm mb-0">
                                <div class="progress-bar progress-bar-success" role="progressbar"
                                     aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                     style="width: 77%;">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-md-6">
                    <div class="card-box">
                        <h4 class="header-title mt-0 m-b-30">Joueurs bannis</h4>

                        <div class="widget-box-2">
                            <div class="widget-detail-2">
                                <h2 class="mb-0"> XXX </h2>
                                <p class="text-muted m-b-25">Joueurs bannis</p>
                            </div>
                            <div class="progress progress-bar-danger-alt progress-sm mb-0">
                                <div class="progress-bar progress-bar-danger" role="progressbar"
                                     aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                     style="width: 77%;">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-md-6">
                    <div class="card-box">
                        <h4 class="header-title mt-0 m-b-30">Joueurs mute</h4>

                        <div class="widget-box-2">
                            <div class="widget-detail-2">
                                <h2 class="mb-0"> XXX </h2>
                                <p class="text-muted m-b-25">Joueurs mute</p>
                            </div>
                            <div class="progress progress-bar-warning-alt progress-sm mb-0">
                                <div class="progress-bar progress-bar-warning" role="progressbar"
                                     aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                     style="width: 77%;">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="col-xl-3 col-md-6">
                    <div class="card-box">
                        <h4 class="header-title mt-0 m-b-30">Objectif (en %)</h4>

                        <div class="widget-chart-1">
                            <div class="widget-chart-box-1">
                                <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#f05050 "
                                       data-bgColor="#F9B9B9" value="58"
                                       data-skin="tron" data-angleOffset="180" data-readOnly=true
                                       data-thickness=".15"/>
                            </div>

                            <div class="widget-detail-1">
                                <h2 class="p-t-10 mb-0"> 2000h </h2>
                                <p class="text-muted m-b-10">Temps de jeu ce mois</p>
                            </div>
                        </div>
                    </div>
                </div>


            </div>

            <div class="row">
                <div class="card-box">
                    <h4 class="header-title mt-0">Joueurs connectés ce mois-ci</h4>
                    <canvas id="monthlyPlayers" class="morris-chart" width="800" height="450"></canvas>
                </div>
            </div>

            <div class="row">
                <div class="card-box">
                    <h4 class="header-title mt-0">Objectifs staff (en %)</h4>
                    <canvas id="staffGoals" width="800" height="450"></canvas>
                </div>
            </div>

        </div>
    </div>
</div>

@endsection

@section('after_scripts')

    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>

    <script>
        new Chart($("#monthlyPlayers"), {
            type: 'line',
            data: {
                labels: [
                    @for($i = 1; $i <= date("t"); $i++)
                    {{ $i }}
                        @if($i != date("t"))
                            ,
                        @endif
                    @endfor
                ],
                datasets: [{
                    data: [
                        @for($i = 1; $i <= date("t"); $i++)
                        {{ rand(0, 2500) }}
                            @if($i != date("t"))
                            ,
                            @endif
                        @endfor
                    ],
                    label: "Nombre de joueurs connectés ce mois-ci",
                    borderColor: "#3e95cd",
                    fill: false
                }
                ]
            },
            options: {
            }
        });

        new Chart($("#staffGoals"), {
            type: 'bar',
            data: {
                labels: ["Modérateur1", "Modérateurs 2", "Modérateurs 3", "Modérateurs 4", "Modérateurs 5"],
                datasets: [
                    {
                        label: "Objectifs staff",
                        backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#e8c3b9","#c45850"],
                        data: [
                            @for($i = 1; $i <= 5; $i++)
                                {{ rand(0, 100) }}
                                @if($i != 5)
                                ,
                                @endif
                            @endfor
                        ]
                    }
                ]
            }
        });



    </script>

@endsection

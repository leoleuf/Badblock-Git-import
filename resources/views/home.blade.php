@extends('layouts.app')

@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content home-content">
            <div class="container">

                <h1 class="page-title">Badblock Manager </h1>

                <div class="row">

                    <div class="col-xl-4 col-md-6">
                        <div class="card-box">
                            <h4 class="header-title mt-0 m-b-30">Joueurs connectés</h4>

                            <div class="widget-box-2">
                                <div class="widget-detail-2">
                                    <h2 class="mb-0" id="players"> 0 </h2>
                                    <p class="text-muted m-b-25">Joueurs connecté(s)</p>
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

                    <div class="col-xl-4 col-md-6">
                        <div class="card-box">
                            <h4 class="header-title mt-0 m-b-30">Joueurs bannis</h4>

                            <div class="widget-box-2">
                                <div class="widget-detail-2">
                                    <h2 class="mb-0" id="players-ban"> 0 </h2>
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
                </div>
            </div>
        </div>
    </div>

@endsection

@section('after_scripts')
    <script>

        function players() {
            //Get players
            $.getJSON('/api/minecraft', function (data) {
                $('#players').html(data.players.now);
                setTimeout(players, 5000);
            });
        }

        players();

        function ban() {
            //Get players
            $.getJSON('/api/ban', function (data) {
                $('#players-ban').html(data.players.now);
                setTimeout(ban, 10000);
            });
        }

        ban();

    </script>

@endsection

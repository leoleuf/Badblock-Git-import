@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Tableau de Bord</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item active">Votes de {{ $data->name }}</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
        <!-- column -->
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Derniers votes pour votre serveur</h4>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Date & Heure</th>
                                <th>ID du vote</th>
                                <th>IP du votant</th>
                                @if ($data->votetype == "VOTIFIER")
                                <th>Nom d'utilisateur</th>
                                @endif
                            </tr>
                            </thead>
                            <tbody>
                            @foreach($votes as $row)
                                <tr>
                                    <td>{{ $row->date }}</td>
                                    <td>{{ $row->id }}</td>
                                    <td>{{ $row->ip }}</td>
                                    @if ($data->votetype == "VOTIFIER")
                                    <td>{{ $row->username }}</td>
                                    @endif
                                </tr>
                            @endforeach
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
@endsection
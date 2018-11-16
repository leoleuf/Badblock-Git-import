@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Statistiques de vos serveurs</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Tableau de bord</a></li>
                <li class="breadcrumb-item active">Statistiques</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
        <!-- column -->
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Liste de mes serveurs :</h4>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Ref°</th>
                                <th>Nom du serveur</th>
                                <th>IP</th>
                                <th>Vote(s)</th>
                                <th>Click(s)</th>
                                <th>#</th>
                            </tr>
                            </thead>
                            <tbody>
                            @foreach($server as $row)
                                <tr>
                                    <td>{{ $row->id }}</td>
                                    <td>{{ $row->name }}</td>
                                    <td>{{ $row->ip }}</td>
                                    <td>{{ $row->votes }}</td>
                                    <td>{{ $row->clicks }}</td>
                                    <td>
                                        <button onclick="location.href = '/dashboard/stats/{{ encname($row->name) }}';" type="button" role="button" class="btn btn-danger">
                                            Voir
                                        </button>
                                    </td>
                                </tr>
                            @endforeach
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
@endsection
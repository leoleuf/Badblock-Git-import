@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Dernières connexions</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Tableau de bord</a></li>
                <li class="breadcrumb-item active">Log de modification</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
        <!-- column -->
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Enregistrement des actions du compte :</h4>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>IP</th>
                                <th>Action</th>
                                <th>Succès</th>
                            </tr>
                            </thead>
                            <tbody>
                            @foreach($data as $row)
                                <tr>
                                    <td>{{ $row->date }}</td>
                                    <td>{{ $row->ip }}</td>
                                    <td>{{ $row->action }}</td>
                                    <td>
                                    @if($row->success)
                                        <span style="background-color: #0fff21" class="badge badge-dark">Oui</span>
                                    @else
                                        <span style="background-color: #ff100b" class="badge badge-dark">Non</span>
                                    @endif
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
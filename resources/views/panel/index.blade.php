@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Tableau de Bord</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item active">Tableau de bord</li>
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
                                <th>Serveur</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            @foreach($server as $row)
                                <tr>
                                    <td>{{ $row->name }}<br />
                                        @if($row->actived)
                                            <span style="background-color: #0fff21" class="badge badge-dark">Serveur affiché</span>
                                        @else
                                            <span style="background-color: #ff100b" class="badge badge-dark">En attente de validation</span>
                                        @endif
                                    </td>
                                    <td class="row">
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
                                                    <i class="mdi mdi-code-tags-check"></i> Intégrer le vote<br />à votre serveur
                                                </button>
                                            </div>

                                    </td>

                                    <td class="row">
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
                    </div>
                </div>
            </div>
        </div>
@endsection
@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h3>Messages d'Event <small>(en jeu)</small></h3>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="card-box">
                                    <div class="dropdown pull-right">
                                        <a href="#" class="dropdown-toggle arrow-none card-drop" data-toggle="dropdown" aria-expanded="false">
                                            <i class="mdi mdi-dots-vertical"></i>
                                        </a>
                                        <div class="dropdown-menu dropdown-menu-right" x-placement="bottom-end" style="position: absolute; transform: translate3d(-140px, 28px, 0px); top: 0px; left: 0px; will-change: transform;">
                                            <!-- item-->
                                            <a href="javascript:void(0);" class="dropdown-item">Action</a>
                                            <!-- item-->
                                            <a href="javascript:void(0);" class="dropdown-item">Another action</a>
                                            <!-- item-->
                                            <a href="javascript:void(0);" class="dropdown-item">Something else</a>
                                            <!-- item-->
                                            <a href="javascript:void(0);" class="dropdown-item">Separated link</a>
                                        </div>
                                    </div>
                                    <h4 class="m-t-0 header-title">Liste des messages.</h4>
                                    <p class="text-muted font-14 m-b-20">
                                        Attention à bien respecter les dates de mises en ligne.
                                    </p>

                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Texte</th>
                                            <th>Début</th>
                                            <th>Fin</th>
                                            <th>Action</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        @foreach($list as $key => $value)
                                            <tr>
                                                <th scope="row">{{ $key + 1 }}</th>
                                                <td>{{ $value }}</td>
                                                <td>N/A</td>
                                                <td>N/A</td>
                                                <td><button class="btn btn-success">Modifier</button><button class="btn btn-danger">Supprimer</button></td>
                                            </tr>
                                        @endforeach

                                        </tbody>
                                    </table>
                                </div>
                                <div class="card-box">

                                    <form method="post" action="/animation/msg-anim">
                                        {{ csrf_field() }}
                                        <div class="form-group">
                                            <input class="form-control" name="msg" placeholder="Entrez votre message...">
                                        </div>
                                        <button type="submit" class="btn btn-success">Ajouter</button>
                                    </form>
                                </div>
                            </div>
                            <!-- end: panel body -->

                        </div> <!-- end panel -->
                    </div> <!-- end col-->
                </div>
                <!-- end row -->

            </div>
        </div>
    </div>
@endsection

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
                                                <td>
                                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#changeMessage">Modifier</button>
                                                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteMessage">Supprimer</button>
                                                </td>
                                            </tr>

                                            <div class="modal fade" id="changeMessage" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Modifier le message</h5>
                                                        </div>
                                                        <form method="post" action="/animation/msg-anim/changeMessage">
                                                            <div class="modal-body">
                                                                {{ csrf_field() }}
                                                                <label for="newMessage">Nouveau message</label>
                                                                <input type="text" class="form-control" name="newMessage" value="{{ $value }}" />
                                                                <input type="hidden" name="newMessage_key" value="{{ $key }}" />
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Fermer</button>
                                                                <input type="submit" class="btn btn-primary" value="Modifier" />
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="modal fade" id="deleteMessage" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Supprimer le message</h5>
                                                        </div>
                                                        <form method="POST" action="/animation/msg-anim/deleteMessage">
                                                            <div class="modal-body">
                                                                Êtes-vous sûr ?
                                                                {{ csrf_field() }}
                                                                <input type="hidden" name="deleteMessage_ID" value="{{ $key }}" />
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Fermer</button>
                                                                <input type="submit" class="btn btn-danger" value="Supprimer">
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
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
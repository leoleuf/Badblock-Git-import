@extends('layouts.app')

@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection

@section('content')
    <div class="content-page">
            <!-- Start content -->
        <div class="content">
            <div class="container">

                <h1>Gestion des todo-list</h1>
                <br />
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal_create">Créer une todolist</button>

                <div class="modal fade" id="modal_create" tabindex="-1" role="dialog" aria-labelledby="Modal_create" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Modifier la todolist</h5>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="todo_title">Titre :</label>
                                        <input type="text" class="form-control" id="todo_create_title" placeholder="Titre">
                                    </div>
                                    <div class="form-group">
                                        <label for="usernamesList">Qui est concerné :</label>
                                        <select class="form-control" multiple="multiple" id="todo_create_usernameList">
                                            @foreach($Users as $user)
                                                <option value="{{ $user }}">{{ $user }}</option>
                                            @endforeach
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="todo_content">Contenu :</label>
                                        <textarea class="form-control" id="todo_create_content" rows="3"></textarea>
                                    </div>
                                    <div class="form-group">
                                        <label for="todo_deadline">Deadline (DD/MM/YYYY) :</label>
                                        <input type="date" class="form-control" id="todo_create_deadline" />
                                    </div>
                                    <div class="form-group">
                                        <label for="todo_priority">Priorité (entre 1 et 100) : </label><br />
                                        <input type="number" class="number" id="todo_create_priority" min="0" max="100" value="1" />
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="resetCreateValues();">Annuler</button>
                                <button type="button" class="btn btn-primary" id="todo_button_valid" data-dismiss="modal" onclick="updateTodo('-1')">Créer la todolist</button>
                            </div>
                        </div>
                    </div>
                </div>

                <br />
                <br />

                <div class="card-columns">
                    @foreach($Todolists as $i => $row)
                        <div class="card" style="width: 18rem;">
                            <div class="card-body">
                                <h3 class="card-title" id="card_{{ $i }}_title">{{ $row->title }}</h3>
                                <p class="card-text" id="card_{{ $i }}_content_display">{{ substr($row->content, 0, $MaxCharactersLength) }}
                                    @if($row->content != substr($row->content, 0, $MaxCharactersLength))
                                        ...
                                    @endif
                                </p>
                                <div style="display: none" id="card_{{ $i }}_started_at">{{ $row->started_at }}</div>
                                <h5 class="card-text" id="card_{{ $i }}_priority">Deadline : {{ $row->deadline }}</h5>
                                <h5 class="card-text" id="card_{{ $i }}_priority">Priorité : {{ $row->priority }}</h5>

                                <button class="btn btn-info"  data-toggle="modal" data-target="#modal_{{ $i }}">Modifier</button>
                                <button class="btn btn-danger" data-toggle="modal" data-target="#modal_{{ $i }}_deleteConfirm">Supprimer</button>
                            </div>
                            <div class="card-footer">
                                <small class="text-muted" id="card_{{ $i }}_author">Créateur : {{ $row->author }}</small><br />
                                <small class="text-muted" id="card_{{ $i }}_author">Personnes concernées :
                                @foreach($row->receivers as $j => $receiver)
                                        {{ $receiver }}
                                        @if($j != count($row->receivers)-1)
                                            ,
                                        @endif
                                @endforeach
                                </small>
                            </div>
                        </div>

                        <div class="modal fade" id="modal_{{ $i }}" tabindex="-1" role="dialog" aria-labelledby="Modal_{{ $i }}" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Modifier la todolist</h5>
                                    </div>
                                    <div class="modal-body">
                                        <form>
                                            <div id="todo_{{ $i }}_id" style="display: none;">{{ $row->id }}</div>
                                            <div class="form-group">
                                                <label for="todo_title">Titre :</label>
                                                <input type="text" class="form-control" id="todo_{{ $i }}_title" value="{{ $row->title }}">
                                            </div>
                                            <div class="form-group">
                                                <label for="usernamesList">Qui est concerné :</label>
                                                <select class="form-control" multiple="multiple" id="todo_{{ $i }}_usernameList">
                                                    @foreach($Users as $user)
                                                        @if(in_array($user, $row->receivers))
                                                            <option value="{{ $user }}" selected="selected">{{ $user }}</option>
                                                        @else
                                                            <option value="{{ $user }}">{{ $user }}</option>
                                                        @endif
                                                    @endforeach
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label for="todo_content">Contenu :</label>
                                                <textarea class="form-control" id="todo_{{ $i }}_content" rows="3">{{ $row->content }}</textarea>
                                            </div>
                                            <div class="form-group">
                                                <label for="todo_started_at">Lancé le (DD/MM/YYYY) :</label>
                                                <input type="date" class="form-control" id="todo_{{ $i }}_started_at" value="{{ $row->started_at }}" readonly  />
                                            </div>
                                            <div class="form-group">
                                                <label for="todo_deadline">Deadline (DD/MM/YYYY) :</label>
                                                <input type="date" class="form-control" id="todo_{{ $i }}_deadline" value="{{ $row->deadline }}" />
                                            </div>
                                            <div class="form-group">
                                                <label for="todo_priority">Priorité (entre 1 et 100) : </label><br />
                                                <input type="number" class="number" id="todo_{{ $i }}_priority" min="0" max="100" value="{{ $row->priority }}" />
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                                        <button type="button" class="btn btn-primary" id="todo_button_valid" onclick="updateTodo('{{ $i }}')">Modifier la todolist</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade" id="modal_{{ $i }}_deleteConfirm" tabindex="-1" role="dialog" aria-labelledby="Modal_{{ $i }}_deleteConfirm" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Supprimer la todolist</h5>
                                    </div>
                                    <div class="modal-body">
                                        <h3>Êtes-vous sûr ?</h3>
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal" id="todo_button_delete" onclick="deleteTodo('{{ $row->id }}')">Supprimer la todolist</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                @endforeach
                </div>

            </div>
        </div>
    </div>
@endsection
@section("after_scripts")

    <script src="/assets/plugins/toastr/toastr.min.js"></script>

    <script>

        /**
        id INT 255 PRIMARY AUTO_INCREMENT
         author VARCHAR 255
         title VARCHAR 255
         content LONGTEXT
         started_at DATE
         deadline DATE
         priority INT 255
         receivers LONGTEXT
         **/

        function resetCreateValues(){
            $("#todo_create_title").val("");
            $("#todo_create_content").val("");
            $("#todo_create_deadline").val("");
            $("#todo_create_priority").val("1");
            $("#todo_create_usernameList").val("");
        }

        function updateTodo(id) {

            var title, content, deadline, priority, receivers, comesFrom, todoID;

            if(id == -1) {
                title = $("#todo_create_title").val();
                content = $("#todo_create_content").val();
                deadline = $("#todo_create_deadline").val();
                priority = $("#todo_create_priority").val();
                receivers = $("#todo_create_usernameList").val();
                comesFrom = "create";
            }
            else {
                title = $("#todo_"+id+"_title").val();
                content = $("#todo_"+id+"_content").val();
                deadline = $("#todo_"+id+"_deadline").val();
                priority = $("#todo_"+id+"_priority").val();
                receivers = $("#todo_"+id+"_usernameList").val();
                todoID = $('#todo_'+id+"_id").html();
                comesFrom = "modify";
            }

            if(title != "" && content != "" && deadline != "" && priority != "" && receivers != "" && receivers != "undefined") {

                $.ajax({
                    type: "POST",

                    url: "/section/todo-management",

                    data: {
                        'title': title,
                        'content': content,
                        'deadline': deadline,
                        'priority': priority,
                        'receivers': receivers,
                        'todoID': todoID,
                        'comesFrom': comesFrom
                    },

                    success: function () {
                        if(comesFrom == "create") {
                            toastr.success('Vous avez bien créer la todolist', "Succès !");
                        }
                        else if(comesFrom == "modify"){
                            toastr.success('Vous avez bien modifié la todolist', "Succès !");
                        }
                    },

                    error: function (jqxhr, status, exception) {
                        toastr.error("Erreur lors de l'envoi, merci de contacter un administrateur. Intitulé de l'erreur : " + exception, 'Erreur');
                    }
                });
            }

            else {
                toastr.error("Merci de remplir tout le formulaire", "Erreur");
            }
        }

        function deleteTodo(todoID){
            $.ajax({
                type: 'POST',

                url: "/section/todo-management",

                data: {
                    'todoID': todoID,
                    'comesFrom': "delete"
                },

                success: function(){
                    toastr.success('Vous avez bien supprimé la todolist', "Succès !");
                },

                error: function (jqxhr, status, exception) {
                    toastr.error("Erreur lors de la suppression, merci de contacter un administrateur. Intitulé de l'erreur : " + exception, 'Erreur');
                }
            })
        }
    </script>

@endsection
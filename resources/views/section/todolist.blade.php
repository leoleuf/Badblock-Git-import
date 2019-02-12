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
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal" data-comesfrom="create" onclick="displayModal('-1')">Ajouter une todolist</button>

                <!--
                id INT 255 PRIMARY AUTO_INCREMENT
                author VARCHAR 255
                title VARCHAR 255
                content LONGTEXT
                started_at DATE
                deadline DATE
                priority INT 255
                receivers LONGTEXT
                */ -->

                <div id="todo_usernameList_default" style="display: none">

                    <!--<select class="form-control" multiple="multiple" id="usernameList_default">
                        @foreach($Users as $user)
                            <option value="{{ $user }}">{{ $user }}</option>
                        @endforeach
                    </select>-->



                </div>

                <!-- Modal -->
                <div class="modal" id="modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" style="display: none">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Créer une nouvelle todolist</h5>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="todo_title">Titre :</label>
                                        <input type="text" class="form-control" id="todo_title" placeholder="Titre">
                                    </div>
                                    <div class="form-group">
                                        <label for="usernamesList">Qui est concerné :</label>
                                        <div id="todo_usernameList">
                                            <select name="sweets" id="fraulein" onchange="displayVals()">
                                                <option value="Chocolate">Chocolate</option>
                                                <option value="Candy">Candy</option>
                                                <option value="Taffy">Taffy</option>
                                                <option value="Caramel">Caramel</option>
                                                <option value="Fudge">Fudge</option>
                                                <option value="Cookie">Cookie</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="todo_content">Contenu :</label>
                                        <textarea class="form-control" id="todo_content" rows="3"></textarea>
                                    </div>
                                    <div class="form-group" id="todo_started_at_container" style="display: none">
                                        <label for="todo_started_at">Lancé le (DD/MM/YYYY) :</label>
                                        <input type="date" class="form-control" id="todo_started_at" readonly />
                                    </div>
                                    <div class="form-group">
                                        <label for="todo_deadline">Deadline (DD/MM/YYYY) :</label>
                                        <input type="date" class="form-control" id="todo_deadline" />
                                    </div>
                                    <div class="form-group">
                                        <label for="todo_priority">Priorité (entre 1 et 100) : </label><br />
                                        <input type="number" class="number" id="todo_priority" min="0" max="100" value="1" />
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                                <button type="button" class="btn btn-primary" id="todo_button_valid" onclick="createTodo()">Créer la todolist</button>
                            </div>
                        </div>
                    </div>
                </div>

                <br />
                <br />

                <!--
                id INT 255 PRIMARY AUTO_INCREMENT
                author VARCHAR 255
                title VARCHAR 255
                content LONGTEXT
                started_at DATE
                deadline DATE
                priority INT 255
                receivers LONGTEXT
                */ -->

                <div class="card-columns">
                @for($i = 0; $i < count($Todolists); $i++)
                        <div class="card" style="width: 18rem;">
                            <div class="card-body">
                                <h3 class="card-title" id="card_{{ $i }}_title">{{ $Todolists[$i]->title }}</h3>
                                <p class="card-text" id="card_{{ $i }}_content_display">{{ substr($Todolists[$i]->content, 0, $MaxCharacterLength) }}...</p>
                                <p class="card-text" id="card_{{ $i }}_content" style="display: none;">{{ $Todolists[$i]->content }}</p>
                                <div style="display: none" id="card_{{ $i }}_started_at">{{ $Todolists[$i]->started_at }}</div>
                                <div style="display: none;" id="card_{{ $i }}_usernameList">
                                    <select multiple class="form-control" id="card_usernameList">
                                        @foreach($Users as $user)
                                            @if(in_array($user, $Todolists[$i]->receivers))
                                                <option value="{{ $user }}" selected="selected">{{ $user }}</option>
                                            @else
                                                <option value="{{ $user }}">{{ $user }}</option>
                                            @endif
                                        @endforeach
                                    </select>
                                </div>
                                <h5 class="card-text d-inline" id="card_{{ $i }}_priority_title">Deadline : </h5>
                                <h5 class="card-text d-inline" id="card_{{ $i }}_deadline">{{ $Todolists[$i]->deadline }}</h5>
                                <br />
                                <h5 class="card-text d-inline" id="card_{{ $i }}_priority_title">Priorité : </h5>
                                <h5 class="card-text d-inline" id="card_{{ $i }}_priority">{{ $Todolists[$i]->priority }}</h5>
                                <br /><br />
                                <button class="btn btn-info"  data-toggle="modal" data-target="#modal" data-comesfrom="modify" onclick="displayModal('{{ $i }}');">Modifier</button>
                                <button class="btn btn-danger" onclick="deleteTodo();">Supprimer</button>
                            </div>
                            <div class="card-footer">
                                <small class="text-muted" id="card_{{ $i }}_author">Créateur : {{ $Todolists[$i]->author }}</small><br />
                                <small class="text-muted" id="card_{{ $i }}_author">Personnes concernées :
                                @foreach($Todolists[$i]->receivers as $receiver)
                                    {{ $receiver }},
                                @endforeach
                                </small>
                            </div>
                        </div>
                @endfor
                </div>

            </div>
        </div>
    </div>
@endsection
@section("after_scripts")

    <script src="/assets/plugins/toastr/toastr.min.js"></script>

    <script>

        function displayModal(i) {

            $('#modal').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget); // Button that triggered the modal
                var modal = $(this);
                modal.css("display", "block");

                if (button.data("comesfrom") == "modify") {
                    $("#todo_started_at_container").css('display', 'block');

                    modal.find("#todo_button_valid").attr("onclick", "changeTodo()");
                    modal.find(".modal-title").html("Modifier la todolist");

                    modal.find('#todo_title').val($("#card_" + i + "_title").html());
                    modal.find("#todo_content").val($("#card_" + i + "_content").html());
                    modal.find("#todo_deadline").val($("#card_"+i+"_deadline").html());
                    modal.find("#todo_started_at").val($("#card_"+i+"_started_at").html());
                    modal.find("#todo_priority").val($("#card_"+i+"_priority").html());
                    modal.find("#todo_usernameList").html($("#card_"+i+"_usernameList").html());

                    modal.find("#todo_button_valid").html("Modifier la todolist");
                }

                else if (button.data("comesfrom") == "create") {
                    $("#todo_started_at_container").css('display', 'none');
                    modal.find("#todo_button_valid").attr("onclick", "createTodo()");
                    modal.find(".modal-title").html("Créer une nouvelle todolist");
                    modal.find("#todo_button_valid").html("Créer une nouvelle todolist");

                    modal.find("#todo_usernameList").html($("#todo_usernameList_default").html());
                    modal.find('#todo_title').val("");
                    modal.find("#todo_content").val("");
                }

            });
        }

        function displayVals() {
            var single = $("#fraulein").val();
            //var multipleValues = $( "#test" ).val() || [];
            // When using jQuery 3:
            // var multipleValues = $( "#multiple" ).val();
            //console.log(multipleValues.join( ", " ));
            console.log(single);
        }

        $( "#fraulein" ).change( displayVals );
        displayVals();

        function createTodo() {

            var title = $("#todo_title").val();
            var content = $("#todo_content").val();
            var deadline = $("#todo_deadline").val();
            var priority = $("#todo_priority").val();

            console.log(title);
            console.log(content);
            console.log(deadline);
            console.log(priority);

            if(title != "" && content != "" && deadline != "" && priority != "" && receivers != "" && receivers != "undefined") {

                $.ajax({
                    type: "POST",

                    url: "/section/todolist-management",

                    data: {
                        'title': title,
                        'content': content,
                        'deadline': deadline,
                        'priority': priority,
                        'receivers': receivers,
                        'comesFrom': "create"
                    },

                    success: function () {
                        toastr.success('Vous avez bien créer la todolist', "Succès !");
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

        function changeTodo() {
            console.log("ChangeTodo");
        }

        function deleteTodo() {

        }
    </script>

@endsection
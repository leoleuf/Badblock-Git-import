@extends("layouts.app")

@section('content')

    <div class="row">
        <div class="col-12">
            <div class="card-box">
                <h4 class="m-t-0 header-title">Vos todolists :</h4>
                <div class="row">
                    <div class="col-12">
                        <div class="p-20">
                            <div class="card-columns">
                                @foreach($Todolists as $i => $row)
                                    @if(in_array($UserID, $row->receivers))
                                    <div class="card" style="width: 18rem;">
                                        <div class="card-body">
                                            <h3 class="card-title" id="card_{{ $i }}_title">{{ $row->title }}</h3>
                                            <p class="card-text" id="card_{{ $i }}_content_display">{{ substr($row->content, 0, $MaxCharactersLength) }}

                                                @if($row->content != substr($row->content, 0, $MaxCharactersLength))
                                                    ...
                                                @endif
                                            </p>
                                            <div style="display: none" id="card_{{ $i }}_started_at">{{ $row->started_at }}</div>
                                            <h5 class="card-text d-inline" id="card_{{ $i }}_deadline">Deadline <h6 class="d-inline">(YYYY/MM/DD)</h6> : {{ $row->deadline }}</h5>
                                            <h5 class="card-text" id="card_{{ $i }}_priority">Priorité : {{ $row->priority }}</h5>

                                            <button class="btn btn-primary" data-toggle="modal" data-target="#modal_{{ $i }}">Voir la todolist</button>
                                        </div>
                                        <div class="card-footer">
                                            <small class="text-muted" id="card_{{ $i }}_author">Créateur : {{ $row->author }}</small><br />
                                            <small class="text-muted" id="card_{{ $i }}_author">Personnes concernées :
                                                @foreach($row->receivers as $j => $receiver)
                                                    {{ $receiver }}
                                                    @if($j != (count($row->receivers))-1)
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
                                                                <input type="text" class="form-control" id="todo_{{ $i }}_title" value="{{ $row->title }}" readonly>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="usernamesList">Qui est concerné :</label>
                                                                <h5 class="card-text">
                                                                    @foreach($row->receivers as $i => $receiver)
                                                                    {{ $receiver }}
                                                                        @if($i != count($row->receivers)-1)
                                                                            ,
                                                                        @endif
                                                                    @endforeach
                                                                </h5>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="todo_content">Contenu :</label>
                                                                <textarea class="form-control" id="todo_{{ $i }}_content" rows="3" readonly>{{ $row->content }}</textarea>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="todo_started_at">Lancé le (DD/MM/YYYY) :</label>
                                                                <input type="date" class="form-control" id="todo_{{ $i }}_started_at" value="{{ $row->started_at }}" readonly  />
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="todo_deadline">Deadline (DD/MM/YYYY) :</label>
                                                                <input type="date" class="form-control" id="todo_{{ $i }}_deadline" value="{{ $row->deadline }}" readonly />
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="todo_priority">Priorité (entre 1 et 100) : </label><br />
                                                                <input type="number" class="number" id="todo_{{ $i }}_priority" min="0" max="100" value="{{ $row->priority }}" readonly />
                                                            </div>
                                                        </form>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    @endif
                                @endforeach
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

@endsection

@section('after_script')

@endsection
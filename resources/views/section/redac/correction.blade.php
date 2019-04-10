@extends('layouts.app')
@section('header')

    <!-- Include Editor style. -->
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/css/froala_editor.pkgd.min.css" rel="stylesheet" type="text/css" />
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/css/froala_style.min.css" rel="stylesheet" type="text/css" />

@endsection
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h3>Liste des textes</h3>
                <div class="row">
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Titre</th>
                                        <th>Demandé par</th>
                                        <th>Etat</th>
                                        <th>Date</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @foreach($data as $key => $value)
                                        <tr>
                                            <th scope="row">{{ $key + 1 }}</th>
                                            <td>{{ $value->title }}</td>
                                            <td>{{ $value->send_by }}</td>
                                            <td>@if($value->is_correct)Corrigé par {{ $value->correct_by }}@else Non corrigé @endif</td>
                                            <th>{{ App\Http\Controllers\DateController::formatDate($value->date_post) }}</th>
                                            <td>@if($value->is_correct) <a href="/section/correction/view/{{ $value->id }}" class="btn btn-info" style="margin-right: 10px">Consulter</a> @else @can('redac_correct_text')<a href="/section/correction-text/{{ $value->id }}" class="btn btn-success" style="margin-right: 10px">Corriger</a>@endcan @endif <a href="/section/suppr-text/{{ $value->id }}" class="btn btn-danger">Supprimer</a></td>
                                        </tr>
                                    @endforeach
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                <form method="post" action="/section/add-text">
                                    {!! csrf_field() !!}
                                    <div class="fr-view">
                                        <div class="form-group">
                                            <input type="text" name="title" class="form-control" placeholder="Ex: Mon Article">
                                        </div>
                                        <textarea class="form-control" name="text" rows="10" cols="50">
                                        </textarea>
                                        <br>
                                        <button type="submit" class="btn btn-success">Valider</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')

    <script type="text/javascript"
            src="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/js/froala_editor.pkgd.min.js"></script>

    <script type="text/javascript">
        $(function () {
            $('textarea').froalaEditor({
                heightMin: 300,
                heightMax: 500
            });
        });
    </script>

@endsection
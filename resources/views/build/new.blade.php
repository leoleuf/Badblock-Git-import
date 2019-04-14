@extends('layouts.app')
@section('header')

    <!-- Include Editor style. -->
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/css/froala_editor.pkgd.min.css" rel="stylesheet"
          type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/css/froala_style.min.css" rel="stylesheet"
          type="text/css"/>

@endsection
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="card-box">
                <form method="post" action="/build/project/new">
                    {!! csrf_field() !!}
                    <div class="row">
                        <div class="col-12 col-md-4">
                            <div class="form-group">
                                <label>Intitulé du projet :</label>
                                <input class="form-control" name="name" placeholder="Ex: Hub" required>
                            </div>
                        </div>
                        <div class="col-12 col-md-4">
                            <div class="form-group">
                                <label>Début :</label>
                                <input class="form-control" type="date" name="dateStart" required>
                            </div>
                        </div>
                        <div class="col-12 col-md-4">
                            <div class="form-group">
                                <label>Fin :</label>
                                <input class="form-control" type="date" name="dateEnd" required>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12 col-md-6">
                            <label>Description du projet :</label>
                            <textarea class="form-control" name="text" rows="10" cols="50" required></textarea>
                        </div>
                        <div class="col-md-6">
                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Participant(s) :</label>
                                        <select multiple="" class="form-control" name="team[]" required>
                                            @foreach($builders as $builder)
                                                <option>{{ $builder }}</option>
                                            @endforeach
                                        </select>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Catégorie :</label>
                                        <select class="form-control" name="category" required>
                                            <option>Build</option>
                                            <option>Terraforming</option>
                                            <option>Organic</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Gain (PB) :</label>
                                        <input class="form-control" type="number" name="price" min="0" placeholder="Ex : 950" required>
                                        <small>Les PB indiqué ci-dessus sont versés à <strong>chaque membres</strong> du projet.</small>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Priorité :</label>
                                        <input class="form-control" type="number" name="priority" min="1" placeholder="Ex : 10" required>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Dépendance :</label>
                                        <select class="form-control" name="dependancy" required>
                                            @foreach($dependancy as $dep)
                                                <option>Projet : {{ $dep->name }}</option>
                                            @endforeach
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <br>
                    <button type="submit" class="btn btn-success">Créer</button>
                </form>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')

    <script type='text/javascript'
            src='https://cdn.jsdelivr.net/npm/froala-editor@2.9.4/js/froala_editor.min.js'></script>

    <script type="text/javascript">
        $(function () {
            $('textarea').froalaEditor({
                heightMin: 300,
                heightMax: 500
            });
        });
    </script>


@endsection
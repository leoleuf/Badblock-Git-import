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
                <h3>Correction : {{ $data->title }}</h3>
                <div class="row">
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                <form method="post" action="/section/validation-text">
                                    {!! csrf_field() !!}
                                    <input type="hidden" name="id" value="{{ $data->id }}">
                                    <div class="fr-view">
                                        <textarea class="form-control" name="text" rows="10" cols="50">
                                            {{ $data->text }}
                                        </textarea>
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
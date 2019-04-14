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
@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h3>Give animation</h3>
                <div class="row">
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                <h4 class="m-t-0 header-title">Ajouter des points boutique.</h4>
                                <p class="text-muted font-14 m-b-20">
                                    Attention à ne pas entrer de valeur négative.
                                </p>
                                <form class="form_inline" method="post" action="/animation/pb">
                                    {{ csrf_field() }}
                                    <div id="formGive">
                                        <div class="row">
                                            <div class="col-12 col-md-4">
                                                <div class="form-group">
                                                    <input type="text" name="pseudo[]" class="form-control input-block"
                                                           placeholder="Hooki le magnifique">
                                                </div>
                                            </div>
                                            <div class="col-12 col-md-4">
                                                <div class="form-group">
                                                    <input type="text" name="points[]" class="form-control input-block"
                                                           placeholder="Ex: 150500">
                                                </div>
                                            </div>
                                            <div class="col-12 col-md-4">
                                                <div class="form-group">
                                                    <input type="text" name="comments[]" class="form-control input-block"
                                                           placeholder="Commentaire">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <button type="button" class="btn btn-danger" id="addLine">Ajouter une ligne</button>
                                    <button type="submit" class="btn btn-success"><span
                                                class="glyphicon glyphicon-saved"></span> Valider
                                    </button>
                                    <form>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end row -->

            </div>
        </div>
    </div>
@endsection
@section("after_scripts")

    <!-- Editable js -->
    <script src="/assets/plugins/magnific-popup/dist/jquery.magnific-popup.min.js"></script>
    <script src="/assets/plugins/jquery-datatables-editable/jquery.dataTables.js"></script>
    <script src="/assets/plugins/datatables/dataTables.bootstrap.js"></script>
    <script src="/assets/plugins/tiny-editable/mindmup-editabletable.js"></script>
    <script src="/assets/plugins/tiny-editable/numeric-input-example.js"></script>
    <!-- init -->
    <script src="/assets/pages/datatables.editable.init.js"></script>

    <script>
        $('#addLine').click(function () {
            $('#formGive').append('<div class="row">\n' +
                '                                            <div class="col-12 col-md-4">\n' +
                '                                                <div class="form-group">\n' +
                '                                                    <input type="text" name="pseudo[]" class="form-control input-block"\n' +
                '                                                           placeholder="Pseudo">\n' +
                '                                                </div>\n' +
                '                                            </div>\n' +
                '                                            <div class="col-12 col-md-4">\n' +
                '                                                <div class="form-group">\n' +
                '                                                    <input type="text" name="points[]" class="form-control input-block"\n' +
                '                                                           placeholder="Ex: 500">\n' +
                '                                                </div>\n' +
                '                                            </div>\n' +
                '                                            <div class="col-12 col-md-4">\n' +
                '                                                <div class="form-group">\n' +
                '                                                    <input type="text" name="comments[]" class="form-control input-block"\n' +
                '                                                           placeholder="Commentaire">\n' +
                '                                                </div>\n' +
                '                                            </div>\n' +
                '                                        </div>')
        });
    </script>


@endsection
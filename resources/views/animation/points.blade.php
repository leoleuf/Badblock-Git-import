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
                                {{ Form::open(array('url' => "/animation/pb",'class'=>'form_inline')) }}
                                <div class="form-group">
                                    <input type="text" name="pseudo[]" class="form-control input-block" placeholder="Pseudo">
                                </div>
                                <div class="form-group">
                                    <input type="text" name="points[]" class="form-control input-block" placeholder="Ex: 500">
                                </div>
                                <div class="form-group">
                                    <input type="text" name="comments" class="form-control input-block" placeholder="Commentaire">
                                </div>
                                <!--<button type="button" class="btn btn-danger" onclick="add();"><span class="fa fa-plus"></span> Add line</button>-->
                                <button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-saved"></span> Valider</button>
                                {{ Form::close() }}
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

    <!--<script>
        function add() {
            $('#tablee').append('<tr>\n' +
                '                                                    <td>\n' +
                '                                                        <input type="text" name="pseudo[]" class="form-control input-block" value="">                                                </td>\n' +
                '                                                    <td>\n' +
                '                                                        <input type="text" name="points[]" class="form-control input-block" value="">                                                </td>\n' +
                '                                                    </td>\n' +
                '                                                </tr>')
        }
    </script>-->


@endsection
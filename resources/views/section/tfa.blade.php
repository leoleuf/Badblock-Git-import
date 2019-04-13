@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h1>Vérification des TFA</h1>
                <div class="row">
                    <div class="col-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="card-box">
                                    <table class="table table-striped" id="datatable-editable">
                                        <thead>
                                        <tr>
                                            <th>Pseudo</th>
                                            <th>TFA Active</th>
                                            <th>Bypass</th>
                                            <th>Action</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        @foreach($user as $data)
                                            <tr class="gradeX">
                                                <td>{{ App\Http\Controllers\section\TfaController::getUserById($data->id) }}</td>
                                                <td>
                                                    @if(App\Http\Controllers\section\TfaController::checkActiveTFA($data->id))
                                                        Non
                                                    @else
                                                        Oui
                                                    @endif
                                                </td>
                                                <td>
                                                    @if($data->TFAbypass)
                                                        Oui
                                                    @else
                                                        Non
                                                    @endif
                                                </td>
                                                <td>
                                                    <button class="btn btn-icon waves-effect waves-light btn-danger m-b-5">
                                                        <i class="fa fa-legal"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        @endforeach
                                        </tbody>
                                    </table>
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
@section("after_scripts")


@endsection
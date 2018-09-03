@extends('layouts.app')
@section('content')
    <br>
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
    <div class="row">
        <div class="col-sm-12">
            <!-- will be used to show any messages -->
            @if (Session::has('message'))
                <div class="alert alert-info">{{ Session::get('message') }}</div>
            @endif
            <div class="panel">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="m-b-30">
                                <h1>Paies des sections :</h1>
                                <a href="/website" class="btn btn-primary waves-effect waves-light">Retour <i class="fa fa-long-arrow-left"></i></a>
                            </div>
                        </div>
                    </div>

                    <div class="">
                        <table class="table table-striped" id="datatable-editable">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Section</th>
                                <th>Points Boutique</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            @foreach($data as $key => $value)
                                <tr class="gradeX">
                                    <td>{{ $value['date'] }}</td>
                                    <td>{{ $value['section'] }}</td>
                                    <td>{{ $value['total'] }}</td>
                                    <td>
                                        <a href="/website/section/{{ $value['_id'] }}" class="btn btn-success">
                                            <span class="fa fa-eye"></span>
                                            Voir
                                        </a>
                                    </td>
                                </tr>
                            @endforeach
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
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

@endsection

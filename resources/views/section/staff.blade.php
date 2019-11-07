@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h1>Liste du staff gradé sur le serveur :</h1>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="card-box">
                                    <table class="table table-striped" id="datatable">
                                        <thead>
                                        <tr>
                                            <th>Pseudo</th>
                                            <th>Grade</th>
                                            <th>Mode premium</th>
                                            <th>TFA (IG)</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        @foreach($user as $row)
                                            <tr class="gradeX">
                                                <td>{{ ucfirst($row['name']) }}</td>
                                                <td>
                                                    <ul>
                                                        @foreach($row['permissions']['groups'] as $key => $data)
                                                            @if($key == "bungee")
                                                                @foreach($data as $p => $h)
                                                                    @if($p != "vip" && $p != "vip+" && $p != "mvp" && $p != "mvp+" && $p != "gradeperso" && $p != "default")
                                                                        {{ ucfirst($p) }}
                                                                    @endif
                                                                @endforeach
                                                            @endif
                                                        @endforeach
                                                    </ul>
                                                </td>
                                                <td>
                                                    @if($row['onlineMode'] == true)
                                                        <i class="fa fa-check"></i>
                                                    @else
                                                        <i class="fa fa-ban"></i>
                                                    @endif
                                                </td>
                                                <td>
                                                    @if(!isset($row['authKey']) || empty($row['authKey']))
                                                        <i class="fa fa-ban"></i>
                                                    @else
                                                        <i class="fa fa-check"></i>
                                                    @endif
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
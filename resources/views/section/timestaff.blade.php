@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h1>Temps de Connexion</h1>
                <div class="row">
                    @foreach($user as $row)
                        <div class="col-12 col-md-4">
                            <div class="panel">
                                <div class="panel-body">
                                    <div class="card-box text-center">
                                        <h3>{{ ucfirst($row['name']) }}</h3>
                                        <p><strong>{{ $row['grade'] }}</strong></p>
                                        <div class="widget-chart-box-1" style="margin-bottom: 15px;">
                                            <input data-plugin="knob" data-width="80" data-height="80"
                                                   data-fgColor="{{ $row['color'] }}"
                                                   data-bgColor="#FFE6BA" value="{{ $row['workFine'] }}"
                                                   data-skin="tron" data-angleOffset="180" data-readOnly=true
                                                   data-thickness=".15"/>
                                        </div>
                                        <span><em>{{ round(($row['time'] / 60 / 60), 1) }} / {{ $row['ntime'] / 2 }} Heures</em></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    @endforeach
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
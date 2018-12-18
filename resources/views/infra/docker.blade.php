@extends('layouts.app')

@section('header')

@endsection

@section('content')
    @foreach($Clusters as $Cluster)
        <a href="/" class="btn btn-icon btn-warning waves-light"  data-toggle="modal" data-target="#openInstance"> <i class="fas fa-plus-square"></i> New Instance</a>
        <a href="/" class="btn btn-icon btn-danger waves-light" data-toggle="modal" data-target="#closeInstance"> <i class="fas fa-minus-square"></i> Stop Instance</a>
        <div class="pull-right">
            <a href="/" class="btn btn-icon btn-info waves-light" onclick="viewcluster();"> <i class="fas fa-network-wired"></i> Vue Cluster</a>
            <a href="/" class="btn btn-icon btn-info waves-light" onclick="viewinstance();"> <i class="fas fa-server"></i> Vue Instances</a>
        </div>
        <br>
        <br>
        <div id="cluster">
            @include('infra.cluster')
        </div>
        <div id="instances">
            @include('infra.instances')
        </div>
    @endforeach
@endsection
@section('after_scripts')
    <script>
        function viewcluster() {
            $('#instances').hide();
            $('#cluster').show();
        }
        function viewinstance() {
            $('#cluster').hide();
            $('#instances').show();
        }
    </script>


    <div id="openInstance" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title mt-0" id="myLargeModalLabel">Ouverture d'une nouvelle Instance :</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group row">
                            <label class="col-2 col-form-label">Cluster</label>
                            <div class="col-10">
                                <select class="form-control" name="cluster">
                                    @foreach($Clusters as $Cluster)
                                        <option value="{{ $Cluster['name'] }}">{{ $Cluster['name'] }}</option>
                                    @endforeach
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-2 col-form-label">World System Name</label>
                            <div class="col-10">
                                <select class="form-control">
                                    <option>tower2v2</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-2 col-form-label">Owner</label>
                            <div class="col-10">
                                <input type="test" id="owner" name="owner" class="form-control" placeholder="Fluor" value="{{Auth::user()->name}}">
                            </div>
                        </div>
                        <center>
                            <a class="btn btn-icon btn-success"> <i class="fas fa-check-square"></i> Valider</a>
                        </center>

                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="closeInstance" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title mt-0" id="myLargeModalLabel">Stoper Instance :</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </div>
                <div class="modal-body">

                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

@endsection

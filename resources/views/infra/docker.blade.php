@extends('layouts.app')

@section('header')

@endsection

@section('content')
    @foreach($Clusters as $Cluster)
        <a href="/" class="btn btn-icon btn-warning waves-light"  data-toggle="modal" data-target="#openInstance"> <i class="fas fa-plus-square"></i> New Instance</a>
        <a href="/" class="btn btn-icon btn-danger"> <i class="fas fa-minus-square"></i> Stop Instance</a>
        <br>
        <br>
        <div class="col-xl-3 col-md-6">
            <div class="card-box">
                <h4 class="header-title mt-0 m-b-30">Cluster : {{ $Cluster['name'] }}</h4>

                <p class="font-600 m-b-5">Cpu <span class="text-primary pull-right">{{ round($Cluster['data']->status->currentCpu * 100, 2) }}%</span></p>
                <div class="progress progress-bar-danger-alt progress-sm m-b-20">
                    <div class="progress-bar progress-bar-danger progress-animated wow animated animated" role="progressbar"
                         aria-valuenow="{{ round($Cluster['data']->status->currentCpu * 100, 2) }}" aria-valuemin="0" aria-valuemax="100"
                         style="width: {{ round($Cluster['data']->status->currentCpu * 100, 2) }}%; visibility: visible; animation-name: animationProgress;">
                    </div>
                </div>
                <p class="font-600 m-b-5">Ram <span class="text-primary pull-right">{{ round(($Cluster['data']->status->totalMemory - $Cluster['data']->status->freeMemory) / $Cluster['data']->status->totalMemory * 100, 2) }}%</span></p>
                <div class="progress progress-bar-primary-alt progress-sm m-b-20">
                    <div class="progress-bar progress-bar-primary progress-animated wow animated animated"
                         role="progressbar" aria-valuenow="{{ round(($Cluster['data']->status->totalMemory - $Cluster['data']->status->freeMemory) / $Cluster['data']->status->totalMemory * 100, 2) }}" aria-valuemin="0" aria-valuemax="100"
                         style="width: {{ round(($Cluster['data']->status->totalMemory - $Cluster['data']->status->freeMemory) / $Cluster['data']->status->totalMemory * 100, 2) }}%; visibility: visible; animation-name: animationProgress;">
                    </div>
                </div>
                <center>
                    <a href="/" class="btn btn-icon btn-warning"> <i class="fa fa-wrench"></i> </a>
                </center>
            </div>
        </div>
    @endforeach
@endsection
@section('after_scripts')


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

@endsection

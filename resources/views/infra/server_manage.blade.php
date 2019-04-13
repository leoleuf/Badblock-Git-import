@extends('layouts.app')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="card-box">
                <h4 class="header-title mt-0 m-b-30">Server : Bungee</h4>

                <p class="font-600 m-b-5">Cpu <span class="text-primary pull-right">27.13%</span></p>
                <div class="progress progress-bar-danger-alt progress-sm m-b-20">
                    <div class="progress-bar progress-bar-danger progress-animated wow animated animated" role="progressbar" aria-valuenow="27.13" aria-valuemin="0" aria-valuemax="100" style="width: 27.13%; visibility: visible; animation-name: animationProgress;">
                    </div>
                </div>
                <p class="font-600 m-b-5">Ram <span class="text-primary pull-right">81.12%</span></p>
                <div class="progress progress-bar-primary-alt progress-sm m-b-20">
                    <div class="progress-bar progress-bar-primary progress-animated wow animated animated" role="progressbar" aria-valuenow="81.12" aria-valuemin="0" aria-valuemax="100" style="width: 81.12%; visibility: visible; animation-name: animationProgress;">
                    </div>
                </div>
                <center>
                    <a href="/" class="btn btn-icon btn-warning"> <i class="fa fa-wrench"></i> </a>
                </center>
            </div>
        </div
    </div>
@endsection
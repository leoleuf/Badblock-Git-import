@extends('layouts.app')

@section('header')

@endsection

@section('content')
    <div class="col-xl-3 col-md-6">
        <div class="card-box">
            <h4 class="header-title mt-0 m-b-30">Cluster : XXXXXXXXX</h4>
            <div class="widget-chart-1">
                <div class="widget-chart-box-1">
                    <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#ffbd4a"
                           data-bgColor="#FFE6BA" value="80"
                           data-skin="tron" data-angleOffset="180" data-readOnly=true
                           data-thickness=".15"/>
                </div>
                <div class="widget-detail-1">
                    <h2 class="p-t-10 mb-0"> 4569 </h2>
                    <p class="text-muted m-b-10">Revenue today</p>
                </div>
            </div>


        </div>
    </div>
@endsection
@section('after_scripts')

@endsection

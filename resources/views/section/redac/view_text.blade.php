@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h3>Text : {{ $data->title }}</h3>
                <div class="row">
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                {!! nl2br($data->text) !!}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
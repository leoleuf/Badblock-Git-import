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
                                    <h1>Edition de {{ $user['realName'] }}</h1>

                                    <div class="row">
                                        <div class="col">Ban : </div>
                                        <div class="col">col</div>
                                        <div class="w-100"></div>
                                        <div class="col">col</div>
                                        <div class="col">col</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


@endsection

@extends('layouts.app')
@section('content')
tps / ram / processeur / nb de joueurs
<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
        <div class="col-lg-4 col-md-6">
            <div class="card-box">
                <div class="dropdown pull-right">
                    <a href="#" class="dropdown-toggle card-drop" data-toggle="dropdown" aria-expanded="false">
                        <i class="zmdi zmdi-more-vert"></i>
                    </a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div>

                <h4 class="header-title m-t-0 m-b-30">Instance Ouvertes</h4>

                <div class="widget-chart-1">
                    <div class="widget-chart-box-1">
                        <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#f05050 "
                               data-bgColor="#F9B9B9" value="58"
                               data-skin="tron" data-angleOffset="180" data-readOnly=true
                               data-thickness=".15"/>
                    </div>

                    <div class="widget-detail-1">
                        <h2 class="p-t-10 m-b-0"> 0/4</h2>
                        <p class="text-muted">Instance opened</p>
                    </div>
                </div>
            </div>
        </div><!-- end col -->

        <div class="col-lg-4 col-md-6">
            <div class="card-box">
                <div class="dropdown pull-right">
                    <a href="#" class="dropdown-toggle card-drop" data-toggle="dropdown" aria-expanded="false">
                        <i class="zmdi zmdi-more-vert"></i>
                    </a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </div>

                <h4 class="header-title m-t-0 m-b-30">RAM</h4>

                <div class="widget-box-2">
                    <div class="widget-detail-2">
                        <span id="playerC" class="badge badge-success pull-left m-t-20">0</span>
                        <h2 id="playersT" class="m-b-0">0</h2>
                        <p class="text-muted m-b-25">Players Online</p>
                    </div>
                    <div class="progress progress-bar-success-alt progress-sm m-b-0">
                        <div id="playerbar" class="progress-bar progress-bar-success" role="progressbar"
                             aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                             style="width: 100%;">
                            <span class="sr-only">77% Complete</span>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- end col -->
                <div class="col-lg-4 col-md-6">
                    <div class="card-box">
                        <h4 class="header-title m-t-0 m-b-30">Nouvelle Instance</h4>

                        <div class="widget-box-2">
                            <div class="widget-detail-2 center">
                                <center><button type="button" class="btn btn-success waves-effect w-md waves-light m-b-5">Success</button></center>
                            </div>
                            <div class="progress progress-bar-success-alt progress-sm m-b-0">
                                <div id="playerbar" class="progress-bar progress-bar-success" role="progressbar"
                                     aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                     style="width: 100%;">
                                    <span class="sr-only">77% Complete</span>
                                </div>
                            </div>
                            <br>
                        </div>
                    </div>
                </div><!-- end col -->
        </div><!-- end col -->
                <div class="row">
                    <div class="col-lg-3">
                        <div class="card-box">
                            <div class="dropdown pull-right">
                                <a href="#" class="dropdown-toggle card-drop" data-toggle="dropdown" aria-expanded="false">
                                    <i class="zmdi zmdi-more-vert"></i>
                                </a>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div>

                            <h4 class="header-title m-t-0 m-b-30">Players Onlines</h4>

                            <div id="container" style="height: 320px;"></div>

                        </div>
                    </div><!-- end col-->

                    <div class="col-lg-3">
                        <div class="card-box">
                            <div class="dropdown pull-right">
                                <a href="#" class="dropdown-toggle card-drop" data-toggle="dropdown" aria-expanded="false">
                                    <i class="zmdi zmdi-more-vert"></i>
                                </a>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div>

                            <h4 class="header-title m-t-0 m-b-30">Realtime Statistics</h4>

                            <div id="contddainer" style="height: 320px;"></div>

                        </div>
                    </div><!-- end col-->
                    <div class="col-lg-3">
                        <div class="card-box">
                            <div class="dropdown pull-right">
                                <a href="#" class="dropdown-toggle card-drop" data-toggle="dropdown" aria-expanded="false">
                                    <i class="zmdi zmdi-more-vert"></i>
                                </a>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div>

                            <h4 class="header-title m-t-0 m-b-30">Realtime Statistics</h4>

                            <div id="contddainer" style="height: 320px;"></div>

                        </div>
                    </div><!-- end col-->
                    <div class="col-lg-3">
                        <div class="card-box">
                            <div class="dropdown pull-right">
                                <a href="#" class="dropdown-toggle card-drop" data-toggle="dropdown" aria-expanded="false">
                                    <i class="zmdi zmdi-more-vert"></i>
                                </a>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div>

                            <h4 class="header-title m-t-0 m-b-30">Realtime Statistics</h4>

                            <div id="contddainer" style="height: 320px;"></div>

                        </div>
                    </div><!-- end col-->
                </div>


            </div>
        </div>
        </div>
        <!-- end row -->

@endsection
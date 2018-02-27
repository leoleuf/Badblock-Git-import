@extends('layouts.app')
@section('content')
    <!-- ============================================================== -->
    <!-- Start right Content here -->
    <!-- ============================================================== -->
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">

                <div class="row">

                    <div class="col-lg-3 col-md-6">
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
                                    <h2 class="p-t-10 m-b-0"> 0</h2>
                                    <p class="text-muted">Instance opened</p>
                                </div>
                            </div>
                        </div>
                    </div><!-- end col -->

                    <div class="col-lg-3 col-md-6">
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

                            <h4 class="header-title m-t-0 m-b-30">Players Online</h4>

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

                    <div class="col-lg-3 col-md-6">
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

                            <h4 class="header-title m-t-0 m-b-30">Statistics</h4>

                            <div class="widget-chart-1">
                                <div class="widget-chart-box-1">
                                    <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#ffbd4a"
                                           data-bgColor="#FFE6BA" value="80"
                                           data-skin="tron" data-angleOffset="180" data-readOnly=true
                                           data-thickness=".15"/>
                                </div>
                                <div class="widget-detail-1">
                                    <h2 class="p-t-10 m-b-0"> 4569 </h2>
                                    <p class="text-muted">Revenue today</p>
                                </div>
                            </div>
                        </div>
                    </div><!-- end col -->

                    <div class="col-lg-3 col-md-6">
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

                            <h4 class="header-title m-t-0 m-b-30">Daily Sales</h4>

                            <div class="widget-box-2">
                                <div class="widget-detail-2">
                                    <span class="badge badge-pink pull-left m-t-20">32% <i class="zmdi zmdi-trending-up"></i> </span>
                                    <h2 class="m-b-0"> 158 </h2>
                                    <p class="text-muted m-b-25">Revenue today</p>
                                </div>
                                <div class="progress progress-bar-pink-alt progress-sm m-b-0">
                                    <div class="progress-bar progress-bar-pink" role="progressbar"
                                         aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                         style="width: 77%;">
                                        <span class="sr-only">77% Complete</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- end col -->

                </div>
                <!-- end row -->

                <div class="row">
                    <div class="col-lg-4">
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

                            <h4 class="header-title m-t-0">Node01 MongoDB <span id="mongoTi" class="label label-purple"> Unknown </span></h4>

                            <div class="widget-chart text-center">
                                <table class="table m-0">
                                    <tbody>
                                    <tr>
                                        <th scope="row">Average Response</th>
                                        <td><span id="mongoMs" class="label label-default">Unknown</span></td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Uptime</th>
                                        <td><span id="mongoUp" class="label label-default">Unknown</span></td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Live Load :</th>
                                        <td><span id="mongoLoad" class="label label-default">Unknown</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div><!-- end col -->
                    <div class="col-lg-4">
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

                            <h4 class="header-title m-t-0">Website MongoDB <span id="mongoTi" class="label label-purple"> Unknown </span></h4>

                            <div class="widget-chart text-center">
                                <table class="table m-0">
                                    <tbody>
                                    <tr>
                                        <th scope="row">Average Response</th>
                                        <td><span id="couchMs" class="label label-default">Unknown</span></td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Uptime</th>
                                        <td><span id="couchUp" class="label label-default">Unknown</span></td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Live Load :</th>
                                        <td><span id="couchLoad" class="label label-default">Unknown</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div><!-- end col -->
                    <div class="col-lg-4">
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

                            <h4 class="header-title m-t-0">PHP Fpm <span id="mongoTi" class="label label-purple"> Unknown </span></h4>

                            <div class="widget-chart text-center">
                                <table class="table m-0">
                                    <tbody>
                                    <tr>
                                        <th scope="row">Average Response</th>
                                        <td><span id="couchMs" class="label label-default">Unknown</span></td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Uptime</th>
                                        <td><span id="couchUp" class="label label-default">Unknown</span></td>
                                    </tr>
                                    <tr>
                                        <th scope="row">Live Load :</th>
                                        <td><span id="couchLoad" class="label label-default">Unknown</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div><!-- end col -->
                <!-- end row -->
        <footer class="footer text-right">
            2017 - 2018 © BadBlock.
        </footer>

    </div>


    <!-- ============================================================== -->
    <!-- End Right content here -->
    <!-- ============================================================== -->

</div>
<!-- END wrapper -->
<script>
    var resizefunc = [];

    var timer, delay = 3000;

    timer = setInterval(function(){
        $.ajax({
            type    : 'get',
            url     : '/api/mongo',
            dataType: 'json',
            success : function(data){

                data = jQuery.parseJSON(JSON.stringify(data));

                if (data.on = 1){
                    if(data.load > data.ms * 2){
                        $("#mongoLoad").attr('class',"label label-danger");
                    }else if(data.load > data.ms){
                        $("#mongoLoad").attr('class',"label label-warning");
                    }else{
                        $("#mongoLoad").attr('class',"label label-success");
                    }

                    $("#mongoTi").text("En service");
                    $("#mongoTi").attr('class',"label label-success");
                    $("#mongoMs").attr('class',"label label-success");
                    $("#mongoUp").attr('class',"label label-success");
                    $("#mongoMs").text(data.ms + " ms");
                    $("#mongoUp").text(data.uptime);
                    $("#mongoLoad").text(data.load + "ms");
                }else{
                    $("#mongoTi").text("Unknown");
                    $("#mongoTi").attr('class',"label label-danger");
                    $("#mongoMs").attr('class',"label label-danger");
                    $("#mongoUp").attr('class',"label label-danger");
                    $("#mongoLoad").attr('class',"label label-danger");
                    $("#mongoMs").text("Unknown");
                    $("#mongoUp").text("Unknown");
                    $("#mongoLoad").text("Unknown");
                }
            },
            error : function(data){
                    $("#mongoTi").text("Unknown");
                    $("#mongoTi").attr('class',"label label-danger");
                    $("#mongoMs").attr('class',"label label-danger");
                    $("#mongoUp").attr('class',"label label-danger");
                    $("#mongoLoad").attr('class',"label label-danger");
                    $("#mongoMs").text("Unknown");
                    $("#mongoUp").text("Unknown");
                    $("#mongoLoad").text("Unknown");
            }
        });
    }, delay);
</script>
    <script>
        var resizefunc = [];

        var timer, delay = 3000;

        timer = setInterval(function(){
            $.ajax({
                type    : 'get',
                url     : '/api/online',
                dataType: 'json',
                success : function(data){
                    data = jQuery.parseJSON(JSON.stringify(data));
                    $("#playerC").text(data.online - $( "#playersT" ).text());
                    $("#playersT").text(data.online);
                    if($("#playerC").text() < 0){
                        $("#playerC").attr('class',"badge badge-danger pull-left m-t-20");
                    }else{
                        $("#playerC").attr('class',"badge badge-success pull-left m-t-20");
                    }
                    var perct = (data.online / data.max) * 100;
                    $('#playerbar').css({
                        width: perct+"%"
                    });

                    //$("#playerC").attr('class',"label label-danger");
                },
                error : function(data){
                    $("#playersT").text("Unknown");
                    $("#mongoTi").attr('class',"label label-danger");
                }

            });
        }, delay);
    </script>
@endsection
@section("after_scripts")
    <script src="/assets/plugins/flot-chart/jquery.flot.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.time.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.tooltip.min.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.resize.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.pie.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.selection.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.stack.js"></script>
    <script src="/assets/plugins/flot-chart/jquery.flot.crosshair.js"></script>




@endsection
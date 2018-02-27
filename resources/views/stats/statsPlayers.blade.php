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

                            <h4 class="header-title m-t-0 m-b-30">Joueurs Enregistrés</h4>

                            <div class="widget-chart-1">
                                <div class="widget-detail-1">
                                    <h2 id="playersR" class="p-t-10 m-b-0"> 0</h2>
                                    <p class="text-muted">Registred players</p>
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

                            <h4 class="header-title m-t-0 m-b-30">Joueurs Bannis</h4>

                            <div class="widget-box-2">
                                <div class="widget-detail-2">
                                    <span id="playersBanB" class="badge badge-success pull-left m-t-20">0</span>
                                    <h2 id="playersBan" class="p-t-10 m-b-0"> 0</h2>
                                    <p id="playersBanText" class="text-muted">Players banned</p>
                                </div>
                                <div class="progress progress-bar-pink-alt progress-sm m-b-0">
                                    <div id="playerBarBan" class="progress-bar progress-bar-red" role="progressbar"
                                         aria-valuenow="" aria-valuemin="0" aria-valuemax="100"
                                         style="width:100%;">
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

                            <h4 class="header-title m-t-0 m-b-30">Joueurs Mutes</h4>

                            <div class="widget-box-2">
                                <div class="widget-detail-2">
                                    <span id="playersMuteB" class="badge badge-success pull-left m-t-20">0</span>
                                    <h2 id="playersMute" class="m-b-0"> 0</h2>
                                    <p id="playersMuteText" class="text-muted m-b-25">Players muted</p>
                                </div>
                                <div class="progress progress-bar-pink-alt progress-sm m-b-0">
                                    <div id="playerbarMute" class="progress-bar progress-bar-red" role="progressbar"
                                         aria-valuenow="" aria-valuemin="0" aria-valuemax="100"
                                         style="width: 100%;">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- end col -->

                </div>
                <!-- end row -->
                <div class="row">
                    <div class="col-lg-6">
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

                    <div class="col-lg-6">
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

        </div> <!-- content -->

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

    var timer, delay = 1000;

    timer = setInterval(function(){
        $.ajax({
            type    : 'get',
            url     : '/api/players',
            dataType: 'json',
            success : function(data){
                data = jQuery.parseJSON(JSON.stringify(data));
                $("#playersR").text(data.register);

                //Calcul pour la progress bar
                var perct = (data.banned / data.register) * 100;
                $('#playerBarBan').css({
                    width: perct+"%"
                });

                //joueur ban
                if($("#playersban").text() > data.banned){
                    $("#playersBanB").attr('class',"badge badge-danger pull-left m-t-20");
                    $("#playersBanB").text(data.banned - $("#playersban").text());
                    $("#playersBan").text(data.banned);
                    $("#playersBanText").text(Math.round(perct, 1) + "% Players muted")

                }else{
                    $("#playersbanB").attr('class',"badge badge-success pull-left m-t-20");
                    $("#playersBanB").text(data.banned - $("#playersban").text());
                    $("#playersBan").text(data.banned);
                    $("#playersBanText").text(Math.round(perct, 1) + "% Players banned");
                }

                //Calcul pour la progress bar
                var perct = (data.muted / data.register) * 100;
                $('#playerbarMute').css({
                    width: perct+"%"
                });
                if($("#playersMute").text() > data.muted){
                    $("#playersMuteB").attr('class',"badge badge-danger pull-left m-t-20");
                    $("#playersMuteB").text(data.muted - $("#playersMute").text());
                    $("#playersMute").text(data.muted);
                    $("#playersMuteText").text(Math.round(perct, 1) + "% Players muted")

                }else{
                    $("#playersMuteB").attr('class',"badge badge-success pull-left m-t-20");
                    $("#playersMuteB").text(data.muted - $("#playersMute").text());
                    $("#playersMute").text(data.muted);
                    $("#playersMuteText").text(Math.round(perct, 1) + "% Players muted")
                }




            },
            error : function(data){
                $("#playersR").text("Unknown");
                $("#playersBan").text("Unknown");
                $("#playersMute").text("Unknown");
                $("#playersMuteText").text("Unknown");
                $("#playersBanText").text("Unknown");
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
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>


    <script>
        Highcharts.createElement('link', {
            href: 'https://fonts.googleapis.com/css?family=Unica+One',
            rel: 'stylesheet',
            type: 'text/css'
        }, null, document.getElementsByTagName('head')[0]);

        Highcharts.theme = {
            colors: ['#2b908f', '#90ee7e', '#f45b5b', '#7798BF', '#aaeeee', '#ff0066',
                '#eeaaee', '#55BF3B', '#DF5353', '#7798BF', '#aaeeee'],
            chart: {
                backgroundColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },
                    stops: [
                        [0, '#2a2a2b'],
                        [1, '#2a2a2b']
                    ]
                },
                style: {
                    fontFamily: '\'Unica One\', sans-serif'
                },
                plotBorderColor: '#606063'
            },
            title: {
                style: {
                    color: '#E0E0E3',
                    textTransform: 'uppercase',
                    fontSize: '20px'
                }
            },
            subtitle: {
                style: {
                    color: '#E0E0E3',
                    textTransform: 'uppercase'
                }
            },
            xAxis: {
                gridLineColor: '#707073',
                labels: {
                    style: {
                        color: '#E0E0E3'
                    }
                },
                lineColor: '#707073',
                minorGridLineColor: '#505053',
                tickColor: '#707073',
                title: {
                    style: {
                        color: '#A0A0A3'

                    }
                }
            },
            yAxis: {
                gridLineColor: '#707073',
                labels: {
                    style: {
                        color: '#E0E0E3'
                    }
                },
                lineColor: '#707073',
                minorGridLineColor: '#505053',
                tickColor: '#707073',
                tickWidth: 1,
                title: {
                    style: {
                        color: '#A0A0A3'
                    }
                }
            },
            tooltip: {
                backgroundColor: 'rgba(0, 0, 0, 0.85)',
                style: {
                    color: '#F0F0F0'
                }
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        color: '#B0B0B3'
                    },
                    marker: {
                        lineColor: '#333'
                    }
                },
                boxplot: {
                    fillColor: '#505053'
                },
                candlestick: {
                    lineColor: 'white'
                },
                errorbar: {
                    color: 'white'
                }
            },
            legend: {
                itemStyle: {
                    color: '#E0E0E3'
                },
                itemHoverStyle: {
                    color: '#FFF'
                },
                itemHiddenStyle: {
                    color: '#606063'
                }
            },
            credits: {
                style: {
                    color: '#666'
                }
            },
            labels: {
                style: {
                    color: '#707073'
                }
            },

            drilldown: {
                activeAxisLabelStyle: {
                    color: '#F0F0F3'
                },
                activeDataLabelStyle: {
                    color: '#F0F0F3'
                }
            },

            navigation: {
                buttonOptions: {
                    symbolStroke: '#DDDDDD',
                    theme: {
                        fill: '#505053'
                    }
                }
            },

            // scroll charts
            rangeSelector: {
                buttonTheme: {
                    fill: '#505053',
                    stroke: '#000000',
                    style: {
                        color: '#CCC'
                    },
                    states: {
                        hover: {
                            fill: '#707073',
                            stroke: '#000000',
                            style: {
                                color: 'white'
                            }
                        },
                        select: {
                            fill: '#000003',
                            stroke: '#000000',
                            style: {
                                color: 'white'
                            }
                        }
                    }
                },
                inputBoxBorderColor: '#505053',
                inputStyle: {
                    backgroundColor: '#333',
                    color: 'silver'
                },
                labelStyle: {
                    color: 'silver'
                }
            },

            navigator: {
                handles: {
                    backgroundColor: '#666',
                    borderColor: '#AAA'
                },
                outlineColor: '#CCC',
                maskFill: 'rgba(255,255,255,0.1)',
                series: {
                    color: '#7798BF',
                    lineColor: '#A6C7ED'
                },
                xAxis: {
                    gridLineColor: '#505053'
                }
            },

            scrollbar: {
                barBackgroundColor: '#808083',
                barBorderColor: '#808083',
                buttonArrowColor: '#CCC',
                buttonBackgroundColor: '#606063',
                buttonBorderColor: '#606063',
                rifleColor: '#FFF',
                trackBackgroundColor: '#000000',
                trackBorderColor: '#000000'
            },

            // special colors for some of the
            legendBackgroundColor: 'rgba(0, 0, 0, 0.5)',
            background2: '#505053',
            dataLabelsColor: '#B0B0B3',
            textColor: '#C0C0C0',
            contrastTextColor: '#F0F0F3',
            maskColor: 'rgba(255,255,255,0.3)'
        };

        // Apply the theme
        Highcharts.setOptions(Highcharts.theme);

        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        Highcharts.chart('container', {
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function () {

                        // set up the updating of the chart each second
                        var series = this.series[0];
                        setInterval(function () {
                            $.ajax({
                                url: '/api/playersjson',
                                success: function(point) {
                                    var x = (new Date()).getTime() // current time
                                    series.addPoint([x, parseInt(point)], true, true);

                                    console.log(series);
                                },
                                cache: false
                            });




                        }, 2000);
                    }
                }
            },
            title: {
                text: 'Online Players'
            },
            xAxis: {
                title: {
                    text: 'Heure'
                },
                type: 'datetime',
                tickPixelInterval: 1000,
            },
            yAxis: {
                title: {
                    text: 'Nombre de joueurs'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: '',
                data: (function () {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;

                    for (i = -90; i <= 0; i += 1) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()
                        });
                    }
                    return data;
                    console.log(data);
                }())
            }]
        });


    </script>




@endsection
@extends('layouts.app')
@section('content')

    <script>
        function showResult(str) {
            if (str.length==0) {
                document.getElementById("livesearch").innerHTML="";
                document.getElementById("livesearch").style.border="0px";
                return;
            }
            if (window.XMLHttpRequest) {
                // code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp=new XMLHttpRequest();
            } else {  // code for IE6, IE5
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.onreadystatechange=function() {
                if (this.readyState==4 && this.status==200) {
                    document.getElementById("livesearch").innerHTML=this.responseText;
                }
            }
            xmlhttp.open("GET","/players/search/json/"+str,true);
            xmlhttp.send();
        }
    </script>

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

                            <h4 class="header-title m-t-0 m-b-30">Rechercher un joueur</h4>

                            <form>
                                <input type="text" class="form-control" size="30" onkeyup="showResult(this.value)">
                                <div id="livesearch"></div>
                            </form>


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

        var timer, delay = 30000000;

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

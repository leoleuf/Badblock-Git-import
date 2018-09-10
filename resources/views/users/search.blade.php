@extends('layouts.app')
@section('content')
    <!-- ============================================================== -->
    <!-- Start right Content here -->
    <!-- ============================================================== -->
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <br>
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

                            <h4 class="header-title m-t-0 m-b-30">Rechercher un joueur par pseudo</h4>

                            <form>
                                <input type="text" id="player_search" onkeyup="searchPlayer()" class="form-control" size="30">
                                <div id="livesearch"></div>
                            </form>
                            <table class="table">
                                <thead class="thead-light">
                                <tr>
                                    <th>Pseudo</th>
                                    <th>Casier</th>
                                    <th>Achat</th>
                                    <th>Profile</th>
                                </tr>
                                </thead>
                                <tbody id="search_down">

                                </tbody>
                            </table>
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

                            <h4 class="header-title m-t-0 m-b-30">Rechercher un joueur par IP</h4>

                            <form>
                                <input type="text" id="player_searchIP" onkeyup="searchPlayerIP()" class="form-control" size="30">
                                <div id="livesearch"></div>
                            </form>
                            <table class="table">
                                <thead class="thead-light">
                                <tr>
                                    <th>Pseudo</th>
                                    <th>Casier</th>
                                    <th>Achat</th>
                                    <th>Profile</th>
                                </tr>
                                </thead>
                                <tbody id="search_down_IP">

                                </tbody>
                            </table>
                        </div>
                    </div><!-- end col-->
                </div>
            </div> <!-- content -->
            <footer class="footer text-right">
                2017 - 2018 © BadBlock.
            </footer>
        </div>
    </div>

    <script>
        function searchPlayer() {

            input = document.getElementById("player_search");
            filter = input.value.toUpperCase();
            ul = document.getElementById("search_down");
            li = ul.getElementsByTagName("li");

            last = "";

            $.ajax({
                url: '/api/stats/search',
                type: "POST",
                data: { search_player : filter},
                success: function(data) {
                    data = JSON.parse(data);
                    $("#search_down").empty();
                    for (i = 0; i < data.length; i++) {
                        if (data[i] != last){
                            $("#search_down").append('<tr><th scope="row">' + data[i]["name"] +'</th><td><a href="/casier/' + data[i]["name"] +'" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i class="fa fa-wrench"></i> </a></td><td><a href="/website/achat/' + data[i]["uniqueId"] +'" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i class="fa fa-wrench"></i> </a></td><td><a href="/profile/' + data[i]["uniqueId"] +'" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i class="fa fa-wrench"></i> </a></td></tr>');
                            console.log(data[i]);
                        }
                    }
                }
            });
        }


        function searchPlayerIP() {

            input = document.getElementById("player_searchIP");
            filter = input.value.toUpperCase();
            ul = document.getElementById("search_down_IP");
            li = ul.getElementsByTagName("li");

            last = "";

            $.ajax({
                url: '/api/stats/searchip',
                type: "POST",
                data: { search_player : filter},
                success: function(data) {
                    data = JSON.parse(data);
                    $("#search_down_IP").empty();
                    for (i = 0; i < data.length; i++) {
                        if (data[i] != last){
                            $("#search_down_IP").append('<tr><th scope="row">' + data[i]["name"] +'</th><td><a href="/casier/' + data[i]["name"] +'" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i class="fa fa-wrench"></i> </a></td><td><a href="/website/achat/' + data[i]["uniqueId"] +'" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i class="fa fa-wrench"></i> </a></td><td><a href="/profile/' + data[i]["uniqueId"] +'" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i class="fa fa-wrench"></i> </a></td></tr>');
                            console.log(data[i]);
                        }
                    }
                }
            });
        }
    </script>
@endsection

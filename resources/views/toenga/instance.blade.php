@extends('layouts.app')
@section('content')
    <style>
        html,
        .console {
            padding: 15px;
            font-family: Consolas;
            color: #21ac13;
        }
        .console{
            height: 1000px;
            overflow:auto;
        }
        .console .line {
            margin-bottom: 5px;
        }
        .console .command {
            background-color: transparent;
            color: #fff;
            border: none;
            width: 90%;
            outline: none;
            -webkit-box-shadow: none;
            box-shadow: none;
            padding: 0;
        }
        .green {
            color: #90ee90;
        }
        .yellow {
            color: #ff0;
        }
    </style>
tps / ram / processeur / nb de joueurs
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

                <h4 class="header-title m-t-0 m-b-30">Nombre de Joueurs</h4>

                <div class="widget-chart-1">
                    <div class="widget-chart-box-1">
                        <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#f05050 "
                               data-bgColor="#F9B9B9" value="0"
                               data-skin="tron" data-angleOffset="180" data-readOnly=true
                               data-thickness=".15"/>
                    </div>

                    <div class="widget-detail-1">
                        <h2 class="p-t-10 m-b-0"> 0</h2>
                        <p class="text-muted">Players connected to this instance</p>
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

                <h4 class="header-title m-t-0 m-b-30">Live TPS</h4>

                <div class="widget-chart-1">
                    <div class="widget-chart-box-1">
                        <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#f05050 "
                               data-bgColor="#F9B9B9" value="0"
                               data-skin="tron" data-angleOffset="180" data-readOnly=true
                               data-thickness=".15"/>
                    </div>

                    <div class="widget-detail-1">
                        <h2 class="p-t-10 m-b-0"> 0</h2>
                        <p class="text-muted">Tick Per Second</p>
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

                <h4 class="header-title m-t-0 m-b-30">Ram</h4>

                <div class="widget-chart-1">
                    <div class="widget-chart-box-1">
                        <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#f05050 "
                               data-bgColor="#F9B9B9" value="0"
                               data-skin="tron" data-angleOffset="180" data-readOnly=true
                               data-thickness=".15"/>
                    </div>

                    <div class="widget-detail-1">
                        <h2 class="p-t-10 m-b-0"> 0 GB</h2>
                        <p class="text-muted">Random Access Memory</p>
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

                <h4 class="header-title m-t-0 m-b-30">CPU Machine</h4>

                <div class="widget-chart-1">
                    <div class="widget-chart-box-1">
                        <input data-plugin="knob" data-width="80" data-height="80" data-fgColor="#f05050 "
                               data-bgColor="#F9B9B9" value="0"
                               data-skin="tron" data-angleOffset="180" data-readOnly=true
                               data-thickness=".15"/>
                    </div>

                    <div class="widget-detail-1">
                        <h2 class="p-t-10 m-b-0"> 0%</h2>
                        <p class="text-muted">Central processing unit usage</p>
                    </div>
                </div>
            </div>
        </div><!-- end col -->
                <div class="col-lg-12">
                    <div class="card-box p-b-0">
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

                        <h4 class="header-title m-t-0 m-b-30">{{ $uuid }} </h4>

                        <form>
                            <div id="basicwizard" class=" pull-in">
                                <ul class="nav nav-tabs navtab-wizard nav-justified">
                                    <li class="active">
                                        <a href="#tab1" data-toggle="tab" aria-expanded="true">Console</a>
                                    </li>
                                    <li class="">
                                        <a href="#tab2" data-toggle="tab" aria-expanded="false">Graphiques</a>
                                    </li>
                                    <li class="">
                                        <a href="#tab3" data-toggle="tab" aria-expanded="false">Paramètres</a>
                                    </li>
                                    <li>
                                        <a href="#tab4" data-toggle="tab">Logs</a>
                                    </li>

                                </ul>
                                <div class="tab-content m-b-0">
                                    <div class="tab-pane m-t-10 fade active in" id="tab1">
                                        <div class="console" id="console">
                                            <div class="command-input"><br/>
                                                <div class="line"><span class="green" data-auth="data-auth">root@skyblcok </span><span class="yellow" data-route="data-route">~/</span></div>
                                                <div class="line"><span class="white">$</span>&nbsp;
                                                    <input class="command" data-input="data-input"/>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="tab-pane m-t-10 fade" id="tab2">
                                        <div class="row">
                                            s
                                        </div>
                                    </div>
                                    <div class="tab-pane m-t-10 fade" id="tab3">
                                        s
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                </div>

        </div>
        </div>
        </div>
        </div>
        <!-- end row -->

@endsection
@section("after_scripts")
    <link href="/assets/plugins/jstree/style.css" rel="stylesheet" type="text/css" />


    <script src="/assets/plugins/jstree/jstree.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"></script>

    <script>


        $(function () {



            // jQuery Cache objects
            var doc = $(document);
            var con = $("#console");
            var com = $(".command");
            // Console History
            var hist = [];
            var histUp = [];
            var histDo = [];
            var status = false;
            var keyP;

            // Focus on Input :)
            doc.on('mouseup', function() {
                com.focus();
            });

            // Helper for print in `console` xD
            function Output() {
                this.puts = function(message) {
                    // Maybe i should escape string ?)
                    $("<div class=line><span class=white>" + message + "</span></div>")
                        .appendTo("div.command-line");
                }
            }

            // Command Line Object
            function CommandLine(console) {

                // jQuery object
                this.console = console;
                // Commands register in class
                this.commands = [];

                // Register some command..
                this.on = function(regexp, callback) {
                    this.commands.push({
                        "regexp": regexp,
                        "callback": callback
                    });
                }

                // Get commands :v
                this.get = function() {
                    return this.commands;
                }

                // Just prepare the stage
                this.consolize = function() {
                    $("<div class='command-line'></div>").prependTo(this.console);
                }

                // Set the attributes
                this.set = function(config) {
                    if (config.auth != null) this.console.find(".command-input *[data-auth]")
                        .text(config.auth);
                    if (config.route != null) this.console.find(".command-input *[data-route]")
                        .text(config.route);
                    // What the hell it's doing this here?
                    if (config.input != null) this.console.find(".command-input *[data-input]")
                        .text(config.input);
                }
            }

            // Command Line
            var cli = new CommandLine(con);

            // Prepare the command-history
            cli.consolize();

            /**
             * Routing
             **/



            cli.on("^help\s?$", function(output) {
                // On command
                output.puts("Fuck you !");
            });

            cli.on("^login\s?$", function(output) {
                // On command
                output.puts("Connection en cour...");
                $.ajax({
                    type    : 'get',
                    url     : '/toenga/websock/{{ $uuid }}',
                    dataType: 'json',
                    success : function(data){
                        if (window.WebSocket === undefined) {
                            $("<div class=line><span class=white>Ton navigateur est merdiques</span></div>")
                                .appendTo("div.command-line");
                            return;
                        } else {
                            ws = initWS();
                        }

                        if(data.perm == 1){
                            $("<div class=line><span class=white>Connection en lecteur seule !</span></div>")
                                .appendTo("div.command-line");
                        }else if(data.perm == 2){
                            $("<div class=line><span class=white>Connection all permissions !</span></div>")
                                .appendTo("div.command-line");
                        }

                        keyP = data.key;
                        console.log("token : " + data.token);
                        console.log("key : " + keyP);

                        function initWS() {
                            var socket = new WebSocket("ws://" + data.host),
                                container = $("#container")
                            socket.onopen = function() {
                                status = true;
                                $("<div class=line><span class=white>Connection établie !</span></div>")
                                    .appendTo("div.command-line");
                                ws.send(JSON.stringify({ token : data.token }) + "\n");

                            };
                            socket.onmessage = function (e) {
                                $("<div class=line><span class=white>" + e.data + "</span></div>")
                                    .appendTo("div.command-line");
                            }
                            socket.onclose = function () {
                                status = false;
                                $("<div class=line><span class=white>Connection perdue</span></div>")
                                    .appendTo("div.command-line");
                            }
                            return socket;
                        }
                    },
                    error : function(data){
                        $("<div class=line><span class=white>Error : acces denied !</span></div>")
                            .appendTo("div.command-line");
                    }

                });


            });

            cli.on("^(.+)$", function(output, matches) {
                if (status = true){
                    var encrypted = CryptoJS.AES.encrypt(keyP, matches[1]).toString();
                    console.log(encrypted);
                    hist.push(matches[1]);
                    ws.send(encrypted);
                    return true;
                }else{

                }
            });



            // When the key return is pressed
            com.on('keydown', function(e) {
                var key = e.which;
                // Equal `Return`
                if (key == 13) {
                    // Register the action
                    var input = con.find(".command-input");
                    var result = $(input.html()).appendTo(con.find(".command-line"));
                    result.find(".command")
                        .val(com.val())
                        .removeAttr("data-input")
                        .attr('disabled', 'disabled');

                    // Get all register commands
                    var commands = cli.get();
                    // Process the next command
                    var next = true;

                    commands.forEach(function(command) {
                        // Regular Expresion
                        var regexp = command.regexp;
                        if (typeof command.regexp === 'string')
                            regexp = new RegExp(command.regexp, "i");
                        // Set output
                        var output = new Output();
                        var match = regexp.test(com.val()); // get if regexp match as bool
                        var matches = regexp.exec(com.val()); // get group matches
                        // Run Callback
                        if (match && next)
                        // if next == true keep processing commands :D
                            next = command.callback(output, matches);
                    });

                    // Input is empty now :)
                    com.val("");

                    // Focus again & scroll to bottom
                    com.focus();
                    $("#console").scrollTop($("#console")[0].scrollHeight);

                    histUp = hist;
                } else if (key == 38) {
                    var tmph = histUp.pop();
                    if (tmph != undefined) {
                        histDo.push(tmph);
                        com.val(tmph);
                    }
                } else if (key == 40) {
                    var tmph = histDo.pop();
                    if (tmph != undefined)
                        histUp.push(tmph);
                    com.val(tmph);
                }
            });


        });






    </script>





@endsection
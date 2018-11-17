@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Éditer un serveur</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Tableau de bord</a></li>
                <li class="breadcrumb-item active">Éditer un serveur</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
    <link rel="stylesheet" href="/panel/input/dist/tagify.css">

    <script src="/panel/input/dist/tagify.polyfills.js"></script>
    <script src="/panel/input/dist/tagify.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="/panel/input/dist/jQuery.tagify.js"></script>

    <style>
        .imggg{
            max-width:300px;
            height:300px;
            margin-top:50px;
        }
        .imggg2{
            max-width:317px;
            height:74px;
            margin-top:50px;
        }
        input{
            margin-top:20px;
        }
    </style>
    <div class="col-lg-12">
        <div class="card">
            <div class="card-block">
                <form action="#" method="post" enctype="multipart/form-data" novalidate>
                    {{ csrf_field() }}
                    <div class="row">
                        <div class="col-sm-6">
                            @if (session()->has('flash'))
                                @foreach(session('flash') as $messageData)
                                    <div class="alert alert-{{ $messageData['level'] }} {{ $messageData['important'] ? 'alert-important' : '' }}">
                                        @if(!$messageData['important'])
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        @endif

                                        {!! trans($messageData['message']) !!}
                                    </div>
                                @endforeach
                            @endif
                            <div class="form-group">
                                <label for="name">Nom du serveur</label>
                                <input name="name" class="form-control" placeholder="Mon serveur survie !" value="{{ $data->name }}" required>
                            </div>
                                <div class="form-group">
                                    <label for="adress">Adresse du serveur (optionnel)</label>
                                    <input name="adress" class="form-control" placeholder="45.63.22.33" value="{{ $data->ip }}">
                                </div>
                                <div class="form-group">
                                    <label for="group4">Type de vérification de vote</label><br />
                                    <input name="group4" type="radio" id="radio_7" value="TRUE" class="radio-col-red"<?php if ($data->votetype == "TRUE") { ?> checked<?php } ?>>
                                    <label for="radio_7">TRUE, méthode simple (<a title="Implémenter la méthode TRUE" href="/add-server/true" target="_blank">Implémenter</a>)</label><br />
                                    <input name="group4" type="radio" id="radio_8" value="JSON" class="radio-col-cyan"<?php if ($data->votetype == "JSON") { ?> checked<?php } ?>>
                                    <label for="radio_8">JSON, méthode intermédiaire (<a title="Implémenter la méthode JSON" href="/add-server/json" target="_blank">Implémenter</a>)</label><br />
                                    <input name="group4" type="radio" id="radio_9" value="CALLBACK" class="radio-col-light-green"<?php if ($data->votetype == "CALLBACK") { ?> checked<?php } ?>>
                                    <label for="radio_9">CallBack, méthode experte (<a title="Implémenter la méthode CallBack" href="/add-server/callback" target="_blank">Implémenter</a>)</label><br />
                                    <input name="group4" type="radio" id="radio_10" value="VOTIFIER" class="radio-col-light-purple"<?php if ($data->votetype == "VOTIFIER") { ?> checked<?php } ?>>
                                    <label for="radio_10">VOTIFIER (<a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">Implémenter</a>)</label>
                                </div>
                                <div class="form-group" id="callbackurl"<?php if ($data->votetype != "CALLBACK") { ?> style="display: none;"<?php } ?>>
                                    <label for="callback_url">URL du CallBack <a title="Implémenter la méthode CallBack" href="/add-server/callback" target="_blank">(?)</a></label>
                                    <input name="callback_url" class="form-control" value="{{ $data->callback_url }}">
                                </div>
                                <div class="form-group" id="votifier"<?php if ($data->votetype != "VOTIFIER") { ?> style="display: none;"<?php } ?>>
                                    <span style="color: red;">La méthode VOTIFIER fonctionne uniquement avec Minecraft. Il faut que le plugin soit installé.</span><br />
                                    Vous pouvez ajouter différentes instances de jeu avec Votifier où le joueur recevra sa récompense.<br /><br />
                                    @if (isset($data->votifierdata) && !empty($data->votifierdata))
                                        @foreach(json_decode($data->votifierdata) as $vk => $vv)
                                        <div class="row">
                                            <div class="col-2">
                                                <label for="votifier_servername">Nom serveur</label>
                                                <input name="votifier_servername[]" class="form-control" value="{{ $vv->name }}">
                                            </div>
                                            <div class="col-3">
                                                <label for="votifier_serverip">IP Votifier <a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">(?)</a></label>
                                                <input name="votifier_serverip[]" class="form-control" value="{{ $vv->ip }}">
                                            </div>
                                            <div class="col-3">
                                                <label for="votifier_serverport">Port Votifier <a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">(?)</a></label>
                                                <input name="votifier_serverport[]" class="form-control" value="{{ $vv->port }}">
                                            </div>
                                            <div class="col-4">
                                                <label for="votifier_publickey">Clé publique <a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">(?)</a></label>
                                                <textarea name="votifier_publickey[]" class="form-control" placeholder="Contenu du fichier public.key dans le dossier rsa du plugin Votifier">{{ $vv->key }}</textarea>
                                            </div>
                                            <br /><br />
                                        </div>
                                        @endforeach
                                    @else
                                        <div class="row">
                                            <div class="col-2">
                                                <label for="votifier_servername">Nom serveur</label>
                                                <input name="votifier_servername[]" class="form-control" value="{{ old('votifier_servername') }}">
                                            </div>
                                            <div class="col-3">
                                                <label for="votifier_serverip">IP Votifier <a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">(?)</a></label>
                                                <input name="votifier_serverip[]" class="form-control" value="">
                                            </div>
                                            <div class="col-3">
                                                <label for="votifier_serverport">Port Votifier <a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">(?)</a></label>
                                                <input name="votifier_serverport[]" class="form-control" value="">
                                            </div>
                                            <div class="col-4">
                                                <label for="votifier_publickey">Clé publique <a title="Implémenter la méthode Votifier" href="/add-server/votifier" target="_blank">(?)</a></label>
                                                <textarea name="votifier_publickey[]" class="form-control" placeholder="Contenu du fichier public.key dans le dossier rsa du plugin Votifier"></textarea>
                                            </div>
                                            <br /><br />
                                        </div>
                                    @endif
                                    <div id="votifier-add">

                                    </div>
                                    <a title="Ajouter un serveur Votifier supplémentaire" href="javascript:void(0)" onclick="votifier();" style="color: green; cursor:hand; font-weight: 500;"><i class="mdi mdi-plus-circle"></i> Ajouter un serveur Votifier supplémentaire</a>
                                    <script>
                                        var vc = 1;
                                        function votifier()
                                        {
                                            vc++;
                                            $('#votifier-add').append("<div class=\"row\"> <div class=\"col-2\"> <label for=\"votifier_serverip\">Nom serveur</label> <input name=\"votifier_servername[]\" class=\"form-control\" value=\"\"> </div><div class=\"col-3\"> <label for=\"votifier_serverip\">IP Votifier <a title=\"Implémenter la méthode Votifier\" href=\"/add-server/votifier\" target=\"_blank\">(?)</a></label> <input name=\"votifier_serverip[]\" class=\"form-control\" value=\"\"> </div><div class=\"col-3\"> <label for=\"votifier_serverport\">Port Votifier <a title=\"Implémenter la méthode Votifier\" href=\"/add-server/votifier\" target=\"_blank\">(?)</a></label> <input name=\"votifier_serverport[]\" class=\"form-control\" value=\"\"> </div><div class=\"col-4\"> <label for=\"votifier_publickey\">Clé publique <a title=\"Implémenter la méthode Votifier\" href=\"/add-server/votifier\" target=\"_blank\">(?)</a></label> <textarea name=\"votifier_publickey[]\" class=\"form-control\" placeholder=\"Contenu du fichier public.key dans le dossier rsa du plugin Votifier\"></textarea> </div><br/><br/> </div>");                                        }
                                    </script>
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">Lien du site du serveur (RECOMMANDÉ)</label>
                                    <input name="website" class="form-control" placeholder="https://mon-serveur.fr" value="{{ $data->website }}">
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">Description courte (min 500 charactères, affichée sur le classement)</label>
                                    <textarea id="comp" name="text" class="form-control" placeholder="Découvrez le meilleur serveur XXXXX, IP : XXXX, économie complète..." style="height: 300px;" required>{{ $data->short_desc }}</textarea>
                                    <p id="tota"></p>
                                    <span style="color: orange;"><b>Attention: la description courte du serveur doit être unique et ne doit pas être un copier-coller</b></span>
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">Description complète du serveur (min 1000 caractères)</label>
                                    <textarea id="comp2" name="desc" class="form-control" placeholder="" style="height: 500px;" required>{{ $data->description }}</textarea>
                                    <p id="tota2"></p>
                                    <span style="color: orange;"><b>Attention: la description complète du serveur doit être unique et ne doit pas être un copier-coller</b></span>
                                </div>
                                <button type="submit" class="btn btn-primary">Envoyer</button>
                        </div>
                        <script>

                            function readURL(input) {
                                if (input.files && input.files[0]) {
                                    var reader = new FileReader();

                                    reader.onload = function (e) {
                                        $('#blah')
                                            .attr('src', e.target.result);
                                    };

                                    reader.readAsDataURL(input.files[0]);
                                }
                            }
                        </script>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label for="exampleInputEmail1">Icone du serveur :</label>
                                <div class="container">
                                    <label class="custom-file">
                                        <input name="image" type="file" id="file" class="custom-file-input" onchange="readURL(this);" >
                                        <span class="custom-file-control"></span>
                                    </label>
                                    <br>
                                    <img class="imggg" id="blah" src="/storage/icone/icon{{ $data->id }}.jpg"/>
                                </div>
                                <label for="exampleInputEmail1">Bannière du serveur :</label>
                                <div class="container">
                                    <label class="custom-file">
                                        <input name="banner" type="file" id="file" class="custom-file-input" onchange="readURL(this);" >
                                        <span class="custom-file-control"></span>
                                    </label>
                                    <br>
                                    <img class="imggg2" id="blah" src="/storage/banner/banner{{ $data->id }}.jpg"/>
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">Tags décrivant votre serveur :</label>
                                    <input type="text" name='tags' placeholder='MiniJeux Faction...' pattern='^[A-Za-z_ ]{1,15}$' value='
                                        @foreach(json_decode($data->tag) as $row)
                                    {{ $row }},
                                        @endforeach
                                            '>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var input = document.querySelector('input[name=tags]'),
            tagify = new Tagify(input, {
                enforceWhitelist : true,
                delimiters          : ",| ",
                suggestionsMinChars : 1,
                maxTags             : 8,
                blacklist           : ["fuck", "pd", "ntm"],
                keepInvalidTags     : false,
                whitelist           : [
                    @foreach($tag as $row)
                    "{{ $row }}",
                    @endforeach
                ]
            });

    </script>
@endsection
@section('after_script')
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.15.0/themes/prism.min.css">
    <script src='https://cdnjs.cloudflare.com/ajax/libs/prism/1.15.0/prism.min.js'></script>
    <script src='https://cdnjs.cloudflare.com/ajax/libs/prism/1.15.0/components/prism-javascript.min.js'></script>
    <script>
        $(document).ready(function () {

            $('input[type="radio"]').click(function () {
                if ($(this).attr("value") == "CALLBACK") {
                    $("#callbackurl").show('slow');
                    $("#votifier").hide('slow');
                }
                else if ($(this).attr("value") == "VOTIFIER") {
                    $("#callbackurl").hide('slow');
                    $("#votifier").show('slow');
                }
                else
                {
                    $("#votifier").hide('slow');
                    $("#callbackurl").hide('slow');
                }
            });
        });
    </script>


    <script>
        var textarea = document.querySelector('#comp');
        var blockCount = document.getElementById('tota');

        function count() {
            var count = 500-textarea.value.length;

            if(count<=0) {
                blockCount.innerHTML= "<span style='color: green;'>Le nombre de caractères est correct. Bravo!</span>";
            }
            else if(count>0) {
                blockCount.innerHTML= "<span style='color: red;'>Vous devez encore taper " + count + " caractères avec un contenu pertinent et assez descriptif de votre serveur.</span>";
            }
        }

        textarea.addEventListener('keyup', count);
        count();


        var textarea2 = document.querySelector('#comp2');
        var blockCount2 = document.getElementById('tota2');

        function count2() {
            var count = 1000-textarea2.value.length;

            if(count<=0) {
                blockCount2.innerHTML= "<span style='color: green;'>Le nombre de caractères est correct. Bravo!</span>";
            }
            else if(count>0) {
                blockCount2.innerHTML= "<span style='color: red;'>Vous devez encore taper " + count + " caractères avec un contenu pertinent et assez descriptif de votre serveur.</span>";
            }
        }

        textarea2.addEventListener('keyup', count2);
        count2();
    </script>

    <style>
        .red {
            color : #ff0000 !important;
        }
    </style>

@endsection
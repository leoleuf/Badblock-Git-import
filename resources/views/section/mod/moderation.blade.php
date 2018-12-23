@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <h3 style="text-align: center">Modération Center</h3>
                <div class="row">
                    <div class="col-lg-6">

                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">
                        <div class="card-box">
                            <h4 class="m-t-0 header-title">Vos Dernières Sanctions  -  Status du lien : <span id="sanction_state" class="badge badge-danger">Inactif</span></h4>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Type</th>
                                    <th>Pseudo</th>
                                    <th>Raison</th>
                                    <th>Screens</th>
                                    <th>Casier</th>
                                </tr>
                                </thead>
                                <tbody id="sanction_list">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="card-box">
                            <h4 class="m-t-0 header-title">Vos Screenshots  -  Status du lien : <span id="screen_state" class="badge badge-danger">Inactif</span></h4>
                        </div>
                    </div>
                    <div id="screen_list" class="row">

                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')
    <script src="assets/plugins/toastr/toastr.min.js"></script>

    <script type="application/javascript">

        var saisie = false;
        var screens = [];

        function get_screen(){
            $('#screen_state').text('Refresh...');
            $('#screen_state').attr('class','badge badge-info');
            $.ajax({
                type: "GET",
                url: "/moderation/screen",
                success:function(data)
                {
                    if (saisie === false){
                        $('#screen_state').text('Actif');
                        $('#screen_state').attr('class','badge badge-success');

                        obj = JSON.parse(data);
                        $('#screen_list').empty();

                        for (screen in obj){
                            console.log(obj[screen]);


                            $('#screen_list').append('<div class="col-md-6 col-xl-3 col-lg-4 natural personal" id="'+ obj[screen]['_id']['$oid'] +'" onclick="addscreen(\''+ obj[screen]['_id']['$oid'] +'\')">\n' +
                                '                                <div class="gal-detail thumb">\n' +
                                '                                    <a href="https://images.badblock.fr/i/'+ obj[screen]['file_name'] +'" target="_blank" class="image-popup" title="Screenshot-1">\n' +
                                '                                        <img id="image'+ obj[screen]['_id']['$oid'] +'" src="https://images.badblock.fr/i/'+ obj[screen]['file_name'] +'" class="thumb-img" alt="work-thumbnail">\n' +
                                '                                    </a>\n' +
                                '                                    <h5>' + obj[screen]['date'] +'</h5>\n' +
                                '                                </div>\n' +
                                '                            </div>');


                        }
                    }

                    setTimeout(function(){
                        get_screen();
                    }, 10000);
                },
                error:function(data)
                {
                    $('#screen_state').text('Erreur Serveur !');
                    $('#screen_state').attr('class','badge badge-danger');

                    console.log(data);
                    setTimeout(function(){
                        get_screen();
                    }, 10000);
                }
            });
        }
        //Call our function
        get_screen();

        function get_sanction(){
            $('#sanction_state').text('Refresh...');
            $('#sanction_state').attr('class','badge badge-info');
            $.ajax({
                type: "GET",
                url: "/moderation/sanction",
                success:function(data)
                {
                    if (saisie === false){
                        $('#sanction_state').text('Actif');
                        $('#sanction_state').attr('class','badge badge-success');

                        obj = JSON.parse(data);
                        $('#sanction_list').empty();

                        for (sanction in obj){
                            console.log(obj[sanction]);

                            $('#sanction_list').append("<tr id='"+ obj[sanction]['id'] +"'>\n" +
                                "                                    <th scope=\"row\">" + obj[sanction]['id'] +"</th>\n" +
                                "                                    <td>" + obj[sanction]['type'] +"</td>\n" +
                                "                                    <td>" + obj[sanction]['pseudo'] +"</td>\n" +
                                "                                    <td>" + obj[sanction]['reason'] +"</td>\n" +
                                "                                    <td><button type=\"button\" class=\"btn btn-info btn-bordred waves-effect w-md waves-light m-b-5\" onclick=\"union(" + obj[sanction]['id'] +")\">Preuves</button></td>\n" +
                                "                                    <td><button onClick=\"window.open('/moderation/mcasier/" + obj[sanction]['pseudo'] +"','Sanctions','resizable,height=450,width=700'); return false;\" class=\"btn btn-icon waves-effect waves-light btn-danger m-b-5\"> <i class=\"fa fa-vcard\"></i> </button></td>\n" +
                                "                                </tr>");

                        }
                    }

                    setTimeout(function(){
                        get_sanction();
                    }, 5000);
                },
                error:function(data)
                {
                    $('#sanction_state').text('Erreur Serveur !');
                    $('#sanction_state').attr('class','badge badge-danger');

                    console.log(data);
                    setTimeout(function(){
                        get_sanction();
                    }, 5000);
                }
            });
        }
        //Call our function
        get_sanction();


       
        function union(id) {
            saisie = true;
            $('#' + id).focus();
            $( ".btn-info" ).attr('class', 'btn btn-info btn-bordred waves-effect w-md waves-light m-b-5 disabled');

            if(screens.length === 0){
                saisie = false;
                toastr.warning('Aucun screenshot de sélectionnés !', ' Attention !');
            }else{
                $.ajax({
                    type: "POST",
                    url: "/moderation/union",
                    data: { sanc_id: id, screens: JSON.stringify(screens) },
                    dataType: "JSON",
                    success:function(data)
                    {
                        saisie = false;
                        toastr.success('Preuve(s) ajoutée(s)', "Les preuves ont correctement était ajoutée(s) à la sanction !");

                    },
                    error:function(data)
                    {
                        saisie = false;
                        toastr.error('Erreur !', 'Un problème s\'est produit pendant l\'ajout des preuves !');

                    }
                });
            }
        }

        function addscreen(file) {
            saisie = false;
            if (screens.includes(file) === true){
                $('#'+ file).attr('class', 'col-md-6 col-xl-3 col-lg-4 natural personal');
                $('#image'+ file).attr('style', "");
                $('#badge'+ file).remove();
                remove(file, screens);
                console.log(screens);
            }else{
                screens.push(file);
                $('#'+ file).attr('class', 'col-md-6 col-xl-3 col-lg-4 natural personal disabled');
                $('#'+ file).append('<span id="badge'+ file +'" class="badge badge-success"><i class="fa fa-check-circle"></i></span>');
                $('#image'+ file).attr('style', "background-color:#01FF09; -webkit-filter: hue-rotate(90deg); -webkit-filter: brightness(20%);");
                console.log(screens);
            }
        }

        function remove(item, array) {
            var index = array.indexOf(item);
            if (index > -1) {
                array.splice(index, 1);
            }
            return index;
        }
    </script>
@endsection

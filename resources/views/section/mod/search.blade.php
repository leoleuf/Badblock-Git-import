@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <h3 style="text-align: center">Recherche les sanctions d'un modérateur</h3>
                <hr>
                <div class="row">
                    <div class="col-lg-3"></div>
                    <div class="col-lg-6">
                        <div class="row">
                            <div class="col-lg-9">
                                <input type="text" placeholder="Nom d'utilisateur" id="username_search" class="form-control">
                            </div>
                            <div class="col-lg-3">
                                <button class="btn btn-primary btn-block waves-effect waves-light" onclick="search()">Rechercher</button>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3"></div>
                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')
    <script src="/assets/plugins/toastr/toastr.min.js"></script>
    <script type="text/javascript">
        function search() {

            let username = $('#username_search').val();

            if(username.length === 0){

                toastr.warning('Entrez un nom d\'utilisateur');

            }else{

                $.ajax({
                    type: "GET",
                    url: "/moderation/sanction/"+username,
                    success:function(data)
                    {
                        
                        window.location.href = "/moderation/sanction/"+username;

                    },
                    error:function(data)
                    {

                        toastr.error('Une erreur est survenu', 'Erreur');

                    }
                });

            }
        }
    </script>
@endsection
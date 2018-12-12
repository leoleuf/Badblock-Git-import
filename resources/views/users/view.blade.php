@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="row">
                    <br>
                    <div class="col-lg-2">
                        <div class="card-box">
                            <div class="container">
                                <div class="row">
                                    <div class="col">
                                        <img src="https://cdn.badblock.fr/head/{{ $Player['name'] }}/110.png">
                                    </div>
                                    @if($Player['online'] == true)
                                        <h2>Online</h2>
                                    @else
                                        <h2>Offline</h2>
                                    @endif
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="card-box">
                            <div class="container">
                                <div class="row">
                                    <div class="col">
                                        <h4>
                                            <li>
                                                Nom : {{ $Player['name'] }}
                                            </li>
                                            @if(isset($Player['realName']))
                                                <li>
                                                    Nom réel : {{ $Player['realName'] }}
                                                </li>
                                            @endif
                                            <li>
                                                Adresse IP : {{ $Player['lastIp'] }}
                                            </li>
                                            <li>
                                                Groupe principal : {{ $Player['permissions']['group'] }}
                                            </li>
                                            <li>
                                                Durée du group principal : {{ $Player['permissions']['end'] }}
                                            </li>
                                            <li>
                                                Groupe(s) secondaire(s) :
                                                <ul>
                                                    @foreach($Player['permissions']['alternateGroups'] as $k => $row)
                                                        <li>
                                                            Groupe : {{ $k }} Time : {{ $row }}
                                                        </li>
                                                    @endforeach
                                                </ul>
                                            </li>
                                        </h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="card-box">
                            <div class="container">
                                <div class="row">
                                    <button type="button" class="btn btn-danger btn-lg" onclick="resetPassword()">Reset Password</button>
                                    <button type="button" class="btn btn-warning btn-lg" onclick="resetTfa()">Reset TFA</button>
                                    <button type="button" class="btn btn-info btn-lg" onclick="resetOm()" >Offline Mode</button>
                                    <button type="button" class="btn btn-sucess btn-lg" onclick="resetOl()" >Online Mode</button>
                                </div>
                            </div>
                        </div>

                        <div class="card-box">
                            <div class="container">
                                <div class="row">
                                    <button type="button" class="btn btn-danger btn-lg">Un ban</button>
                                    <button type="button" class="btn btn-warning btn-lg">Un mute</button>
                                    <button type="button" class="btn btn-info btn-lg">Kick</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="card-box">
                            <div class="row">
                                <div class="col-xl-12">
                                    <ul class="nav nav-tabs">
                                        <li class="nav-item">
                                            <a href="#1" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Skin
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#2" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Information G.
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#3" data-toggle="tab" aria-expanded="true" class="nav-link">
                                                Casier
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#4" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Logs Guardian
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#5" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Achats
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#6" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Paiements
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#7" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Groupes & Permissions
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#8" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Authentification
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#9" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Logs
                                            </a>
                                        </li>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade" id="1">
                                            <img src="https://cdn.badblock.fr/head/{{ $Player['name'] }}/110.png">
                                        </div>
                                        <div role="tabpanel" class="tab-pane fade" id="2">
                                            <div class="container">
                                                <ul>
                                                    <li>
                                                        Nom : {{ $Player['name'] }}
                                                    </li>
                                                    @if(isset($Player['realName']))
                                                        <li>
                                                            Nom réel : {{ $Player['realName'] }}
                                                        </li>
                                                    @endif
                                                    <li>
                                                        Adresse IP : {{ $Player['lastIp'] }}
                                                    </li>
                                                    <li>
                                                        Groupe principal : {{ $Player['permissions']['group'] }}
                                                    </li>
                                                    <li>
                                                        Durée du group principal : {{ $Player['permissions']['end'] }}
                                                    </li>
                                                    <li>
                                                        Groupe(s) secondaire(s) :
                                                        <ul>
                                                            @foreach($Player['permissions']['alternateGroups'] as $k => $row)
                                                                <li>
                                                                    Groupe : {{ $k }} Time : {{ $row }}
                                                                </li>
                                                            @endforeach
                                                        </ul>
                                                    </li>
                                                </ul>
                                                <ul>
                                                    <li>
                                                        Niveau : {{ $Player['game']['level'] }}
                                                    </li>
                                                    <li>
                                                        Xp : {{ $Player['game']['xp'] }}
                                                    </li>
                                                    <li>
                                                        Badcoins : {{ $Player['game']['badcoins'] }}
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>

                                        <div role="tabpanel" class="tab-pane fade" id="5">
                                            <h4 class="header-title m-t-0 m-b-30">Achat(s) du joueur</h4>
                                            <table class="table">
                                                <thead class="thead-light">
                                                <tr>
                                                    <th>Date</th>
                                                    <th>Offre</th>
                                                    <th>Points</th>
                                                    <th>Ingame</th>
                                                    <th>Actions</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                @foreach($Player['buy'] as $row)
                                                    <tr>
                                                        <th scope="row">{{ $row['date'] }}</th>
                                                        <td>{{ $row['name'] }}</td>
                                                        <td>{{ $row['price'] }}</td>
                                                        <td>
                                                            @if($row['ingame'])
                                                                <i class="fa fa-check"></i>
                                                            @else
                                                                <i class="fa fa-remove"></i>
                                                            @endif
                                                        </td>
                                                        <td>
                                                        </td>
                                                    </tr>
                                                @endforeach
                                                </tbody>
                                            </table>
                                        </div>

                                        <div role="tabpanel" class="tab-pane fade" id="6">
                                            <div class="card-box">
                                                <h4 class="header-title m-t-0 m-b-30">Rechargement(s) du joueur</h4>
                                                <table class="table">
                                                    <thead class="thead-light">
                                                    <tr>
                                                        <th>Date</th>
                                                        <th>Gateway</th>
                                                        <th>Prix</th>
                                                        <th>Points</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    @foreach($Player['funds'] as $row)
                                                        <tr>
                                                            <th scope="row">{{ $row['date'] }}</th>
                                                            <td>{{ $row['gateway'] }}</td>
                                                            <td>{{ $row['price'] }}</td>
                                                            <td>{{ $row['points'] }}</td>
                                                            <td>
                                                                <a target="_blank" href="https://badblock.fr/dashboard/facture/{{ $row['_id'] }}" class="btn btn-icon waves-effect waves-light btn-info m-b-5">
                                                                    <i class="fa fa-eye"></i>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    @endforeach
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>

                                        <div role="tabpanel" class="tab-pane fade" id="7">



                                        </div>
                                        <div role="tabpanel" class="tab-pane fade" id="9">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Date</th>
                                                    <th>Utilisateur</th>
                                                    <th>Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                @foreach($Logs as $Log)
                                                    <tr
                                                    @if($Log['action'] == "Actived online mode")
                                                    class="bg-success text-white"
                                                    @elseif($Log['action'] == "Actived offline mode")
                                                    class="bg-warning text-white"
                                                    @else
                                                    class="bg-danger text-white"
                                                    @endif
                                                    >
                                                        <td>{{ $Log['date'] }}</td>
                                                        <td>{{ $Log['user'] }}</td>
                                                        <td>{{ $Log['action'] }}</td>
                                                    </tr>
                                                @endforeach
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div><!-- end col -->
                            </div>
                        </div>
                    </div><!-- end col -->
                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')
    <script src="/assets/plugins/toastr/toastr.min.js"></script>

    <script>
        
        function resetPassword() {
            $.ajax({
                type: "POST",
                url: "/profile-api/{{ $Player['uniqueId'] }}/resetpassword",
                success:function(data)
                {
                    toastr.success('Succès !', "Le mot de passe à bien était reset !");
                    console.log('Valider !');
                },
                error:function(data)
                {
                    toastr.error('Erreur !', 'Un problème s\'est produit ou vos permissions sont insuffisantes !');
                    console.log('Erreur !');
                }
            });
        }
        
        function resetTfa() {
            $.ajax({
                type: "POST",
                url: "/profile-api/{{ $Player['uniqueId'] }}/resettfa",
                success:function(data)
                {
                    toastr.success('Succès !', "La TFA est bien désactivée !");
                    console.log('Valider !');
                },
                error:function(data)
                {
                    toastr.error('Erreur !', 'Un problème s\'est produit ou vos permissions sont insuffisantes !');
                    console.log('Erreur !');
                }
            });
        }
        
        function resetOm() {
            $.ajax({
                type: "POST",
                url: "/profile-api/{{ $Player['uniqueId'] }}/resetom",
                success:function(data)
                {
                    toastr.success('Succès !', "L'offline mode est activé !");
                    console.log('Valider !');
                },
                error:function(data)
                {
                    toastr.error('Erreur !', 'Un problème s\'est produit ou vos permissions sont insuffisantes !');
                    console.log('Erreur !');
                }
            });
        }

        function resetOl() {
            $.ajax({
                type: "POST",
                url: "/profile-api/{{ $Player['uniqueId'] }}/resetol",
                success:function(data)
                {
                    toastr.success('Succès !', "L'online est activé !");
                    console.log('Valider !');
                },
                error:function(data)
                {
                    toastr.error('Erreur !', 'Un problème s\'est produit ou vos permissions sont insuffisantes !');
                    console.log('Erreur !');
                }
            });
        }

        
        
    </script>
    

@endsection

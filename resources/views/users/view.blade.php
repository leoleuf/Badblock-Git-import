@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="row">
                    <br>
                    <div class="col-lg-6">
                        <div class="card-box">
                            <div class="container">
                                <div class="row">
                                    <div class="col">
                                        <img src="https://cdn.badblock.fr/head/{{ $Player['name'] }}/110.png">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="card-box">
                            <div class="row">
                                <div class="col-xl-6">

                                    <ul class="nav nav-tabs">
                                        <li class="nav-item">
                                            <a href="#0" data-toggle="tab" aria-expanded="false" class="nav-link active show">
                                                Skin
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#1" data-toggle="tab" aria-expanded="false" class="nav-link active show">
                                                Statistiques
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#2" data-toggle="tab" aria-expanded="true" class="nav-link">
                                                Casier
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#3" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Logs Guardian
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#4" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Achats
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#5" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Paiements
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#6" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Groupes & Permissions
                                            </a>
                                        </li>
                                        <li class="nav-item">
                                            <a href="#7" data-toggle="tab" aria-expanded="false" class="nav-link">
                                                Authentification
                                            </a>
                                        </li>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade active show" id="0">
                                            <div class="col">
                                                <img src="https://cdn.badblock.fr/head/{{ $Player['name'] }}/110.png">
                                            </div>
                                        </div>
                                        <div role="tabpanel" class="tab-pane fade active show" id="1">
                                            <div class="container">
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
                                        <div role="tabpanel" class="tab-pane fade" id="6">
                                                <h3>Informations générales :</h3>
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
                                        </div>
                                        <div role="tabpanel" class="tab-pane fade" id="7">
                                            <h3>Options générales</h3>
                                            <div class="container">
                                                <form action="#" method="POST">
                                                    {{ csrf_field() }}
                                                    <div class="row">
                                                        <h5>Changer le mot de passe :</h5>
                                                        <input type="password" name="password" class="form-control" placeholder="******">
                                                        <h5>Mode premium :</h5>
                                                        <select name="onlinemode" class="custom-select mt-3">
                                                            <option selected="">Selectionné le mode</option>
                                                            <option value="true">Mode premium</option>
                                                            <option value="false">Mode cracké</option>
                                                        </select>
                                                        <h5>Double authentification : (Vider pour supprimer)</h5>
                                                        @if(isset($Player['authKey']))
                                                            <input type="text" name="authKey" class="form-control" placeholder="******" value="{{ $Player['authKey'] }}">
                                                        @else
                                                            <input type="text" name="authKey" class="form-control" placeholder="******" value="">
                                                        @endif
                                                        <br>
                                                        <div class="col-auto center-block">
                                                            <center><button type="submit" class="btn btn-primary mb-2">Valider</button></center>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
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

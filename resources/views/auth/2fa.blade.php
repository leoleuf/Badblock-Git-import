@extends('layouts.app')
@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-default">
                    <div class="panel-heading"><strong>Two Factor Authentication</strong></div>
                    <div class="panel-body">
                        <p>L'Authentification à Deux Facteurs (A2F) sécurise l'accès à votre compte en y ajoutant une deuxième méthode d'authentification
                        (aussi appelés facteurs) afin de vérifier votre identité. Deux facteurs d'authentification protègent votre compte des menaces comme le pishing,
                        le social engineering or les attaques par force brute et sécurisent vos identifiants des attaquants exploitant des identifiants peu sécurisés ou volés.</p>
                        <br/>
                        <p>Pour activer l'A2F, veuillez suivre ces quelques étapes.</p>
                        <strong>
                            <ol>
                                <li>Cliquez sur Générer une clé d'authentification à deux facteur afin de vous générer un QR code unique</li>
                                <li>Vérifer le code PIN depuis l'application mobile <a href="https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en">Google Authenticator</a></li>
                            </ol>
                        </strong>
                        <br/>
                        @if (session('error'))
                            <div class="alert alert-danger">
                                {{ session('error') }}
                            </div>
                        @endif
                        @if (session('success'))
                            <div class="alert alert-success">
                                {{ session('success') }}
                            </div>
                        @endif
                        @if(!Auth::user()->PasswordSecurity)
                            <form class="form-horizontal" method="POST" action="{{ route('generate2faSecret') }}">
                                {{ csrf_field() }}
                                <div class="form-group">
                                    <div class="col-md-6 col-md-offset-4">
                                        <button type="submit" class="btn btn-primary">
                                            Générer une clé d'authentification à deux facteurs
                                        </button>
                                    </div>
                                </div>
                            </form>
                        @elseif(!$data['user']->passwordSecurity->google2fa_enable)
                            <strong>1. Scannez ce QR code dans Google Authenticator :</strong><br/>
                            <img src="{{$data['google2fa_url'] }}" alt="">
                            <br/><br/>
                            <strong>2.Entrez le code PIN de Google Authenticator</strong><br/><br/>
                            <form class="form-horizontal" method="POST" action="{{ route('enable2fa') }}">
                                {{ csrf_field() }}
                                <div class="form-group{{ $errors->has('verify-code') ? ' has-error' : '' }}">
                                    <label for="verify-code" class="col-md-4 control-label">Code PIN :</label>
                                    <div class="col-md-6">
                                        <input id="verify-code" type="password" class="form-control" name="verify-code"
                                               required>
                                        @if ($errors->has('verify-code'))
                                            <span class="help-block">
                                                <strong>{{ $errors->first('verify-code') }}</strong>
                                            </span>
                                        @endif
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-md-6 col-md-offset-4">
                                        <button type="submit" class="btn btn-primary">
                                            Activer l'A2F
                                        </button>
                                    </div>
                                </div>
                            </form>
                        @elseif($data['user']->passwordSecurity->google2fa_enable)
                            <div class="alert alert-success">
                                l'A2F est <strong>ACTIVÉE</strong> sur votre compte.
                            </div>
                        <!--
                            <p>Si vous voulez désactiver l'A2F, entrez votre mot de passe et cliquez sur Désactiver l'A2F.</p>
                            <form class="form-horizontal" method="POST" action="{{ route('disable2fa') }}">
                                <div class="form-group{{ $errors->has('current-password') ? ' has-error' : '' }}">
                                    <label for="change-password" class="col-md-4 control-label">Mot de passe</label>
                                    <div class="col-md-6">
                                        <input id="current-password" type="password" class="form-control"
                                               name="current-password" required>
                                        @if ($errors->has('current-password'))
                                            <span class="help-block">
<strong>{{ $errors->first('current-password') }}</strong>
</span>
                                        @endif
                                    </div>
                                </div>
                                <div class="col-md-6 col-md-offset-5">
                                    {{ csrf_field() }}
                                    <button type="button" class="btn btn-primary ">Désactiver l'A2F</button>
                                </div>
                            </form>-->
                            @endif

                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
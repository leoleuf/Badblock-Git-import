@extends('layouts.app')
@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-12 col-md-offset-2">
                <div class="card-box">
                    <div class="panel panel-default">
                        <div class="panel-heading">Authentification à deux facteurs</div>
                        <div class="panel-body">
                            <p>L'Authentification à Deux Facteurs (A2F) sécurise l'accès à votre compte en y ajoutant une deuxième méthode d'authentification
                                (aussi appelés facteurs) afin de vérifier votre identité. Deux facteurs d'authentification protègent votre compte des menaces comme le pishing,
                                le social engineering or les attaques par force brute et sécurisent vos identifiants des attaquants exploitant des identifiants peu sécurisés ou volés.</p>
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
                            <strong>Entrez le code PIN généré par Google Authenticator</strong><br/><br/>
                            <form class="form-horizontal" action="{{ route('2faVerify') }}" method="POST">
                                {{ csrf_field() }}
                                <div class="form-group{{ $errors->has('one_time_password-code') ? ' has-error' : '' }}">
                                    <label for="one_time_password" class="col-md-4 control-label">Code PIN</label>
                                    <div class="col-md-6">
                                        <input name="one_time_password" class="form-control"  type="text"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-md-6 col-md-offset-4">
                                        <button class="btn btn-primary" type="submit">S'authentifier</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
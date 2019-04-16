@section('title', 'Inscription | Serveur-Multigames.net')
@section('description', 'Inscrivez-vous sur Serveur-Multigames.net pour noter des serveurs, ajouter et modifier votre serveur, et bien plus encore !')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/register')
@section('jquery', 'async defer')
@extends('front.index')

@section('content')

    <script src='https://www.google.com/recaptcha/api.js'></script>

    <script>
        function onSubmit(token) {
            document.getElementById("vote-form").submit();
        }
    </script>

    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>S'enregistrer</h2>
                        <p class="mb-0">Enregistrez-vous dès maintenant</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">M'inscrire</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-9 mb-5 mb-lg-0">

                    <div class="card lis-brd-light wow fadeInUp" style="visibility: visible; animation-name: fadeInUp;">
                        <form method="post" action="{{ route('register') }}" class="card-body p-0">

                            @csrf

                            <div class="row p-4">

                                <div class="col-12 col-md-3">
                                    <div class="form-group lis-relative">
                                        <h6 class="lis-font-weight-500">Déjà inscrit ?</h6>
                                    </div>
                                </div>

                                <div class="col-12 col-md-9">
                                    <div class="form-group lis-relative">
                                        <a title="Se connecter" href="/login">Connectez-vous</a> dès maintenant si vous êtes déjà inscrit, afin d'éviter de créer d'autres comptes.
                                    </div>
                                </div>
                            </div>

                            <hr />

                            <div class="row p-4">
                                <div class="col-12 col-sm-12 mb-3">
                                    <h6 class="lis-font-weight-500"><i class="fa fa-info-circle pr-2 lis-f-14"></i> Détails d'inscription</h6>
                                </div>

                                <div class="col-12 col-sm-12">
                                    @if ($errors->has('name'))
                                        <div class="alert alert-danger" role="alert">
                                            {{ $errors->first('name') }}
                                        </div>
                                    @endif

                                    @if ($errors->has('email'))
                                        <div class="alert alert-danger" role="alert">
                                            {{ $errors->first('email') }}
                                        </div>
                                    @endif

                                        @if ($errors->has('password'))
                                            <div class="alert alert-danger" role="alert">
                                                {{ $errors->first('password') }}
                                            </div>
                                        @endif

                                    <div class="form-group row">
                                        <label for="name" class="col-12 col-md-3 col-form-label">Pseudonyme</label>
                                        <div class="col-12 col-md-9">
                                            <input class="form-control border-top-0 border-left-0 border-right-0 rounded-0" name="name" value="{{ old('name') }}" placeholder="Saisissez un pseudonyme" type="text" required autofocus>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="email" class="col-12 col-md-3 col-form-label">Email</label>
                                        <div class="col-12 col-md-9">
                                            <input class="form-control border-top-0 border-left-0 border-right-0 rounded-0" name="email" value="{{ old('email') }}" placeholder="Saisissez un e-mail" type="email" required>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="password" class="col-12 col-md-3 col-form-label">Mot de passe</label>
                                        <div class="col-12 col-md-9">
                                            <input class="form-control border-top-0 border-left-0 border-right-0 rounded-0" type="password" name="password" required>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="password_confirm" class="col-12 col-md-3 col-form-label">Confirmer votre mot de passe</label>
                                        <div class="col-12 col-md-9">
                                            <input class="form-control border-top-0 border-left-0 border-right-0 rounded-0" type="password" name="password_confirmation" required>
                                        </div>
                                    </div>

                                </div>
                                <div class="col-12 col-sm-12">
                                    <button class="btn btn-primary" class="btn btn-primary" class="g-recaptcha"
                                            data-sitekey="6Lf8amQUAAAAAM2wJE-R24huo1IDSTgDQZVoURX1"
                                            data-callback="onSubmit">
                                        Je m'inscris !
                                    </button>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>

            </div>
        </div>
    </section>

@endsection

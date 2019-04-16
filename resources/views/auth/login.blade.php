@section('title', 'Se connecter | Serveur-Multigames.net')
@section('description', 'Connectez-vous sur Serveur-Multigames.net pour noter des serveurs, ajouter et modifier votre serveur et plus encore !')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/login')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')


    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Se connecter</h2>
                        <p class="mb-0">Connectez-vous dès maintenant</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Me connecter</li>
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
                        <form method="post" action="{{ route('login') }}" class="card-body p-0">

                            <div class="row p-4">

                                <div class="col-12 col-md-3">
                                    <div class="form-group lis-relative">
                                        <h6 class="lis-font-weight-500">Pas inscrit ?</h6>
                                    </div>
                                </div>

                                <div class="col-12 col-md-9">
                                    <div class="form-group lis-relative">
                                        <a title="S'enregistrer" href="/register">Enregistrez-vous</a> dès maintenant si vous n'êtes déjà pas inscrit.
                                    </div>
                                </div>
                            </div>

                            <hr />

                            <div class="row p-4">
                                <div class="col-12 col-sm-12 mb-3">
                                    <h6 class="lis-font-weight-500"><i class="fa fa-info-circle pr-2 lis-f-14"></i> Détails de connexion</h6>
                                </div>


                                    <div class="col-12 col-sm-12">
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
                                        <label for="name" class="col-12 col-md-3 col-form-label">Adresse e-mail</label>
                                        <div class="col-12 col-md-9">
                                            <input class="form-control border-top-0 border-left-0 border-right-0 rounded-0" name="email" value="{{ old('email') }}" placeholder="Saisissez un email" type="email" required autofocus>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="name" class="col-12 col-md-3 col-form-label">Mot de passe</label>
                                        <div class="col-12 col-md-9">
                                            <input class="form-control border-top-0 border-left-0 border-right-0 rounded-0" name="password" value="" placeholder="Saisissez un mot de passe" type="password" required>
                                        </div>
                                    </div>
                                </div>

                                        {{ csrf_field() }}

                                    <div class="col-12 col-sm-12">
                                        <input type="submit" class="btn btn-primary" name="submit" value="Se connecter" />
                                    </div>
                            </div>
                        </form>
                        </div>
                    </div>
            </div>
        </div>
    </section>
@endsection

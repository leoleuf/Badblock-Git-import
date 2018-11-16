@section('title', 'Se connecter | Serveur-Multigames.net')
@section('description', 'Connectez-vous sur Serveur-Multigames.net pour noter des serveurs, ajouter et modifier votre serveur et plus encore !')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/login')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')

    <!-- start banner Area -->
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Connexion
                    </h1>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Connexion Serveur MultiGames" href="/login">Connexion</a></p>
                </div>
            </div>
        </div>
    </section>
    <!-- End banner Area -->

    <!-- Start feature-cat Area -->
    <section class="post-area section-gap">
        <div class="container">
            <div class="row justify-content-center d-flex">
                <div class="col-lg-4 post-list">
                    <div class="single-post d-flex flex-row center">
                                    <form method="POST" action="{{ route('login') }}" aria-label="{{ __('Login') }}">
                                        @csrf

                                        <div class="form-group">
                                            <label for="email" class="col-sm-8 col-form-label ">{{ __('Adresse e-mail') }}</label>

                                            <div class="col-md-12">
                                                <input id="email" type="email" class="form-control{{ $errors->has('email') ? ' is-invalid' : '' }}" name="email" value="{{ old('email') }}" required autofocus>

                                                @if ($errors->has('email'))
                                                    <span class="invalid-feedback" role="alert">
                                        <strong>{{ $errors->first('email') }}</strong>
                                    </span>
                                                @endif
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="password" class="col-md-8 col-form-label">{{ __('Mot de passe') }}</label>

                                            <div class="col-md-12">
                                                <input id="password" type="password" class="form-control{{ $errors->has('password') ? ' is-invalid' : '' }}" name="password" required>

                                                @if ($errors->has('password'))
                                                    <span class="invalid-feedback" role="alert">
                                        <strong>{{ $errors->first('password') }}</strong>
                                    </span>
                                                @endif
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-md-12">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="checkbox" name="remember" id="remember" {{ old('remember') ? 'checked' : '' }}>

                                                    <label class="form-check-label" for="remember">
                                                        {{ __('Se souvenir de moi') }}
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <br />
                                        <div class="form-group">
                                            <div class="col-md-12">
                                                <button type="submit" class="btn btn-primary">
                                                    {{ __('Se connecter') }}
                                                </button>
                                                <br />
                                                <a title="S'inscrire sur Serveur MultiGames" class="btn btn-link" href="/register">
                                                    Pas inscrit ? Je m'inscris !
                                                </a>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
    </section>
@endsection

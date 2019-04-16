@section('title', 'Mettre en avant son serveur de jeu - Serveur MultiGames')
@section('description', 'Mettez en avant votre serveur de jeu, donnez lui de la visibilité en utilisant notre outil de publicité pour vous afficher en première position sur nos classements de serveurs de jeux.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/mise-en-avant')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')

    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Mettre en avant mon serveur</h2>
                        <p class="mb-0">Envie de mettre en avant votre serveur sans plus attendre afin d'attirer la communauté d'un jeu et devenir populaire ? Découvrez comment.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Mise en avant</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>
    <section class="submit-area section-gap">
        <div class="container">
            <div class="section-top-border">
                <div class="row">
                    <div class="col-md-3">
                        <img alt="Mise en avant" src="img/grow.png" class="img-fluid">
                    </div>
                    <div class="col-md-9 mt-sm-20">
                        <p>Envie de <strong>gagner plus de visibilité</strong> et plus de joueurs sur ton serveur de jeu privé ? Avec Serveur MultiGames, il est possible de mettre en avant ton serveur en quelques clics seulement depuis le Tableau de Bord afin d'intéresser la communauté du jeu et de la rediriger vers ton serveur pour le <strong>faire connaître un maximum</strong> en seulement quelques étapes. <strong>Prends la première place</strong> sans plus hésiter.<br />
                            <br />
                            @if(Auth::user())
                                <a title="Mettre en avant mon serveur" href="/dashboard/mise-en-avant" class="btn btn-primary btn-lg"><i class="fa fa-diamond" aria-hidden="true"></i> Mettre en avant mon serveur</a>
                            @else
                                <a title="Mettre en avant mon serveur" href="/login" class="btn btn-primary btn-lg"><i class="fa fa-diamond" aria-hidden="true"></i> Mettre en avant mon serveur</a>
                            @endif
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <div class="row justify-content-center align-items-center">
                <div class="col-lg-3 feat-img no-padding">
                    <img alt="Mettre en avant son serveur" class="img-fluid" src="img/pages/f1.jpg">
                </div>
                <div class="col-lg-3 no-padding feat-txt">
                    <h6 class="text-uppercase text-white">AVANTAGES DE LA MISE EN AVANT</h6>
                    <h2>FAITES VOUS CONNAÎTRE</h2>
                    <p>Vous souhaitez avoir plus de visibilité ?<br /><br />Mettre en avant son serveur sur Serveur MultiGames est le meilleur moyen d'assurer votre visibilité sur le court comme sur le long terme.
                    </p>
                </div>
                <div class="col-lg-3 feat-img no-padding">
                    <img alt="Visibilité pour votre serveur de jeu" class="img-fluid" src="img/pages/f2.jpg">
                </div>
                <div class="col-lg-3 no-padding feat-txt">
                    <h6 class="text-uppercase text-white">VOTRE IMAGE EN TANT QUE SERVEUR</h6>
                    <h2>PRENEZ LA PREMIERE PLACE</h2>
                    <p>
                        Nos classements sont consultés par plus <br />de 5 000 utilisateurs tous les jours, qui recherchent le serveur qui leur<br />correspond.<br /><br />Donnez de la visibilité à votre serveur dès maintenant.
                    </p>
                </div>
            </div>
        </div>
    </section>

@endsection
@section('title', 'Mettre en avant son serveur de jeu - Serveur MultiGames')
@section('description', 'Mettez en avant votre serveur de jeu, donnez lui de la visibilité en utilisant notre outil de publicité pour vous afficher en première position sur nos classements de serveurs de jeux.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/mise-en-avant')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Mettre en avant mon serveur
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2>
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Mettre en avant son serveur MultiGames" href="/mise-en-avant">Mettre en avant son serveur</a></p>
                </div>
            </div>
        </div>
    </section>

    <section class="submit-area section-gap">
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <div class="submit-left">
                        <h4>Inscrivez votre serveur de jeu</h4>
                        <p>
                            Inscrivez votre serveur de jeu sur notre classement dans un premier temps.
                        </p>
                        <a title="Inscrire un serveur de jeu sur le classement" href="/dashboard/add-server" class="primary-btn header-btn">Inscrire mon serveur de jeu</a>
                    </div>
                </div>
                <div class="col-lg-6 ">
                    <div class="submit-right">
                        <h4>Mettez en avant votre serveur</h4>
                        <p>
                            Mettez en avant votre serveur et prenez la première position dans nos classements depuis le Tableau de Bord.
                        </p>
                        <a title="Mettre en avant un serveur de jeu sur le classement" href="/dashboard/mise-en-avant" class="primary-btn header-btn">Mettre en avant mon serveur</a>
                    </div>
                </div>

            </div>
        </div>
    </section>

    <section class="feature-area">
        <div class="container-fluid">
            <div class="row justify-content-center align-items-center">
                <div class="col-lg-3 feat-img no-padding">
                    <img alt="Mettre en avant son serveur" class="img-fluid" src="img/pages/f1.jpg">
                </div>
                <div class="col-lg-3 no-padding feat-txt">
                    <h6 class="text-uppercase text-white">AVANTAGES DE LA MISE EN AVANT</h6>
                    <h1>FAITES VOUS CONNAÎTRE</h1>
                    <p>Vous souhaitez avoir plus de visibilité ?<br /><br />Mettre en avant son serveur sur Serveur MultiGames est le meilleur moyen d'assurer votre visibilité sur le court comme sur le long terme.
                    </p>
                </div>
                <div class="col-lg-3 feat-img no-padding">
                    <img alt="Visibilité pour votre serveur de jeu" class="img-fluid" src="img/pages/f2.jpg">
                </div>
                <div class="col-lg-3 no-padding feat-txt">
                    <h6 class="text-uppercase text-white">VOTRE IMAGE EN TANT QUE SERVEUR</h6>
                    <h1>PRENEZ LA PREMIERE PLACE</h1>
                    <p>
                        Nos classements sont consultés par plus <br />de 5 000 utilisateurs tous les jours, qui recherchent le serveur qui leur<br />correspond.<br /><br />Donnez de la visibilité à votre serveur dès maintenant.
                    </p>
                </div>
            </div>
        </div>
    </section>

@endsection
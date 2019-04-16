@section('title', 'Installation de la méthode VOTIFIER - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode VOTIFIER pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/votifier')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Méthode Votifier</h2>
                        <p class="mb-0">Initiation à la méthode Votifier, simple.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none"><a title="API et Utilisation" href="/api" class="lis-light">API et Utilisation</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Votifier</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation du plugin Votifier pour les Serveurs Minecraft (et du reste, au protocol version 1) pour vérifier les votes directement sur son serveur.
                            D'autres méthodes existent, mais se révèlent plus techniques.</p>
                        <blockquote class="generic-blockquote">
                            <h6>Informations requises pour la méthode VOTIFIER</h6>
                            <i>IP Votifier</i> : IP du serveur à entrer<br />
                            <i>Port Votifier</i> : Port de Votifier (<span style="color: red;">et non du serveur</span>), &nbsp;dans la configuration du plugin Votifier<br />
                            <i>Clé publique Votifier</i> : C'est le contenu du fichier public.key dans le dossier plugins/Votifier/rsa/public.key, à copier-coller dans le panel<br />
                            <br />
                            <span style="color: red;">Attention : Cette méthode ne fonctionne qu'avec les Serveurs Minecraft. Le plugin Votifier doit être installé et activé sur le serveur</span><br /><br />

                            <a rel="nofollow noopener noreferrer" title="Télécharger Votifier" class="btn btn-success btn-default" href="https://dev.bukkit.org/projects/votifier" target="_blank">
                                <i class="fa fa-download"></i> Télécharger le plugin Votifier
                            </a>
                        </blockquote>
                    </div>
                </div>
        </div>
    </section>
@endsection
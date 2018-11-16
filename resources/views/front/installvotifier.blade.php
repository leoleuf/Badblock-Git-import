@section('title', 'Installation de la méthode VOTIFIER - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode VOTIFIER pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/votifier')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Installation VOTIFIER
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a> <span class="lnr lnr-arrow-right"></span> <a title="Ajouter mon serveur" href="/add-server">Ajouter mon serveur</a> <span class="lnr lnr-arrow-right"></span> <a title="Installation de la méthode VOTIFIER" href="/add-server/votifier">Installation de la méthode VOTIFIER</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Installation de la méthode VOTIFIER (Serveur Minecraft)</h3>
                <div class="row">
                    <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation du plugin Votifier pour les Serveurs Minecraft (et du reste, au protocol version 1) pour vérifier les votes directement sur son serveur.
                            D'autres méthodes existent, mais se révèlent plus techniques.</p>
                        <blockquote class="generic-blockquote">
                            <strong>Informations requises pour la méthode VOTIFIER</strong><br /><br />
                            <i>IP Votifier</i> : IP du serveur à entrer<br />
                            <i>Port Votifier</i> : Port de Votifier (<span style="color: red;">et non du serveur</span>), &nbsp;dans la configuration du plugin Votifier<br />
                            <i>Clé publique Votifier</i> : C'est le contenu du fichier public.key dans le dossier plugins/Votifier/rsa/public.key, à copier-coller dans le panel<br />
                            <br />
                            <span style="color: red;">Attention : Cette méthode ne fonctionne qu'avec les Serveurs Minecraft. Le plugin Votifier doit être installé et activé sur le serveur</span><br /><br />

                            <a rel="nofollow noopener noreferrer" title="Télécharger Votifier" class="genric-btn info" href="https://dev.bukkit.org/projects/votifier" target="_blank">Télécharger le plugin Votifier</a>
                        </blockquote>
                    </div>
                </div>
            </div>
            <div class="section-top-border">
            </div>
        </div>
    </div>
@endsection
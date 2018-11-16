@section('title', 'Installation de la méthode CALLBACK - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode CALLBACK pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/callback')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Installation CALLBACK
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a> <span class="lnr lnr-arrow-right"></span> <a title="Ajouter mon serveur" href="/add-server">Ajouter mon serveur</a> <span class="lnr lnr-arrow-right"></span> <a title="Installation de la méthode CALLBACK" href="/add-server/callback">Installation de la méthode CALLBACK</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Installation de la méthode CALLBACK</h3>
                <div class="row">
                    <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation de la méthode CALLBACK pour vérifier les votes. Si vous ne savez pas comment implémenter cette méthode, utilisez plutôt la <a title="Installation par la méthode TRUE" href="/add-server/true">méthode TRUE</a>.
                            Lorsqu'un joueur vote pour votre serveur, vous receverez une requête sur le lien configuré pour le serveur, avec des paramètres que vous pourrez réutiliser
                            pour donner une récompense à un joueur ou enregistrer le vote par exemple.</p>
                        <blockquote class="generic-blockquote">
                            <strong>Informations reçues par la méthode GET</strong><br /><br />
                            <i>ip</i> : contenant l'adresse IP de l'utilisateur ayant voté<br />
                            <i>servername</i> : nom du serveur encodé ('Téstà 123' deviendra 'testa-123')<br />
                            <i>status</i> : status du vote (1 seul disponible pour la méthode callback : SUCCESS), indiquant que le joueur a bien voté<br />
                            <i>nextvote</i> : nombre de secondes avant le prochain vote du joueur<br /><br />
                            <strong>Exemple de code de récupération</strong><br />
                            <span style="color: red;">Action très recommandée : Vérifiez l'IP qui envoie la requête afin d'éviter les usurpations de vote.</span><br /><br />
                            &lt;?php<br /><br />

                            $ip = $_GET['ip'];<br>
                            $servername = $_GET['servername'];<br>
                            $status = $_GET['status'];<br>
                            $nextvote = $_GET['nextvote'];<br><br />
                            if ($status == "SUCCESS")<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote valide, donner le lot<br>
                            }<br><br />
                            ?&gt;<br /><br />
                            <strong>Exemple de réponse</strong><br /><br />
                            <i>ip</i> : 127.0.0.1<br />
                            <i>servername</i> : testa-123<br />
                            <i>status</i> : SUCCESS<br />
                            <i>nextvote</i> : 5400
                        </blockquote>
                    </div>
                </div>
            </div>
            <div class="section-top-border">
            </div>
        </div>
    </div>
@endsection
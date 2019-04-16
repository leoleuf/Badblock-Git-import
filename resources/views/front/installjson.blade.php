@section('title', 'Installation de la méthode JSON - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode JSON pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/json')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Méthode Json</h2>
                        <p class="mb-0">Initiation à la méthode Json, intermédiaire.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none"><a title="API et Utilisation" href="/api" class="lis-light">API et Utilisation</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Json</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                        <p><strong>Serveur MultiGames</strong> propose gratuitement l'utilisation de la méthode JSON pour vérifier les votes. Si vous ne savez pas comment implémenter cette méthode, utilisez plutôt la <a title="Installation par la méthode TRUE" href="/add-server/true">méthode TRUE</a>.
                            Lorsque vous enverrez une requête à notre API avec la méthode <strong>JSON</strong>, vous receverez une réponse formattée qui vous indiquera le temps avant le prochain vote d'une IP et si cette personne a voté ou non. Cette méthode est utile pour récupérer le temps avant le prochain vote d'un joueur.</p>
                        <blockquote class="generic-blockquote">
                            <strong>Exemple d'implémentation en PHP</strong><br /><br />
                            <kbd>&nbsp;&lt;?php&nbsp;<br>
                                &nbsp;&nbsp;&nbsp;$SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                                &nbsp;&nbsp;&nbsp;$IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant<br>
                                &nbsp;&nbsp;&nbsp;$SM = "https://serveur-multigames.net/api/$SERVER_ID/?ip=$IP";<br>
                                &nbsp;&nbsp;&nbsp;$result = @file_get_contents($SM);<br>
                                &nbsp;&nbsp;&nbsp;$result = json_decode($result);<br>
                                &nbsp;&nbsp;&nbsp;if ($result->status == "SUCCESS")<br>
                                &nbsp;&nbsp;&nbsp;{<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Vote valide<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$nextvote = $result->nextvote; // Temps restant possible avant le prochain vote<br />
                                &nbsp;&nbsp;&nbsp;}<br>
                                &nbsp;&nbsp;&nbsp;else<br>
                                &nbsp;&nbsp;&nbsp;{<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Déjà voté ou vote non valide<br>
                                &nbsp;&nbsp;&nbsp;}<br>
                                &nbsp;&nbsp;&nbsp;?&gt;
                            </kbd><br /><br />
                            <h5>Clés et valeurs retournées</h5>
                            <i>ip</i> : toutes les IPv4/IPv6 (exemple: 127.0.0.1)<br />
                            <i>status</i> : SUCCESS (vote effectué) ou WAIT (vote non effectué ou déjà effectué)<br />
                            <i>nextvote</i> : temps en secondes avant le prochain vote (-1 si jamais voté)
                            <br /><br />
                            <h6>Exemple de réponse</h6>
                            {<br />
                            &nbsp;&nbsp;&nbsp;&nbsp;"ip": "127.0.0.1",<br />
                            &nbsp;&nbsp;&nbsp;&nbsp;"status": "SUCCESS",<br />
                            &nbsp;&nbsp;&nbsp;&nbsp;"nextvote": 3208<br />
                            }<br /><br />
                            <h6>Autres languages de programmation</h6>
                            Vous pouvez également implémenter l'API et effectuer les requêtes dans d'autres langages de programmation, cela fonctionne également et plusieurs bibliothèques pour décoder le JSON existent. Il faut utiliser la requête suivante : <br/>
                            URL : https://serveur-multigames.net/api/<i>nom-de-votre-serveur</i>/?ip=<i>adresse.ip.du.joueur</i>
                        </blockquote>
                    </div>
                </div>
            </div>
        </section>
    </section>

@endsection
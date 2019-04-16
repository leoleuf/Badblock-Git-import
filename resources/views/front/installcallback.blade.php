@section('title', 'Installation de la méthode CALLBACK - Serveur MultiGames')
@section('description', 'Installez gratuitement la méthode CALLBACK pour vérifier les votes de vos joueurs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/add-server/callback')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Méthode Callback</h2>
                        <p class="mb-0">Initiation à la méthode Callback, experte.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none"><a title="API et Utilisation" href="/api" class="lis-light">API et Utilisation</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Callback</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
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
                            <kbd>&nbsp;&lt;?php&nbsp;<br />
                                &nbsp;&nbsp;$ip = $_GET['ip'];&nbsp;<br>
                                &nbsp;&nbsp;$servername = $_GET['servername'];&nbsp;<br>
                                &nbsp;&nbsp;$status = $_GET['status'];&nbsp;<br>
                                &nbsp;&nbsp;$nextvote = $_GET['nextvote'];&nbsp;<br>
                                &nbsp;&nbsp;if ($status == "SUCCESS")&nbsp;<br />
                                &nbsp;&nbsp;{&nbsp;<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Vote valide, donner le lot&nbsp;<br>
                                &nbsp;&nbsp;}&nbsp;<br>
                                &nbsp;?&gt;&nbsp;</kbd><br /><br />
                            <strong>Exemple de réponse</strong><br /><br />
                            <i>ip</i> : 127.0.0.1<br />
                            <i>servername</i> : testa-123<br />
                            <i>status</i> : SUCCESS<br />
                            <i>nextvote</i> : 5400
                        </blockquote>
                </div>

            </div>
        </div>
    </section>
@endsection
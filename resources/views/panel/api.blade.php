@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Mon API</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Tableau de bord</a></li>
                <li class="breadcrumb-item active">Intégrer mon serveur</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
        <!-- column -->
        <!-- column -->
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Intégrer le site de vote</h4>
                    Pour intégrer le lien vers la page de vote, il faut mettre le code exactement comme suit :<br /><br />
                    <code style="background-color: #ecf0f1;">
                    &lt;a title=&quot;Serveur Minecraft&quot; href=&quot;https://serveur-multigames.net/{{ encname($server->cat) }}/{{ encname($server->name) }}/vote&quot;&gt;&lt;img alt=&quot;Serveur Minecraft&quot; src=&quot;https://serveur-multigames.net/img/serveur-multigames.png&quot; /&gt;&lt;/a&gt;&lt;br /&gt;par &lt;a title=&quot;Serveur minecraft&quot; href=&quot;https://serveur-multigames.net/minecraft&quot;&gt;Serveur minecraft&lt;/a&gt;
                    </code><br />
                    <br />
                    @if ($server->votetype == "TRUE")
                    <h4 class="card-title">Script PHP pour vérifier si l'utilisateur a bien voté (méthode TRUE)</h4>
                    <code style="background-color: #ecf0f1;">
                        &lt;?php<br>
                            $SERVER_ID = "{{ encname($server->name) }}"; // Nom du serveur<br>
                            $IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant<br>
                            $SM = "https://serveur-multigames.net/api/$SERVER_ID/?ip=$IP";<br>
                            $result = @file_get_contents($SM);<br><br />
                            if($result == true)<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote valide<br>
                            }<br>
                            else<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote non valide<br>
                            }<br>
                        ?&gt;
                    </code>
                    <h4 class="card-title">Si l'api retourne "TRUE" c'est que l'utilisateur a bien voté.</h4>
                    @elseif ($server->votetype == "JSON")
                        <h4 class="card-title">Script PHP pour vérifier si l'utilisateur a bien voté (méthode JSON)</h4>
                        <code style="background-color: #ecf0f1;">
                            &lt;?php<br /><br />
                            $SERVER_ID = "nom-du-serveur"; // Nom du serveur<br>
                            $IP = $_SERVER['REMOTE_ADDR']; // Adresse IP du votant<br>
                            $SM = "https://serveur-multigames.net/api/$SERVER_ID/?ip=$IP";<br>
                            $result = @file_get_contents($SM);<br>
                            $result = json_decode($result);<br /><br />
                            if ($result->status == "SUCCESS")<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Vote valide<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;$nextvote = $result->nextvote; // Temps restant possible avant le prochain vote<br />
                            }<br>
                            else<br />
                            {<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;// Déjà voté ou vote non valide<br>
                            }<br><br />
                            ?&gt;
                        </code>
                    @elseif ($server->votetype == "CALLBACK")
                        <h4 class="card-title">Implémentation de la méthode CALLBACK</h4>
                        La méthode CALLBACK nécessite une mise en place spéciale disponible dans la section <a title="Installation de la méthode CallBack" href="/add-server/callback" target="_blank">Installation de la méthode CallBack</a>. Si vous ne savez pas utiliser cette méthode, nous vous conseillons de modifier la configuration de votre serveur et de mettre la méthode <strong>TRUE</strong>.
                    @endif
                    <div class="card-block">
                        <h5 class="card-title">Vous pouvez modifier la méthode de vérification de vote en éditant le serveur.</h5>
                    </div>
                </div>
            </div>
        </div>
@endsection
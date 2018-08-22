<?php
namespace App\Controllers;

use function DusanKasan\Knapsack\identity;
use function DusanKasan\Knapsack\isEmpty;
use function DusanKasan\Knapsack\slice;
use MongoDB\Exception\Exception;
use Monolog\Handler\Mongo;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Slim\Http\Request;

class UserController extends Controller
{

    public function getDashboard(RequestInterface $request, ResponseInterface $response)
	{
        //Récupération des données du serveur
        $collection = $this->container->mongoServer->players;
        $user = $collection->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

        $check = false;

        foreach ((array) $user['permissions']['alternateGroups'] as $k => $row){
            if ($k == "gradeperso"){
                $check = true;
            }else{
                $check = false;
            }
        }

        if ($user['permissions']['group'] == "gradeperso" || $check == true){

            //vérifiaction s'il n'y a pas deja un doc
            $count = $this->container->mongoServer->custom_data->count(['uniqueId' => $user['uniqueId']]);

            //Création du document si inexistant
            $data = [
                'uniqueId' => $user['uniqueId'],
                'prefix' => "",
                'prefix_new' => "",
                'prefix_state' => false,
                'chat_color' => "",
            ];
            if ($count == 0){
                $this->container->mongoServer->custom_data->InsertOne($data);
                $custom = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);
            }else{
                $custom = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);
            }
        }else{
            $custom = false;
        }

        //Recherche des achats du joueurs
        $collection_facture = $this->container->mongo->buy_logs;
        $buys = $collection_facture->find(['uniqueId' => $user['uniqueId']]);
        $c_buys = $collection_facture->count(['uniqueId' => $user['uniqueId']]);

        if ($c_buys == 0){
            $buys = false;
        }

        //Recherche des refund du joueurs
        $collection_facture = $this->container->mongo->funds;
        $factures = $collection_facture->find(['uniqueId' => $user['uniqueId']]);
        $c_factures = $collection_facture->count(['uniqueId' => $user['uniqueId']]);

        if ($c_factures == 0){
            $factures = false;
        }

        //Recherche des connections
        try{
            $connection = $this->container->mysql_casier->fetchRowMany('SELECT logs from friends WHERE pseudo = "' . $user["realName"] . '" LIMIT 10');
            $connection = json_decode($connection[0]['logs']);
            if (count($connection) > 0){
                $connection = array_slice($connection, (count($connection) - 10));

                foreach ($connection as $k => $row){
                    $row->ip = substr($row->log, strpos($row->log, "(IP:"),strpos($row->log, ")"));
                    $row->ip = str_replace("(IP: ", "", $row->ip);
                    $row->ip = str_replace(")", "", $row->ip);
                    $row->date = str_replace("[", "", $row->date);
                    $row->date = str_replace("]", "", $row->date);
                    $datetime = \DateTime::createFromFormat('d/m/Y H:i:s', $row->date, new \DateTimeZone('Europe/Paris'));
                    $timestamp = $datetime->getTimestamp();
                    $timestamp = $timestamp - (86400 * 365);
                    $timestamp = $timestamp + (86400 * 30);
                    $connection[$k]->date = date("d/m/Y", $timestamp);
                    $connection[$k]->time = date("H:i:s", $timestamp);
                    $connection[$k]->ip = $row->ip;
                }
            }else{
                $connection = false;
            }
        }catch (\mysqli_sql_exception $e){
            $connection = false;
        }


        try{
            //Récupération des sanctions
            $array = $this->container['config']['punishTypes'];
            $sanctions = $this->container->mysql_casier->fetchRowMany('SELECT * from sanctions WHERE pseudo = "' . $user["name"] . '" ORDER BY DATE LIMIT 10');
            if (count($sanctions) > 0){
                foreach ($sanctions as $k => $row){
                    $sanctions[$k]['type'] = $array[$row['type']];
                    if ($sanctions[$k]['expire'] != -1){
                        $sanctions[$k]['expire'] = $sanctions[$k]['expire'] / 1000;
                        $sanctions[$k]['expire'] =  round($sanctions[$k]['expire'], 0);
                    }
                }
            }else{
                $sanctions = false;
            }
        }catch (\mysqli_sql_exception $e){
            $sanctions = false;
        }

        try{
            // Récupération des dernières connexions
            $lastLogins = array();
            $lq = $this->container->mysql_casier->fetchRow('SELECT logs from friends WHERE pseudo = "' . $user["name"] . '" ORDER BY id LIMIT 1');
            if (count($lq) > 0){
                $i = 0;
                $brq = json_decode($lq['logs'], true);
                foreach ($brq as $k => $v){
                    $i++;
                    $v = $v['date'];
                    $v = str_replace("]", "", $v);
                    $v = str_replace("[", "", $v);
                    $datetime = \DateTime::createFromFormat( 'd/m/Y H:i:s', $v, new \DateTimeZone('Europe/Paris'));
                    $timestamp = $datetime->getTimestamp();
                    $timestamp = $timestamp - (86400 * 365);
                    $timestamp = $timestamp + (86400 * 30);
                    $v = date("d/m/Y à H:i:s", $timestamp);
                    $lastLogins[$i] = $v;
                }
            }else{
                $sanctions = false;
            }
        }catch (\mysqli_sql_exception $e){
            $sanctions = false;
        }


        //On affiche 0 pts boutiques si le joueur a pas sous
        if (empty($user["shoppoints"])){
            $user["shoppoints"] = 0;
        }

        $collection_vote = $this->container->mongo->votes_logs;
        $vote = $collection_vote->count(['name' => strtolower($this->session->getProfile('username')['username'])]);

        //Return view
        return $this->render($response, 'user.dashboard', ['connection' => $connection,'buys' => $buys,'user' => $user, 'vote' => $vote, 'custom' => $custom,'factures' => $factures, 'sanctions' => $sanctions]);


	}


	public function facture(RequestInterface $request, ResponseInterface $response, $args){
        //Check du get sale
        if (strlen($args["uid"]) != 24){
            $this->getDashboard($request,$response);
        }

        //Recherche de la facture
        $collection_facture = $this->container->mongo->funds;
        $factures = $collection_facture->findOne(['_id' => new \MongoDB\BSON\ObjectId($args["uid"])]);

        //Check si elle existe
        if ($factures == null){
            $this->getDashboard($request,$response);
        }

        //Récupération des données du serveur
        $collection = $this->container->mongoServer->players;
        $user = $collection->findOne(['realName' => strtolower($this->session->getProfile('username')['username'])]);

        if ($factures['uniqueId'] == $user['uniqueId'] || $this->container->session->getProfile('username')['is_admin'] == true){
            //On affiche 0 pts boutiques si le joueur a pas sous
            if (empty($user["shoppoints"])){
                $user["shoppoints"] = 0;
            }
            //Return view
            return $this->render($response, 'user.facture-view', ["user" => $user, "facture" => $factures]);
        }else{
            $this->getDashboard($request,$response);
        }


    }

	public function getProfile(RequestInterface $request, ResponseInterface $response, $args)
	{
        if (empty($args['pseudo'])) {
            //if user not found
            return $this->container['notFoundHandler']($request, $response);
        }
	    $args["pseudo"] = strtolower($args["pseudo"]);
        //Check si la page est déjà en cache
        if ($this->redis->exists('profile:'.$args["pseudo"])){
            $user = $this->redis->getJson('profile:'.$args["pseudo"]);
            return $this->render($response, 'user.profile', ['user' => $user]);
        }else{
            //Nouveau cache
            //sans cache
            $collection = $this->container->mongoServer->players;

            $user = $collection->findOne(['name' => $args['pseudo']]);

            if($user["punish"]["mute"]){
                $user["punish"]["muteEnd"] = round($user["punish"]["muteEnd"] / 1000);
            }
            if($user["punish"]["ban"]){
                $user["punish"]["banEnd"] = round($user["punish"]["banEnd"] / 1000);
            }

            $this->redis->setJson('profile:'.$args["pseudo"], $user);
            $this->redis->expire('profile:'.$args["pseudo"], 300);

            //return view
            return $this->render($response, 'user.profile', ['user' => $user]);
        }

	}


	public function changepassserv(RequestInterface $request, ResponseInterface $response){
        if (isset($_POST['newpassword'],$_POST['newpasswordverif'])){
            if (!empty($_POST['newpassword']) && !empty($_POST['newpasswordverif']) ){
                if ($_POST['newpassword'] == $_POST['newpasswordverif']){
                    if (strlen($_POST['newpassword']) >= 4){
                        $data = $this->ladder->encryptPassword($_POST['newpassword']);

                        $collection = $this->container->mongoServer->players;

                        $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])],['$set' => ["loginPassword" => $data]]);

                        $this->flash->addMessage('setting_error', "Changement effectué avec succès !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

                    }else{
                        $this->flash->addMessage('setting_error', "Votre mot de passe est trop court !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                }else{
                    $this->flash->addMessage('setting_error', "Les deux mot de passe ne sont pas identiques !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            }else{
                $this->flash->addMessage('setting_error', "Merci de saisir un mot de passe !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            }
        }else{
            $this->flash->addMessage('setting_error', "Merci de saisir un mot de passe !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
    }



    public function changeconnectmode(RequestInterface $request, ResponseInterface $response)
    {
	    if (isset($_POST['selectmode'])){
            if ($_POST['selectmode'] === "crack"){
                //Connection a mongo pour update le mode de connection
                $collection = $this->container->mongoServer->players;
                $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])],['$set' => ["onlineMode" => false]]);

                $this->flash->addMessage('setting_error', "Changement de mode de connection vers crack !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

            }elseif($_POST['selectmode'] === "premium"){
                //Connection a mongo pour update le mode de connection
                $collection = $this->container->mongoServer->players;
                $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])],['$set' => ["onlineMode" => true]]);


                $this->flash->addMessage('setting_error', "Changement de mode de connection vers premium !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

            }else{
                $this->flash->addMessage('setting_error', "Erreur !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            }
        }
    }

    public function teamspeak(RequestInterface $request, ResponseInterface $response)
    {
        if (isset($_POST['idts']) & !empty($_POST['idts'])){
            $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

            //vérifiaction s'il n'y a pas deja un doc
            $count = $this->container->mongo->teamspeak_uid->count(['uniqueId' => $user['uniqueId']]);

            $id_ts = explode("//",$_POST['idts']);
            $id_ts = explode("/",$id_ts[1]);
            $id_ts = explode("=",$id_ts[1]);

            $id_ts = $id_ts[0] . "=";


            $data = [
                'uniqueId' => $user['uniqueId'],
                'teamspeak_uid' => $id_ts,
                'ban' => false,
                'banExpire' => -1
            ];


            if ($count == 0){
                $this->container->mongo->teamspeak_uid->InsertOne($data);
            }else{
                $this->container->mongo->teamspeak_uid->updateOne(['uniqueId' => $user['uniqueId']],['$set' => ["teamspeak_uid" => $_POST['idts']]]);
            }

            $this->flash->addMessage('setting_error', "Compte TeamSpeak linké !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

        }else{
            $this->flash->addMessage('setting_error', "Merci de saisir un UID valide !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }

    }


    //gestion du grade custom
    public function custom(RequestInterface $request, ResponseInterface $response, $method){
        //Formatting
        $method = $method['method'];
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

        //vérifiaction s'il n'y a pas deja un doc
        $count = $this->container->mongoServer->custom_data->count(['uniqueId' => $user['uniqueId']]);
        foreach ((array) $user['permissions']['alternateGroups'] as $k => $row){
            if ($k == "gradeperso"){
                $check = true;
            }else{
                $check = false;
            }
        }

        if ($user['permissions']['group'] == "gradeperso" || $check == true){
        }else{
            $this->flash->addMessage('setting_error', "Erreur interne !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
            //Création du document si inexistant
        $data = [
            'uniqueId' => $user['uniqueId'],
            'prefix' => "",
            'prefix_new' => "",
            'prefix_state' => false,
            'chat_color' => "",
        ];
        if ($count == 0){
            $this->container->mongoServer->custom_data->InsertOne($data);
        }


        $count = $this->container->mongo->teamspeak_uid->count(['uniqueId' => $user['uniqueId']]);

        $data = [
            'uniqueId' => $user['uniqueId'],
            'teamspeak_uid' => "",
            'ban' => false,
            'banExpire' => -1
        ];


        if ($count == 0){
            $this->container->mongo->teamspeak_uid->InsertOne($data);
        }

        if ($method == "prefix"){
            //Nouveau préfix
            if (isset($_POST['prefix'])){
                if (!empty($_POST['prefix'])){
                    if (strlen($_POST['prefix']) < 16){
                        $this->container->mongoServer->custom_data->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ['prefix_state' => false,'prefix_new' => $_POST['prefix']]]);

                        //https://canary.discordapp.com/api/webhooks/456069141464612864/8GTSrExs1xlL2v4fxj5pTI8BEhJlYqpn0M_Lesioqrw81dCS07hdo3fN9Vz_JPq9jWqv


                        //Discord WebHook notif

                        $data = array("username" => "Grade Custom","embeds" => array(0 => array(
                            "url" => "http://badblock.fr",
                            "title" => "Prefix de " . $user['realName'] . " à valider !",
                            "description" => "Valider le préfix " . $_POST['prefix'],
                            "color" => 5788507
                        )));

                        $curl = curl_init(getenv("DISCORD_RESP"));
                        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
                        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
                        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
                        curl_exec($curl);



                        $this->flash->addMessage('setting_error', "Demande de préfix envoyé !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }else{
                        $this->flash->addMessage('setting_error', "Merci de saisir un préfix de moins de 16 caractères !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                }else{
                    $this->flash->addMessage('setting_error', "Merci de saisir un préfix valide !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            }
        }elseif ($method == "textcolor"){
            if (isset($_POST['color'])){
                if (!empty($_POST['color'])){

                    if (in_array($_POST['color'], $this->container['config']['colorChat'])){

                        $_POST['color'] = array_search($_POST['color'], $this->container['config']['colorChat']);
                        $this->container->mongoServer->custom_data->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ['chat_color' => $_POST['color']]]);

                        $this->flash->addMessage('setting_error', "Couleur de chat validé !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                }else{
                    $this->flash->addMessage('setting_error', "Merci de choisir une couleur !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            }
        }elseif ($method == "teamspeak_canal"){
            if (isset($_POST['canal_name']) && isset($_POST['canal_psw'])){
                if (!empty($_POST['canal_name']) && !empty($_POST['canal_psw'])){
                    $ts_uid = $this->container->mongo->teamspeak_uid->findOne(['uniqueId' => $user['uniqueId']]);
                    $count = $this->container->mongo->teamspeak_channel->count(['uniqueId' => $user['uniqueId']]);
                    //Création du document si inexistant
                    $data = [
                        'uniqueId' => $user['uniqueId'],
                        'ts_owner_uid' => $ts_uid['teamspeak_uid'],
                        'channel_name' => $_POST['canal_name'],
                        'channel_pwd' => $_POST['canal_psw'],
                        'state' => false
                    ];
                    if ($count == 0){
                        $this->container->mongo->teamspeak_channel->InsertOne($data);

                        $this->flash->addMessage('setting_error', "Canal créer !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }else{
                        $this->container->mongo->teamspeak_channel->updateOne(['uniqueId' => $user['uniqueId']], ['$set' =>
                            [
                                'uniqueId' => $user['uniqueId'],
                                'ts_owner_uid' => $ts_uid['teamspeak_uid'],
                                'channel_name' => $_POST['canal_name'],
                                'channel_pwd' => $_POST['canal_psw'],
                                'state' => false
                            ]
                        ]);
                        $this->flash->addMessage('setting_error', "Paramètre de canal changé !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                }else{
                    $this->flash->addMessage('setting_error', "Merci de choisir un nom et un mot de passe valide !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            }
        }elseif ($method == "icone"){
            $this->flash->addMessage('setting_error', "Fonctionnalité désactivé !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
    }


    public function changeName(RequestInterface $request, ResponseInterface $response){
        if (!isset($_POST['name'])){
            $this->flash->addMessage('setting_error', "Nouveau Pseudo non renseigné !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
        if (strlen($_POST['name']) < 3 || strlen($_POST['name']) > 16){
            $this->flash->addMessage('setting_error', "Votre Nouveau Pseudo doit faire entre 4 et 16 caractères !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }


    }



}
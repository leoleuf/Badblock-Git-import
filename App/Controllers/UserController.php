<?php
namespace App\Controllers;

use function DusanKasan\Knapsack\identity;
use function DusanKasan\Knapsack\isEmpty;
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



        if ($user['permissions']['group'] == "gradeperso" || in_array('gradeperso', (array) $user['permissions']['alternateGroups'])){
            //vérifiaction s'il n'y a pas deja un doc
            $count = $this->container->mongoServer->custom_data->count(['uniqueId' => $user['uniqueId']]);

            //Création du document si inexistant
            $data = [
                'uniqueId' => $user['uniqueId'],
                'prefix' => "",
                'prefix_state' => false,
                'chat_color' => "",
            ];
            if ($count == 0){
                $this->container->mongoServer->custom_data->InsertOne($data);
            }else{
                $custom = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);
            }
        }else{
            $custom = false;
        }

        //Recherche des factures du joueurs
        $collection_facture = $this->container->mongo->funds;
        $factures = $collection_facture->find(['unique-id' => $user['uniqueId']]);

        //Récupération des sanctions
        $sanctions = $this->container->mysql_casier->fetchRowMany('SELECT * from sanctions WHERE pseudo = "' . $user["name"] . '" ORDER BY DATE LIMIT 10');


        //On affiche 0 pts boutiques si le joueur a pas sous
        if (empty($user["shoppoints"])){
            $user["shoppoints"] = 0;
        }




        //Return view
        return $this->render($response, 'user.dashboard', ['user' => $user,'custom' => $custom,'factures' => $factures, 'sanctions' => $sanctions]);

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

        if ($factures['unique-id'] == $user['uniqueId'] || $this->container->session->getProfile('username')['is_admin'] == true){
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

            $data = [
                'uniqueId' => $user['uniqueId'],
                'teamspeak_uid' => $_POST['idts'],
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

        //Création du document si inexistant
        $data = [
            'uniqueId' => $user['uniqueId'],
            'prefix' => "",
            'prefix_state' => false,
            'chat_color' => "",
        ];
        if ($count == 0){
            $this->container->mongoServer->custom_data->InsertOne($data);
        }



        if ($method == "prefix"){
            //Nouveau préfix





        }elseif ($method == ""){

        }


    }





}
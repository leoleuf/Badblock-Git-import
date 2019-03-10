<?php

namespace App\Controllers;

use function DusanKasan\Knapsack\identity;
use function DusanKasan\Knapsack\isEmpty;
use function DusanKasan\Knapsack\slice;
use MongoDB\Exception\Exception;
use Monolog\Handler\Mongo;
use App\Twitter\TwitterAPIExchange;
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

        foreach ((array)$user['permissions']->groups->bungee as $k => $row) {
            if ($k == "gradeperso") {
                $check = true;
            }
        }

        if ($check == true) {
            //Vérifiaction s'il n'y a pas deja un doc
            $count = $this->container->mongoServer->custom_data->count(['uniqueId' => $user['uniqueId']]);

            //Création du document si inexistant
            $data = [
                'uniqueId' => $user['uniqueId'],
                'prefix' => "&dLegend",
                'prefix_new' => "",
                'prefix_state' => true,
                'chat_color' => "",
            ];
            if ($count == 0) {
                $this->container->mongoServer->custom_data->InsertOne($data);
                $custom = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);
            } else {
                $custom = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);
            }
        } else {
            $custom = false;
        }

        //Recherche des achats du joueurs
        $collection_facture = $this->container->mongo->buy_logs;
        $buys = $collection_facture->find(['uniqueId' => $user['uniqueId']]);
        $c_buys = $collection_facture->count(['uniqueId' => $user['uniqueId']]);

        if ($c_buys == 0) {
            $buys = false;
        }

        //Recherche des refund du joueurs
        $collection_facture = $this->container->mongo->funds;
        $factures = $collection_facture->find(['uniqueId' => $user['uniqueId']]);
        $c_factures = $collection_facture->count(['uniqueId' => $user['uniqueId']]);

        if ($c_factures == 0) {
            $factures = false;
        }

        //Recherche des connections
        $connection = $this->container->mongoServer->connectionLogs->find(['username' => $user['name']], ['sort' => ['timestamp' => -1], 'limit' => 20])->toArray();
        //Récupération des sanctions
        $sanctions = $this->container->mongoServer->punishments->find(['punishedUuid' => $user['uniqueId']], ['limit' => 20, 'sort' => ['date' => -1],]);


        //On affiche 0 pts boutiques si le joueur a pas sous
        if (empty($user["shoppoints"])) {
            $user["shoppoints"] = 0;
        }

        // We get the people sponsored by this person
        $rfr = $this->container->mongoServer->refers->findOne(['uniqueId' => $user['uniqueId']]);

        if ($rfr != null) {
            if (isset($rfr['state']) && $rfr['state'] == "CONFIRMED") {
                $usrnm = $this->container->mongoServer->players->findOne(['uniqueId' => $rfr['receiver']]);

                if ($usrnm != null && isset($usrnm['name'])) {
                    $rfr = $usrnm['name'];
                } else {
                    $rfr = "";
                }
            } else {
                $rfr = "";
            }
        } else {
            $rfr = "";
        }

        $refer = $this->container->mongoServer->refers->find(['receiver' => $user['uniqueId']]);
        $rf = array();

        $i = 0;
        foreach ($refer as $key => $value) {
            $usrnm = $this->container->mongoServer->players->findOne(['uniqueId' => $value['uniqueId']]);

            if ($usrnm != null && isset($usrnm['name'])) {
                $usrnm = $usrnm['name'];
                $usrnm = htmlspecialchars($usrnm);

                $data = [
                    'sponsor' => $usrnm,
                    'state' => $value['state']
                ];
                $rf[$i] = $data;
                $i++;
            }
        }

        $collection_vote = $this->container->mongo->votes_logs;
        $vote = $collection_vote->count(['name' => strtolower($this->session->getProfile('username')['username'])]);

        //Return view
        return $this->render($response, 'user.dashboard', ['connection' => $connection, 'rfr' => $rfr, 'rf' => $rf, 'buys' => $buys, 'user' => $user, 'vote' => $vote, 'custom' => $custom, 'factures' => $factures, 'sanctions' => $sanctions]);


    }


    public function facture(RequestInterface $request, ResponseInterface $response, $args)
    {
        //Check du get sale
        if (strlen($args["uid"]) != 24) {
            $this->getDashboard($request, $response);
        }

        //Recherche de la facture
        $collection_facture = $this->container->mongo->funds;
        $factures = $collection_facture->findOne(['_id' => new \MongoDB\BSON\ObjectId($args["uid"])]);

        //Check si elle existe
        if ($factures == null) {
            $this->getDashboard($request, $response);
        }

        //Récupération des données du serveur
        $collection = $this->container->mongoServer->players;
        $user = $collection->findOne(['realName' => strtolower($this->session->getProfile('username')['username'])]);

        if ($factures['uniqueId'] == $user['uniqueId'] || $this->container->session->getProfile('username')['is_admin'] == true) {
            //On affiche 0 pts boutiques si le joueur a pas sous
            if (empty($user["shoppoints"])) {
                $user["shoppoints"] = 0;
            }
            //Return view
            return $this->render($response, 'user.facture-view', ["user" => $user, "facture" => $factures]);
        } else {
            $this->getDashboard($request, $response);
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
        if ($this->redis->exists('profile:' . $args["pseudo"])) {
            $user = $this->redis->getJson('profile:' . $args["pseudo"]);
            return $this->render($response, 'user.profile', ['joueur' => $user]);
        } else {
            //Nouveau cache
            //sans cache
            $collection = $this->container->mongoServer->players;
            $user = $collection->findOne(['name' => $args['pseudo']]);

            if ($user == null){
                return $this->container['notFoundHandler']($request, $response);
            }

            //Replace stylé
            $group = [];
            foreach ($user['permissions']->groups->bungee as $k => $row) {
                if (strpos($k, 'pmanage_') !== false) {
                    array_push($group, str_replace('pmanage_', 'Manager ', $k));
                } else if (strcasecmp($k, 'gradeperso') == 0) {
                    array_push($group, 'Legend');
                } else if (strcasecmp($k, 'default') == 0) {
                    array_push($group, 'Joueur');
                } else {
                    array_push($group, $k);
                }
            }
            $user['permissions']['alternateGroups'] = $group;


            //Search friends
            $user['friends'] = [];
            $ufriends = $this->container->mongoServer->friendlist->findOne(["_owner" => $user['uniqueId']]);
            if ($ufriends != null){
                foreach ($ufriends["players"] as $friend){
                    if ($friend["state"] == "ACCEPTED"){
                        $name = $this->container->mongoServer->players->findOne(["uniqueId" => $friend['uuid']]);
                        if ($name != null){
                            array_push($user['friends'], $name['realName']);
                        }
                    }
                }
            }else{
                $user['friends'] = [];
            }

            //Plots
            try{
                $Plots = $this->container->mysql_freebuild->fetchRowMany("SELECT * FROM freebuildplot WHERE owner = '" . $user["uniqueId"] . "' LIMIT 100");
                foreach ($Plots as $k => $Plot) {
                    $Plots[$k]['x'] = 262 * $Plot['plot_id_x'] - 132;
                    $Plots[$k]['z'] = 262 * $Plot['plot_id_z'] - 132;

                    //Search trusted
                    $Trust_list = [];
                    $Trust = $this->container->mysql_freebuild->fetchRowMany("SELECT * FROM `freebuildplot_helpers` WHERE `plot_plot_id` = " . $Plot['id']);
                    if ($Trust != null) {
                        foreach ($Trust as $user) {
                            $In = $this->container->mongoServer->players->findOne(['uniqueId' => $user['user_uuid']]);
                            if ($In != null) {
                                array_push($Trust_list, $In['realName']);
                            }
                        }
                        $Plots[$k]['trust'] = $Trust_list;
                    } else {
                        $Plots[$k]['trust'] = false;
                    }
                }
            }catch (Exception $exception){

            }

            if (count($Plots) == 0) {
                $user['plots'] = false;
            } else {
                $user['plots'] = $Plots;
            }

            $this->redis->setJson('profile:' . $args["pseudo"], $user);
            $this->redis->expire('profile:' . $args["pseudo"], 200);

            //return view
            return $this->render($response, 'user.profile', ['joueur' => $user]);
        }

    }


    public function changepassserv(RequestInterface $request, ResponseInterface $response)
    {
        if (isset($_POST['newpassword'], $_POST['newpasswordverif'])) {
            if (!empty($_POST['newpassword']) && !empty($_POST['newpasswordverif'])) {
                if ($_POST['newpassword'] == $_POST['newpasswordverif']) {
                    if (strlen($_POST['newpassword']) >= 4) {
                        $data = $this->ladder->encryptPassword($_POST['newpassword']);

                        $collection = $this->container->mongoServer->players;

                        $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])], ['$set' => ["loginPassword" => $data]]);

                        $this->flash->addMessage('setting_error', "Changement effectué avec succès !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

                    } else {
                        $this->flash->addMessage('setting_error', "Votre mot de passe est trop court !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                } else {
                    $this->flash->addMessage('setting_error', "Les deux mot de passe ne sont pas identiques !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            } else {
                $this->flash->addMessage('setting_error', "Merci de saisir un mot de passe !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            }
        } else {
            $this->flash->addMessage('setting_error', "Merci de saisir un mot de passe !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
    }


    public function refer(RequestInterface $request, ResponseInterface $response)
    {
        $n = $this->session->getProfile('username')['username'];
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($n)]);

        if ($user == null) {
            return;
        }

        if (isset($user['refer']) && $user['refer']) {
            return $this->redirect($response, "/dashboard");
        }

        return $this->render($response, 'user.refer');
    }

    public function referSubmit(RequestInterface $request, ResponseInterface $response)
    {
        // Get the username
        $n = $this->session->getProfile('username')['username'];
        // Fetch data
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($n)]);

        // If we couldn't find data
        if ($user == null) {
            // So we stop
            return;
        }

        // If he skiped the step
        if (!isset($_POST['refer'])) {
            // We put that he did have this step
            $this->container->mongoServer->players->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ["refer" => true]]);
            // And we send him back on his request
            return $this->redirect($response, "/dashboard");
        }

        // In the other case where he entered a nickname, we check the referrals
        $refer = $this->container->mongoServer->refers->count(['uniqueId' => $user['uniqueId']]);

        // If he's already someone's referral
        if ($refer != false && $refer > 0) {
            // We tell him
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#already');
        }

        // In the other case, we check the username
        $nb = strtolower(htmlspecialchars($_POST['refer']));
        // Fetch data
        $userB = $this->container->mongoServer->players->findOne(['name' => strtolower($nb)]);

        // If we couldn't find data
        if ($userB == null) {
            // We tell him
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#username-error');
        }

        // If the guy is schizophrenic
        if ($userB['uniqueId'] == $user['uniqueId']) {
            // We tell him
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#ctsponsor-yourself');
        }

        // We get the people sponsored by this person
        $referB = $this->container->mongoServer->refers->count(['receiver' => $userB['uniqueId']]);

        // Too many sponsored people
        if ($referB != false && $referB > 2) {
            // We tell him
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#toomany-refers');
        }

        // Create data object (for the request)
        $data = [
            'uniqueId' => $user['uniqueId'],
            'receiver' => $userB['uniqueId'],
            'state' => "PENDING"
        ];

        // Insert the pending request in the collection
        $this->container->mongoServer->refers->InsertOne($data);

        // We put that he did have this step
        $this->container->mongoServer->players->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ["refer" => true]]);

        /** BASE MAIL => SEND TO THE APPLICANT */

        try {
            $user_xen = $this->xenforo->getUser($n);
        } catch (\Exception $e) {
            $user_xen = null;
        }

        if ($user_xen != null) {
            // Get the mail base content
            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-pending.html");
            // Replace the applicant's username
            $mailContent = str_replace("(username)", $user['name'], $mailContent);
            // Replace the receiver's username
            $mailContent = str_replace("(other-username)", $userB['name'], $mailContent);
            // Update the date
            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
            // Instanciate the email object
            $mail = new \App\Mail(true);
            // Send the mail
            $mail->sendMail($user_xen["email"], "Vous avez demandé un parrainage", $mailContent);
        }

        /** BASE MAIL => SEND TO THE RECEIVER */

        try {
            $userB_xen = $this->xenforo->getUser($nb);
        } catch (\Exception $e) {
            $userB_xen = null;
        }

        if ($userB_xen != null) {
            // Get the mail base content
            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-received.html");
            // Replace the applicant's username
            $mailContent = str_replace("(username)", $userB['name'], $mailContent);
            // Replace the receiver's username
            $mailContent = str_replace("(other-username)", $user['name'], $mailContent);
            // Update the date
            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
            // Instanciate the email object
            $mail = new \App\Mail(true);
            // Send the mail
            $mail->sendMail($userB_xen["email"], "Vous avez reçu une demande de parrainage", $mailContent);
        }

        /** DEBUG */
        $mailContent = $user['name'] . " => " . $userB['name'] . " => en attente";
        $mail = new \App\Mail(true);
        $mail->sendMail("xmalware2@gmail.com", "BadBlock - Parrainage", $mailContent);

        // Tell him
        $this->flash->addMessage('setting_error', "Votre demande de parrainage a été envoyée. Vous receverez 100 points boutique quand la personne aura acceptée votre demande. Vous receverez un mail.");

        // And we send him back on his request
        return $this->redirect($response, "/dashboard");
    }

    public function referManage(RequestInterface $request, ResponseInterface $response)
    {
        //yes / no / id
        // Get the username
        $n = $this->session->getProfile('username')['username'];
        // Fetch data
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($n)]);

        // If we couldn't find data
        if ($user == null) {
            // So we stop
            return;
        }

        // If we can't find ID
        if (!isset($_POST['id'])) {
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#sponsor-cantfind-id');
        }

        $id = intval($_POST['id']);

        // In the other case where he entered a nickname, we check the referrals
        $refer = $this->container->mongoServer->refers->find(['receiver' => $user['uniqueId']]);

        $i = 0;

        $request = null;

        foreach ($refer as $key => $value) {
            if ($i == $id) {
                $request = [
                    'id' => $value['_id'],
                    'uniqueId' => $value['uniqueId'],
                    'state' => $value['state']
                ];
                break;
            }
            $i++;
        }

        if ($request == null) {
            // We tell him
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#sponsor-unknown-request');
        }

        if ($request['state'] != "PENDING") {
            // We tell him
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#sponsor-not-pending');
        }

        $nB = $request['uniqueId'];
        // Fetch data
        $userB = $this->container->mongoServer->players->findOne(['uniqueId' => strtolower($nB)]);

        if ($userB == null) {
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#sponsor-unknown-user');
        }

        $nB = strtolower($userB['name']);

        if (isset($_POST['no'])) {
            // We put that he did have this step
            $this->container->mongoServer->refers->deleteOne(['_id' => new \MongoDB\BSON\ObjectID($request['id'])]);

            /** BASE MAIL => SEND TO THE APPLICANT */

            try {

                $userB_xen = $this->xenforo->getUser($nB);
            } catch (\Exception $e) {
                $userB_xen = null;
            }

            if ($userB_xen != null) {
                // Get the mail base content
                $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-he-refused.html");
                // Replace the applicant's username
                $mailContent = str_replace("(username)", $user['name'], $mailContent);
                // Update the date
                $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                // Instanciate the email object
                $mail = new \App\Mail(true);
                // Send the mail
                $mail->sendMail($userB_xen["email"], "Votre demande de parrainage a été refusée", $mailContent);
            }

            /** BASE MAIL => SEND TO THE RECEIVER */

            try {
                $user_xen = $this->xenforo->getUser($n);
            } catch (\Exception $e) {
                $user_xen = null;
            }

            if ($user_xen != null && $nB != null) {
                // Get the mail base content
                $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-you-refused.html");
                // Replace the applicant's username
                $mailContent = str_replace("(username)", $nB, $mailContent);
                // Update the date
                $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
                // Instanciate the email object
                $mail = new \App\Mail(true);
                // Send the mail
                $mail->sendMail($userB_xen["email"], "Vous avez refusé une demande de parrainage", $mailContent);
            }

            /** DEBUG */
            $mailContent = $userB['name'] . " => " . $user['name'] . " => refusé";
            $mail = new \App\Mail(true);
            $mail->sendMail("xmalware2@gmail.com", "BadBlock - Parrainage", $mailContent);

            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#sponsor-you-refused');
        }

        // We put that he did have this step
        $this->container->mongoServer->refers->updateOne(['_id' => new \MongoDB\BSON\ObjectID($request['id'])], ['$set' => ["state" => "CONFIRMED"]]);

        $data = [
            'uniqueId' => $userB['uniqueId'],
            'date' => date('Y-m-d H:i:s'),
            'price' => 0,
            'gateway' => 'parrainage',
            'pseudo' => $userB['name'],
            'points' => 100
        ];

        $this->container->mongoUltra->funds->insertOne($data);

        $resp = [
            'name' => $userB['name'],
            'date' => date("Y-m-d H:i:s")
        ];

        $this->container->mongoUltra->funds_logs->insertOne($resp);

        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $request['uniqueId']]);

        if ($money == null) {
            $data = [
                "uniqueId" => $request['uniqueId'],
                "points" => 100
            ];
            $this->container->mongo->fund_list->insertOne($data);
        } else {
            $money['points'] = $money['points'] + 100;
            $this->container->mongo->fund_list->updateOne(["uniqueId" => $request['uniqueId']], ['$set' => ["points" => $money['points']]]);
        }

        $this->container->mongoServer->players->updateOne(['uniqueId' => $request['uniqueId']], ['$set' => ["state" => "CONFIRMED"]]);


        /** BASE MAIL => SEND TO THE APPLICANT */

        try {
            $userB_xen = $this->xenforo->getUser($nB);
        } catch (\Exception $e) {
            $userB_xen = null;
        }

        if ($userB_xen != null) {
            // Get the mail base content
            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-he-accepted.html");
            // Replace the applicant's username
            $mailContent = str_replace("(username)", $user['name'], $mailContent);
            // Update the date
            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
            // Instanciate the email object
            $mail = new \App\Mail(true);
            // Send the mail
            $mail->sendMail($userB_xen["email"], "Votre demande de parrainage a été acceptée", $mailContent);
        }

        /** BASE MAIL => SEND TO THE RECEIVER */

        try {
            $user_xen = $this->xenforo->getUser($n);
        } catch (\Exception $e) {
            $user_xen = null;
        }

        if ($user_xen != null && $nB != null) {
            // Get the mail base content
            $mailContent = file_get_contents("https://cdn.badblock.fr/wd/mails/mail-sponsor-you-accepted.html");
            // Replace the applicant's username
            $mailContent = str_replace("(username)", $nB, $mailContent);
            // Update the date
            $mailContent = str_replace("(date)", date('Y-m-d H:i:s'), $mailContent);
            // Instanciate the email object
            $mail = new \App\Mail(true);
            // Send the mail
            $mail->sendMail($user_xen["email"], "Vous avez accepté une demande de parrainage", $mailContent);
        }

        /** DEBUG */
        $mailContent = $userB['name'] . " => " . $user['name'] . " => accepté";
        $mail = new \App\Mail(true);
        $mail->sendMail("xmalware2@gmail.com", "BadBlock - Parrainage", $mailContent);

        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#sponsor-you-accepted');
    }

    public function changeconnectmode(RequestInterface $request, ResponseInterface $response)
    {
        if (isset($_POST['selectmode'])) {
            if ($_POST['selectmode'] === "crack") {
                //Connection a mongo pour update le mode de connection
                $collection = $this->container->mongoServer->players;
                $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])], ['$set' => ["onlineMode" => false]]);

                $this->flash->addMessage('setting_error', "Changement de mode de connection vers crack !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

            } elseif ($_POST['selectmode'] === "premium") {
                //Connection a mongo pour update le mode de connection
                $collection = $this->container->mongoServer->players;
                $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])], ['$set' => ["onlineMode" => true]]);


                $this->flash->addMessage('setting_error', "Changement de mode de connection vers premium !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

            } else {
                $this->flash->addMessage('setting_error', "Erreur !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            }
        }
    }

    public function teamspeak(RequestInterface $request, ResponseInterface $response)
    {
        $this->flash->addMessage('setting_error', "Service désactivé !");
        //redirect to last page
        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

        if (isset($_POST['idts']) & !empty($_POST['idts'])) {
            $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

            //vérifiaction s'il n'y a pas deja un doc
            $count = $this->container->mongo->teamspeak_uid->count(['uniqueId' => $user['uniqueId']]);

            $id_ts = $_POST['idts'];
            $reg = "/(?:\\d+\\/)([^~]+)/";
            $id_ts = preg_match($reg, $id_ts, $matches);
            $id_ts = $matches[0];
            $Pos = strpos($id_ts, "/");
            $id_ts = substr($id_ts, $Pos + 1);

            $data = [
                'uniqueId' => $user['uniqueId'],
                'teamspeak_uid' => $id_ts,
                'ban' => false,
                'banExpire' => -1
            ];


            if ($count == 0) {
                $this->container->mongo->teamspeak_uid->InsertOne($data);
            } else {
                $Pl = $this->container->mongo->teamspeak_uid->findOne(['uniqueId' => $user['uniqueId']]);
                if ($Pl['teamspeak_uid'] != $id_ts){
                    $this->container->teamspeak->removeClient($Pl['teamspeak_uid']);
                }
                $this->container->mongo->teamspeak_uid->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ["teamspeak_uid" => $id_ts]]);
            }

            //Add all grades
            //Vérif grade Legend
            $check = false;
            $count = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);
            foreach ((array)$user['permissions']->groups->bungee as $k => $row) {
                if ($k == "gradeperso") {
                    $check = true;
                }
            }

            $this->container->teamspeak->addtogroup($id_ts, 14);

            //Other grade
            foreach ((array)$user['permissions']->groups->bungee as $k => $row) {
                if ($k == "mvp+") {
                    $this->container->teamspeak->addtogroup($id_ts, 53);
                }elseif ($k == "mvp") {
                    $this->container->teamspeak->addtogroup($id_ts, 202);
                }elseif ($k == "vip+") {
                    $this->container->teamspeak->addtogroup($id_ts, 52);
                }elseif ($k == "vip") {
                    $this->container->teamspeak->addtogroup($id_ts, 203);
                }elseif ($k == "gradeperso") {
                    $this->container->teamspeak->addtogroup($id_ts, 61);
                }
            }


            if($check == true){
                //Check groups
                $count = $this->container->mongo->teamspeak_groups->count(['uniqueId' => $user['uniqueId']]);
                $custom = $this->container->mongoServer->custom_data->findOne(['uniqueId' => $user['uniqueId']]);

                if ($count < 1){
                    //Duplicate wtf ??
                    if ($custom['prefix'] == "&dLegend"){
                        $custom['prefix'] = "Legend ". $user['name'];
                    }

                    //Create group
                    $Id = $this->container->teamspeak->customGroup($custom['prefix']);

                    //add user to group
                    $this->container->teamspeak->addtogroup($id_ts, $Id);

                    $data = [
                        'uniqueId' => $user['uniqueId'],
                        'teamspeak_uid' => $id_ts,
                        'group_id' => $Id
                    ];

                    $this->container->mongo->teamspeak_groups->insertOne($data);
                }else{
                    $reg = "/[&](.{1})/";
                    $custom['prefix'] = preg_replace($reg, "",$custom['prefix']);

                    $Id = $this->container->mongo->teamspeak_groups->findOne(['uniqueId' => $user['uniqueId']]);
                    $this->container->teamspeak->removeGroup($Id['group_id']);

                    //Create group
                    $Id = $this->container->teamspeak->customGroup($custom['prefix']);

                    //add user to group
                    $Su = $this->container->teamspeak->addtogroup($id_ts, $Id);

                    $data = [
                        'uniqueId' => $user['uniqueId'],
                        'teamspeak_uid' => $id_ts,
                        'group_id' => $Id
                    ];
                    $this->container->mongo->teamspeak_groups->deleteOne(['uniqueId' => $user['uniqueId']]);
                    $this->container->mongo->teamspeak_groups->insertOne($data);
                }
            }

            $this->flash->addMessage('setting_error', "Compte TeamSpeak linké !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

        } else {
            $this->flash->addMessage('setting_error', "Merci de saisir un UID valide !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }

    }


    /**
     * Get UUID from Username
     *
     * @param  string $username
     * @return string|bool  UUID (without dashes) on success, false on failure
     */
    public function username_to_uuid($username)
    {
        $profile = $this->username_to_profile($username);
        if (is_array($profile) and isset($profile['id'])) {
            return $profile['id'];
        }
        return false;
    }

    /**
     * Get Profile (Username and UUID) from username
     *
     * @uses http://wiki.vg/Mojang_API#Username_-.3E_UUID_at_time
     *
     * @param  string $username
     * @return array|bool  Array with id and name, false on failure
     */
    public function username_to_profile($username)
    {
        if ($this->is_valid_username($username)) {
            $json = file_get_contents('https://api.mojang.com/users/profiles/minecraft/' . $username);
            if (!empty($json)) {
                $data = json_decode($json, true);
                if (is_array($data) and !empty($data)) {
                    return $data;
                }
            }
        }
        return false;
    }

    /**
     * Check if string is a valid Minecraft username
     *
     * @param  string $string to check
     * @return bool   Whether username is valid or not
     */
    public function is_valid_username($string)
    {
        return is_string($string) and strlen($string) >= 2 and strlen($string) <= 16 and ctype_alnum(str_replace('_', '', $string));
    }

    public function rewardTwitter1(RequestInterface $request, ResponseInterface $response, $method)
    {
        $n = $this->session->getProfile('username')['username'];
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($n)]);

        if ($user == null) {
            return;
        }

        $consumer_key = "JgQHyz4RwedCWVdyUD5VLM8Rw";
        $consumer_secret = "EFvvPXbwd5ANxlKysETsvkq8tJGFxZ43xp304ou9zuc7igPtNy";

        $oauth_verifier = "";
        $oauth_token = "";
        $token = "";
        $secret = "";

        if (!isset($user['oauth_verifier']) OR !isset($user['oauth_token']) OR
            !isset($user['token']) OR !isset($user['secret']) OR
            empty($user['oauth_verifier'])
            OR empty($user['oauth_token']) OR empty($user['token']) OR empty($user['secret'])) {
            if (!isset($_GET['oauth_verifier']) OR !isset($_GET['oauth_token'])) {
                $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret);
                $temporary_credentials = $connection->oauth('oauth/request_token', array("oauth_callback" => "https://badblock.fr/dashboard/reward/twitter-1"));
                $_SESSION['oauth_token'] = $temporary_credentials['oauth_token'];
                $_SESSION['oauth_token_secret'] = $temporary_credentials['oauth_token_secret'];
                $url = $connection->url("oauth/authorize", array("oauth_token" => $temporary_credentials['oauth_token']));
                return $this->redirect($response, $url);
            }

            $oauth_verifier = $_GET['oauth_verifier'];
            $oauth_token = $_GET['oauth_token'];

            $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret);
            $params = array("oauth_verifier" => $oauth_verifier, "oauth_token" => $oauth_token);
            $access_token = $connection->oauth("oauth/access_token", $params);

            $token = $access_token['oauth_token'];
            $secret = $access_token['oauth_token_secret'];

            $this->container->mongoServer->players->updateOne(["name" => strtolower($n)], ['$set' => ["oauth_verifier" => $oauth_verifier, "oauth_token" => $oauth_token,
                "token" => $token, "secret" => $secret]]);
        } else {
            $oauth_verifier = $user['oauth_verifier'];
            $oauth_token = $user['oauth_token'];

            $token = $user['token'];
            $secret = $user['secret'];
        }

        $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret, $token, $secret);
        $content = $connection->get("account/verify_credentials");

        if ($content != null && $content->errors != null) {
            $this->flash->addMessage('setting_error', "Tu as déjà reçu ta récompense Twitter 1.");
            //redirect to last page

            return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
        }

        $string = $connection->get("statuses/user_timeline", array('count' => 200, 'excludes_replies' => true, 'includes_rts' => true));

        $d = false;

        foreach ($string as $items) {
            if (!isset($items->text)) {
                continue;
            }

            if ($this->startswith($items->text, "Rejoins-moi sur le Serveur Minecraft BadBlock dès maintenant ! https://") && $this->endswith($items->text, " @BadBlockGame")) {
                $d = true;
            }
        }

        if (isset($user['recomptwitter1']) && $user['recomptwitter1']) {
            $this->flash->addMessage('setting_error', "Tu as déjà reçu ta récompense Twitter 1.");
            //redirect to last page

            return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
        }

        if ($d) {
            $this->container->mongoServer->players->updateOne(["name" => strtolower($n)], ['$set' => ["recomptwitter1" => true]]);
            $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user->uniqueId]);

            $cu = 50;

            if ($money == null) {
                $data = [
                    "uniqueId" => $user->uniqueId,
                    "points" => $cu
                ];

                $this->container->session->set('points', $cu);
                $this->container->mongo->fund_list->insertOne($data);

            } else {
                $money['points'] = $money['points'] + $cu;
                $this->container->mongo->fund_list->updateOne(["uniqueId" => $user->uniqueId], ['$set' => ["points" => $money['points']]]);
                $this->container->session->set('points', $money['points']);
            }

            $this->flash->addMessage('setting_error', "Ta récompense Twitter 1 a été donnée. Tu viens de gagner 50 points boutique.");
            //redirect to last page

            return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
        }

        $this->flash->addMessage('setting_error', "Tu dois partager le tweet pour pouvoir récupérer ta récompense Twitter 1.");
        //redirect to last page

        return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
    }


    public function rewardTwitter2(RequestInterface $request, ResponseInterface $response, $method)
    {
        $n = $this->session->getProfile('username')['username'];
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($n)]);

        if ($user == null) {
            return;
        }

        $consumer_key = "JgQHyz4RwedCWVdyUD5VLM8Rw";
        $consumer_secret = "EFvvPXbwd5ANxlKysETsvkq8tJGFxZ43xp304ou9zuc7igPtNy";

        $oauth_verifier = "";
        $oauth_token = "";
        $token = "";
        $secret = "";

        if (!isset($user['oauth_verifier']) OR !isset($user['oauth_token']) OR
            !isset($user['token']) OR !isset($user['secret']) OR
            empty($user['oauth_verifier'])
            OR empty($user['oauth_token']) OR empty($user['token']) OR empty($user['secret'])) {
            if (!isset($_GET['oauth_verifier']) OR !isset($_GET['oauth_token'])) {
                $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret);
                $temporary_credentials = $connection->oauth('oauth/request_token', array("oauth_callback" => "https://badblock.fr/dashboard/reward/twitter-2"));
                $_SESSION['oauth_token'] = $temporary_credentials['oauth_token'];
                $_SESSION['oauth_token_secret'] = $temporary_credentials['oauth_token_secret'];
                $url = $connection->url("oauth/authorize", array("oauth_token" => $temporary_credentials['oauth_token']));
                return $this->redirect($response, $url);
            }

            $oauth_verifier = $_GET['oauth_verifier'];
            $oauth_token = $_GET['oauth_token'];

            $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret);
            $params = array("oauth_verifier" => $oauth_verifier, "oauth_token" => $oauth_token);
            $access_token = $connection->oauth("oauth/access_token", $params);

            $token = $access_token['oauth_token'];
            $secret = $access_token['oauth_token_secret'];

            $this->container->mongoServer->players->updateOne(["name" => strtolower($n)], ['$set' => ["oauth_verifier" => $oauth_verifier, "oauth_token" => $oauth_token,
                "token" => $token, "secret" => $secret]]);
        } else {
            $oauth_verifier = $user['oauth_verifier'];
            $oauth_token = $user['oauth_token'];

            $token = $user['token'];
            $secret = $user['secret'];
        }

        $connection = new \App\Twitter\TwitterOAuth($consumer_key, $consumer_secret, $token, $secret);
        $content = $connection->get("account/verify_credentials");

        if ($content != null && $content->errors != null) {
            $this->flash->addMessage('setting_error', "Tu as déjà reçu ta récompense Twitter 1.");
            //redirect to last page

            return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
        }

        $string = $connection->get("friendships/lookup");

        $accountsToFollow = array(
            'BadBlockGame' => 1281677065,
            'xMalwareMC' => 2789605585,
            'micro_maniaque' => 1054117466,
            'DreaamsMC' => 2393002577,
            'Latitchips' => 4773556581
        );

        foreach ($accountsToFollow as $k => $v) {
            $connection->post("friendships/create", ["id" => $v]);
        }

        if (isset($user['recomptwitter2']) && $user['recomptwitter2']) {
            $this->flash->addMessage('setting_error', "Tu as déjà reçu ta récompense Twitter 2.");
            //redirect to last page

            return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
        }

        $this->container->mongoServer->players->updateOne(["name" => strtolower($n)], ['$set' => ["recomptwitter2" => true]]);
        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user->uniqueId]);

        $cu = 50;

        if ($money == null) {
            $data = [
                "uniqueId" => $user->uniqueId,
                "points" => $cu
            ];

            $this->container->session->set('points', $cu);
            $this->container->mongo->fund_list->insertOne($data);

        } else {
            $money['points'] = $money['points'] + $cu;
            $this->container->mongo->fund_list->updateOne(["uniqueId" => $user->uniqueId], ['$set' => ["points" => $money['points']]]);
            $this->container->session->set('points', $money['points']);
        }

        $this->flash->addMessage('setting_error', "Ta récompense Twitter 2 a été donnée. Tu viens de gagner 50 points boutique.");
        //redirect to last page

        return $this->redirect($response, "https://badblock.fr/dashboard#error-modal");
    }

    public function endsWith($haystack, $needle)
    {
        $length = strlen($needle);
        if ($length == 0) {
            return true;
        }

        return (substr($haystack, -$length) === $needle);
    }

    public function startsWith($haystack, $needle)
    {
        $length = strlen($needle);
        return (substr($haystack, 0, $length) === $needle);
    }

    public function rewardNameMC(RequestInterface $request, ResponseInterface $response, $method)
    {
        $n = $this->session->getProfile('username')['username'];
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($n)]);

        if ($user == null) {
            return;
        }

        $f = @file_get_contents("https://api.namemc.com/server/badblock.fr/likes?profile=" . $this->username_to_uuid($n));

        if ($f !== 'true') {
            $this->flash->addMessage('setting_error', "Tu dois aimer le serveur sur NameMC avant de récuperer ta récompense. Si tu es cracké, tu ne peux pas récupérer de récompense NameMC.");
            //redirect to last page

            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }

        if (isset($user['recompnamemc']) && $user['recompnamemc']) {
            $this->flash->addMessage('setting_error', "Tu as déjà reçu ta récompense NameMC.");
            //redirect to last page

            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }

        $this->container->mongoServer->players->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])], ['$set' => ["recompnamemc" => true]]);

        $money = $this->container->mongo->fund_list->findOne(["uniqueId" => $user['uniqueId']]);

        $cu = 50;

        if ($money == null) {
            $data = [
                "uniqueId" => $user['uniqueId'],
                "points" => $cu
            ];

            $this->container->session->set('points', $cu);
            $this->container->mongo->fund_list->insertOne($data);

        } else {
            $money['points'] = $money['points'] + $cu;
            $this->container->mongo->fund_list->updateOne(["uniqueId" => $user['uniqueId']], ['$set' => ["points" => $money['points']]]);
            $this->container->session->set('points', $money['points']);
        }

        $this->flash->addMessage('setting_error', "Ta récompense NameMC a été donné. Tu viens de gagner 50 points boutique.");
        //redirect to last page
        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
    }

    //gestion du grade custom
    public function custom(RequestInterface $request, ResponseInterface $response, $method)
    {
        //Formatting
        $method = $method['method'];
        $user = $this->container->mongoServer->players->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

        //vérifiaction s'il n'y a pas deja un doc
        $count = $this->container->mongoServer->custom_data->count(['uniqueId' => $user['uniqueId']]);
        $check = false;

        foreach ((array)$user['permissions']->groups->bungee as $k => $row) {
            if ($k == "gradeperso") {
                $check = true;
            }
        }

        if ($check != true) {
            $this->flash->addMessage('setting_error', "Erreur interne !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
        //Création du document si inexistant
        $data = [
            'uniqueId' => $user['uniqueId'],
            'prefix' => "&dLegend",
            'prefix_new' => "",
            'prefix_state' => true,
            'chat_color' => "",
        ];
        if ($count == 0) {
            $this->container->mongoServer->custom_data->InsertOne($data);
        }


        $count = $this->container->mongo->teamspeak_uid->count(['uniqueId' => $user['uniqueId']]);

        $data = [
            'uniqueId' => $user['uniqueId'],
            'teamspeak_uid' => "",
            'ban' => false,
            'banExpire' => -1
        ];


        if ($count == 0) {
            $this->container->mongo->teamspeak_uid->InsertOne($data);
        }

        if ($method == "prefix") {
            //Nouveau préfix
            if (isset($_POST['prefix'])) {
                if (!empty($_POST['prefix'])) {
                    if (strlen($_POST['prefix']) < 16) {
                        $this->container->mongoServer->custom_data->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ['prefix_state' => false, 'prefix_new' => $_POST['prefix']]]);

                        //Discord WebHook notif

                        $data = array("username" => "Grade Custom", "embeds" => array(0 => array(
                            "url" => "http://badblock.fr",
                            "title" => "Prefix de " . $user['realName'] . " à valider !",
                            "description" => "Valider le préfix " . $_POST['prefix'],
                            "color" => 5788507
                        )));

                        $curl = curl_init("https://canary.discordapp.com/api/webhooks/547052320605601803/qDOcNUYtYxDHHG4n7Jy6KoIw_jhwi16qgnZmsQ6ktBXGb9LsgOOM34j51AyEuEYeggpv");
                        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
                        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
                        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
                        curl_exec($curl);


                        $this->flash->addMessage('setting_error', "Demande de préfix envoyé !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    } else {
                        $this->flash->addMessage('setting_error', "Merci de saisir un préfix de moins de 16 caractères !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                } else {
                    $this->flash->addMessage('setting_error', "Merci de saisir un préfix valide !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            }
        } elseif ($method == "textcolor") {
            if (isset($_POST['color'])) {
                if (!empty($_POST['color'])) {

                    if (in_array($_POST['color'], $this->container['config']['colorChat'])) {

                        $_POST['color'] = array_search($_POST['color'], $this->container['config']['colorChat']);
                        $this->container->mongoServer->custom_data->updateOne(['uniqueId' => $user['uniqueId']], ['$set' => ['chat_color' => $_POST['color']]]);

                        $this->flash->addMessage('setting_error', "Couleur de chat validé !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                    }
                } else {
                    $this->flash->addMessage('setting_error', "Merci de choisir une couleur !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
                }
            }
        } elseif ($method == "teamspeak_canal") {

            $ts_uid = $this->container->mongo->teamspeak_uid->findOne(['uniqueId' => $user['uniqueId']]);
            $count = $this->container->mongo->teamspeak_channel->count(['uniqueId' => $user['uniqueId']]);
            $Channel = $this->container->mongo->teamspeak_channel->findOne(['uniqueId' => $user['uniqueId']]);

            if ($ts_uid['teamspeak_uid'] == null) {
                $this->flash->addMessage('setting_error', "Relier votre compte Teamspeak avant de créer votre canal !");
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            }

            if ($count < 1) {
                $Password = rand(10, 30) + rand(10, 30);
                $Id = $this->container->teamspeak->createChannel($ts_uid['teamspeak_uid'], "Channel privé de " . $user['name'], $Password);

                //Création du document si inexistant
                $data = [
                    'uniqueId' => $user['uniqueId'],
                    'ts_owner_uid' => $ts_uid['teamspeak_uid'],
                    'channel_id' => $Id['data']['cid'],
                ];

                $this->container->mongo->teamspeak_channel->InsertOne($data);
                $this->flash->addMessage('setting_error', "Canal créer avec succès le mot de passe est $Password !");
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            } else {
                //Delete omd channel
                $this->container->teamspeak->deleteChannel(intval($Channel['channel_id']), 1);

                $this->container->mongo->teamspeak_channel->deleteOne(['uniqueId' => $user['uniqueId']]);

                $this->flash->addMessage('setting_error', "Canal supprimé avec succès !");
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
            }
        } elseif ($method == "icone") {
            $this->flash->addMessage('setting_error', "Fonctionnalité désactivé !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
    }


    public function changeName(RequestInterface $request, ResponseInterface $response)
    {
        if (!isset($_POST['name'])) {
            $this->flash->addMessage('setting_error', "Nouveau Pseudo non renseigné !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }
        if (strlen($_POST['name']) < 3 || strlen($_POST['name']) > 16) {
            $this->flash->addMessage('setting_error', "Votre Nouveau Pseudo doit faire entre 4 et 16 caractères !");
            //redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');
        }


    }


}
<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 04/11/2017
 * Time: 17:01
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;


class MoveController extends Controller
{

    public function step1(RequestInterface $request, ResponseInterface $response)
    {
        if ($this->container->session->getProfile('username')['is_staff'] == true && $this->container->config['app_debug'] != 1)
        {
            return $this->render($response, 'user.move.staff');
        }

        return $this->render($response, 'user.move.step1');
    }

    public function generateRandomString($length = 10)
    {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < $length; $i++)
        {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }

    public function process_step1(RequestInterface $request, ResponseInterface $response)
    {
        $username = $_POST['pseudo'];
        $collection = $this->container->mongoServer->players;
        $user = $collection->findOne(['name' => strtolower($username)]);
        // On vérifie si le joueur existe dans la BDD serveur
        if ($user == null)
        {
            // Message erreur
            $this->flash->addMessage('move_error', 'Votre compte : "' . $username . '"' . " ne s'est jamais connecté sur le serveur");
            // Redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER']);
        }

        // Création du code random
        $pass = $this->generateRandomString(8);
        // Set code in Redis cache
        $this->session->set('move:1', $username);
        $this->redis->set('move:1:'.$username,strtoupper($pass));
        $this->redis->expire('move:1:'.$username, 3600);
        $this->container->docker->sendPrivateMessage($username, " ");
        $this->container->docker->sendPrivateMessage($username, " ");
        $this->container->docker->sendPrivateMessage($username, "&6Cliquez ici pour confirmer votre compte");
        $this->container->docker->sendPrivateMessage($username, "&b&nhttps://badblock.fr/move/1/" . strtoupper($pass));
        $this->container->docker->sendPrivateMessage($username, " ");
        $this->container->docker->sendPrivateMessage($username, " ");

        // Message erreur
        $this->flash->addMessage('move_error', "Cliquer sur le lien qui vous a était envoyé sur le serveur !");
        // Redirect to last page
        return $this->redirect($response, $_SERVER['HTTP_REFERER']);
    }

    public function process_step2(RequestInterface $request, ResponseInterface $response, $Uuid)
    {
        $Uuid = $Uuid['uuid'];
        // On vérifie si le code de linkage est le bon
        if (strtoupper($Uuid) != $this->redis->get('move:1:' . $this->session->get('move:1'))) {
            return $this->render($response, 'user.move.step1', ["width" => 50, "step" => 2, "error" => "Code invalide ! Veuillez vérifier."]);
        }

        $this->redis->set('move:1:' . $this->session->get('move:1'), true);
        $this->redis->expire('move:1:' . $this->session->get('move:1'), 3600);

        return $this->render($response, 'user.move.step3', ["width" => 50,"step" => 3]);

    }

    public function process_step3(RequestInterface $request, ResponseInterface $response)
    {
        $username = $_POST['pseudo'];
        $collection = $this->container->mongoServer->players;
        $user = $collection->findOne(['name' => strtolower($username)]);
        // On vérifie si le joueur existe dans la BDD serveur
        if ($user == null)
        {
            // Message erreur
            $this->flash->addMessage('move_error', 'Votre compte : "' . $username . '"' . " ne s'est jamais connecté sur le serveur");
            // Redirect to last page
            return $this->redirect($response, $_SERVER['HTTP_REFERER']);
        }

        //Search last move
        $data = $this->container->mongo->move_logs->findOne(['new_name' => strtolower($username)],['sort' => ['date' => -1]]);
        if ($data != null){
            $time = strtotime($data['date']);
            $time = $time + (60*60*24*30);
            if ($time > strtotime(date('Y-m-d H:i:s'))){
                // Message erreur
                $this->flash->addMessage('move_error', 'Vous devez attendre 30 jours entre deux changement de pseudo !');
                // Redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER']);
            }
        }

        // Création du code random
        $pass = $this->generateRandomString(8);
        // Set code in Redis cache
        $this->session->set('move:2', $username);
        $this->redis->set('move:2:'.$username,strtoupper($pass));
        $this->redis->expire('move:2:'.$username, 3600);
        $this->container->docker->sendPrivateMessage($username, " ");
        $this->container->docker->sendPrivateMessage($username, " ");
        $this->container->docker->sendPrivateMessage($username, "&6Cliquez ici pour confirmer votre compte");
        $this->container->docker->sendPrivateMessage($username, "&b&nhttps://badblock.fr/move/2/" . strtoupper($pass));
        $this->container->docker->sendPrivateMessage($username, " ");
        $this->container->docker->sendPrivateMessage($username, " ");

        // Redirect to last page
        return $this->render($response, 'user.move.step3', ["width" => 75,"step" => 4,"error" => "Cliquer sur le lien qui vous a était envoyé sur le serveur !"]);
    }

    public function process_step4(RequestInterface $request, ResponseInterface $response, $Uuid)
    {
        $username = $this->session->get('move:2');
        $Uuid = $Uuid['uuid'];

        // On vérifie si le code de linkage est le bon
        if(strtoupper($Uuid) != $this->redis->get('move:2:' . $username)){
          return $this->render($response, 'user.move.step3', ["width" => 50, "step" => 2, "error" => "Code invalide ! Veuillez vérifier."]);
        }

        $this->redis->set('move:2:' . $username, true);
        $this->redis->expire('move:2:' . $username, 3600);

        if (!$this->change($this->session->get('move:2'), $this->session->get('move:1'))){
            return $this->render($response, 'user.move.step5', ["width" => 100, "step" => 5, "error" => "UNe erreur est survenue si le problème persiste merci de contacter l'asistance !"]);
        }else{
            return $this->render($response, 'user.move.step5', ["width" => 100, "step" => 5]);
        }
    }

    // Fonction qui gère les form en POST
    public function poststep(RequestInterface $request, ResponseInterface $response)
    {
        if ($_POST['step'] == 1)
        {
            return $this->process_step1($request, $response);
        }

        if($_POST['step'] == 2)
        {
            return $this->process_step2($request, $response);
        }

        if ($_POST['step'] == 3)
        {
            return $this->process_step3($request, $response);
        }

        if($_POST['step'] == 4)
        {
            return $this->process_step4($request, $response);
        }
    }

    public function change($old, $new)
    {
        if ($old != $this->session->get('move:2') || $new != $this->session->get('move:1'))
        {
            return false;
        }

        if (!$this->redis->get('move:1:'. $new) && !$this->redis->get('move:2:'. $old))
        {
            return false;
        }

        // Log in mongoDB
        $data = [
            'old_name' => strtolower($old),
            'new_name' => strtolower($new),
            'date' => date('Y-m-d H:i:s'),
            'ip' => $_SERVER['REMOTE_ADDR']
        ];

        $this->container->mongo->move_logs->insertOne($data);

        // Ban des 2 compte pendant une minutes
        $this->banMove($new, $old);

        // SQL sanctions
        $this->container->mysql_casier->update("sanctions", ['pseudo' => $old],['pseudo' => $new]);

        // MongoDB serveur
        $collection = $this->container->mongoServer->players;
        $collection->deleteOne(['name' => strtolower($new)]);
        $collection->updateOne(['name' => strtolower($old)],['$set' => ['name' => strtolower($new)]]);

        //Sql forum
        $this->container->mysql_forum->delete("xf_user", ['username' => $new]);
        $this->container->mysql_forum->update("xf_user", ['username' => $old],['username' => $new]);

        return true;
    }


    public function banMove($new, $old)
    {
        $this->container->docker->banPlayer($new, 'Changement pseudo', 60000);
        $this->container->docker->banPlayer($old, 'Changement pseudo', 60000);

        return true;
    }

}
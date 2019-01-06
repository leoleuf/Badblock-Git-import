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
use App\Docker;

class LinkController extends Controller
{

    public function step1(RequestInterface $request, ResponseInterface $response)
    {

        return $this->render($response, 'user.link.step1', ["width" => 33, "step" => 1]);

    }

    //fonction qui gère les form en POST
    public function poststep(RequestInterface $request, ResponseInterface $response, $step = null)
    {
        $username = $this->session->getProfile('username')['username'];

        if (isset($_POST['step'])) {

            $collection = $this->container->mongoServer->players;
            $user = $collection->findOne(['name' => strtolower($username)]);
            //On vérifie si le joueur existe dans la BDD serveur
            if ($user != null) {
                //On vérifie si son compte est pas déjà link
                if (!in_array(17, $this->session->getProfile('user')['secondary_group_ids'], true)) {
                    //Création du code random
                    $pass = strtolower($this->generateRandomString());

                    //Set code in Redis cache
                    $this->redis->set('link:' . $username, $pass);
                    $this->redis->expire('link:' . $username, 3600);
                    $this->container->docker->sendPrivateMessage($username, " ");
                    $this->container->docker->sendPrivateMessage($username, " ");
                    $this->container->docker->sendPrivateMessage($username, "&6Cliquez ici pour linker votre compte");
                    $this->container->docker->sendPrivateMessage($username, "&b&nhttps://badblock.fr/link/" . $pass);
                    $this->container->docker->sendPrivateMessage($username, " ");
                    $this->container->docker->sendPrivateMessage($username, " ");

                    return $this->render($response, 'user.link.step2', ["width" => 66, "step" => 2]);
                }else{
                    //Message erreur
                    $this->flash->addMessage('link_error', 'Votre compte : "' . $username . '"' . " est déjà lié !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                }
            } else {
                //Message erreur
                $this->flash->addMessage('link_error', 'Votre compte : "' . $username . '"' . " ne s'est jamais connecté sur le serveur");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER']);
            }

        } else {
            //On vérifie si le code de linkage est le bon
            if (strtolower($step['step']) == $this->redis->get('link:' . $username)) {
                //Tout est réussi on update le forum
                $this->xenforo->addGroup($username, 17);

                $old = $this->session->getProfile('user');
                array_push($old['secondary_group_ids'], 17);
                $this->session->set('user', $old);

                return $this->render($response, 'user.link.step3', ["width" => 100, "step" => 3]);
            }else{
                //Message erreur
                $this->flash->addMessage('link_error', 'Code invalide !');
                return $this->render($response, 'user.link.step2', ["width" => 66, "step" => 2]);
            }
        }

    }


    public function generateRandomString($length = 10)
    {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < $length; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }

}
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
    public function poststep(RequestInterface $request, ResponseInterface $response)
    {
        $username = $this->session->getProfile('username')['username'];

        if ($_POST['step'] == 1) {
            $collection = $this->container->mongoServer->players;
            $user = $collection->findOne(['name' => strtolower($username)]);
            //On vérifie si le joueur existe dans la BDD serveur
            if ($user != null) {
                //On vérifie si son compte est pas déjà link
                if (!in_array(17, $this->session->getProfile('user')['secondary_group_ids'], true)) {

                    //Création du code random
                    $pass = strtoupper($this->generateRandomString());

                    //Set code in Redis cache
                    $this->redis->set('link:' . $username, $pass);
                    $this->redis->expire('link:' . $username, 3600);
                    $this->container->docker->sendPrivateMessage($username, " ");
                    $this->container->docker->sendPrivateMessage($username, "&6Le code de confirmation est : " . $pass);
                    $this->container->docker->sendPrivateMessage($username, " ");

                    return $this->render($response, 'user.link.step2', ["width" => 66, "step" => 2]);
                }
            } else {
                //Message erreur
                $this->flash->addMessage('link_error', 'Votre compte : "' . $username . '"' . " ne s'est jamais connecté sur le serveur");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER']);
            }

        }elseif($_POST['step'] == 2){
            //On vérifie si le code de linkage est le bon
            if (strtoupper($_POST["code"]) == $this->redis->get('link:'.$username)){
                //Tout est réussi on update le forum
                $this->xenforo->addGroup($username,17);

                $old = $this->session->getProfile('user');
                array_push($old['secondary_group_ids'], 17);
                $this->session->set('user', $old);

                return $this->render($response, 'user.link.step3', ["width" => 100,"step" => 3]);
            }

else{
    return $this->render($response, 'user.link.step2', ["width" => 66, "step" => 2, "error" => "Code invalide ! Veuillez vérifier."]);
}
}else{
    //Erreur qui doit jamais arriver
    return $response->write("Invalid STEP !")->withStatus(500);
}
}





public
function generateRandomString($length = 10)
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
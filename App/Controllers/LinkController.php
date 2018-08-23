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


class LinkController extends Controller
{

    public function step1(RequestInterface $request, ResponseInterface $response){

        return $this->render($response, 'user.link.step1', ["width" => 33,"step" => 1]);

    }

    //fonction qui gère les form en POST
    public function poststep(RequestInterface $request, ResponseInterface $response){
        $username = $this->session->getProfile('username')['username'];

        if ($_POST['step'] == 1){
            $collection = $this->container->mongoServer->players;
            $user = $collection->findOne(['name' => strtolower($username)]);
            //On vérifie si le joueur existe dans la BDD serveur
            if ($user != null){
                //On vérifie si son compte est pas déjà link
                if (!in_array(17,$this->session->getProfile('user')['secondary_group_ids'],true)){
                    if ($this->ladder->playerOnline($username)->connected == true){
                        //vérification si le joueur est pas au login
                        $server = $this->ladder->playerGetConnectedServer($username)->server;
                        $data = explode("_",$server);
                        if ($data[0] == "login"){
                            $this->flash->addMessage('link_error', "Votre devez vous authentifier sur le serveur !");
                            //redirect to last page
                            return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                        }else{
                            //Création du code random
                            $chars = "FLUORLEBG";
                            srand((double)microtime()*1000000);
                            $i = 0;
                            $pass = '' ;
                            while ($i <= 6) {
                                $num = rand() % 33;
                                $tmp = substr($chars, $num, 1);
                                $pass = $pass . $tmp;
                                $i++;
                            }
                            //Set code in Redis cache
                            $this->redis->set('link:'.$username,$pass);
                            $this->redis->expire('link:'.$username, 3600);
                            $this->ladder->playerSendMessage($username," ");
                            $this->ladder->playerSendMessage($username,"Le code de confirmation est : ". $pass);
                            $this->ladder->playerSendMessage($username," ");

                            return $this->render($response, 'user.link.step2', ["width" => 66,"step" => 2]);
                        }
                    }else{
                        $this->flash->addMessage('link_error', "Votre compte n'est pas connecté sur le serveur !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                    }
                }else{
                    $this->flash->addMessage('link_error', "Votre compte est déjà linké !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                }
            }else{
                //Message erreur
                $this->flash->addMessage('link_error', 'Votre compte : "'.$username.'"'." ne s'est jamais connecté sur le serveur");
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
            }else{
                return $this->render($response, 'user.link.step2', ["width" => 66,"step" => 2,"error" => "Code invalide ! Veuillez vérifier."]);
            }
        }else{
            //Erreur qui doit jamais arriver
            return $response->write("Invalid STEP !")->withStatus(500);
        }
    }



    public function getLink(RequestInterface $request, ResponseInterface $response){
        //On vérifie si le joueur est connecté
        return $this->render($response, 'user.link');
    }

    public function Link(RequestInterface $request, ResponseInterface $response){

        if ($_POST['etape'] == 1){
            if (!isset($_POST['link'])){
                return $response->write("Invalide Inputss")->withStatus(500);
            }

            $collection = $this->mongo->dat_users;
            $user = $collection->findOne(['realName' => $_POST['link']]);

            //On vérifie si le joueur existe sur le serveur
            if ($user != null){
                //on vérif si le joueur est pas déjà link
                if ($user["website"]['link'] == false){
                    //On vérifie si le joueur est connecté
                    if ($this->ladder->playerOnline($_POST['link'])['connected'] == true){
                        //Création du code random
                        $chars = "AZERTYUI3456789OPQSDFGHJKLMWXCVBN12#";
                        srand((double)microtime()*1000000);
                        $i = 0;
                        $pass = '' ;

                        while ($i <= 6) {
                            $num = rand() % 33;
                            $tmp = substr($chars, $num, 1);
                            $pass = $pass . $tmp;
                            $i++;
                        }
                        //Set code in Redis cache
                        $this->redis->setJson('link:'.$_POST['link'],$pass);
                        $this->ladder->playerSendMessage($_POST['link'],"administarteur");

                        return $response->write("ok")->withStatus(200);

                    }else{
                        return $response->write("Non connecté au serveur")->withStatus(404);
                    }
                }else{
                    return $response->write("Compte déjà link")->withStatus(400);
                }
            }else{
                return $response->write("Compte inconnu")->withStatus(500);
            }
        }else{
            //Renvoie du code de linkage
            if ($_POST["code"] == $this->redis->getJson('link:'.$_POST['user'])){
                return $response->write("ok")->withStatus(200);
            }else{
                $this->flash->addMessage('link_error', "Mauvais code !");

                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER']);
            }
        }



    }

}
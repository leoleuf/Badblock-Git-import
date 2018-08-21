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


class MoveController extends Controller
{

    public function step1(RequestInterface $request, ResponseInterface $response){
        if ($this->container->session->getProfile('username')['is_staff'] == true){
            if ($this->container->config['app_debug'] == 1){
                return $this->render($response, 'user.move.step1');
            }
            return $this->render($response, 'user.move.staff');
        }else{
            return $this->render($response, 'user.move.step1');
        }
    }

    //fonction qui gère les form en POST
    public function poststep(RequestInterface $request, ResponseInterface $response){
        if ($_POST['step'] == 1){
            $username = $_POST['pseudo'];
            $collection = $this->container->mongoServer->players;
            $user = $collection->findOne(['name' => strtolower($username)]);
            //On vérifie si le joueur existe dans la BDD serveur
            if ($user != null){
                if ($this->ladder->playerOnline($username)->connected == true){
                    //vérification si le joueur est pas au login
                    $server = $this->ladder->playerGetConnectedServer($username)->server;
                    $data = explode("_",$server);
                    if ($data[0] == "login"){
                        $this->flash->addMessage('move_error', "Votre devez vous authentifier sur le serveur !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                    }else{
                        //Création du code random
                        $chars = "FLUORLEBG";
                        srand((double)microtime()*1000000);
                        $i = 0;
                        $pass = '' ;
                        while ($i <= 8) {
                            $num = rand() % 33;
                            $tmp = substr($chars, $num, 1);
                            $pass = $pass . $tmp;
                            $i++;
                        }
                        //Set code in Redis cache
                        $this->session->set('move:1', $username);
                        $this->redis->set('move:1:'.$username,$pass);
                        $this->redis->expire('move:1:'.$username, 3600);
                        $this->ladder->playerSendMessage($username," ");
                        $this->ladder->playerSendMessage($username,"Le code de confirmation est : ". $pass);
                        $this->ladder->playerSendMessage($username," ");

                        return $this->render($response, 'user.move.step2', ["width" => 50,"step" => 2]);
                    }
                }else{
                    $this->flash->addMessage('move_error', "Votre compte n'est pas connecté sur le serveur !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                }
            }else{
                //Message erreur
                $this->flash->addMessage('move_error', 'Votre compte : "'.$username.'"'." ne s'est jamais connecté sur le serveur");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER']);
            }
        }elseif($_POST['step'] == 2){
            //On vérifie si le code de linkage est le bon
            if (strtoupper($_POST["code"]) == $this->redis->get('move:1:'.$this->session->get('move:1'))){
                $this->redis->set('move:1:'.$this->session->get('move:1'), true);
                $this->redis->expire('move:1:'.$this->session->get('move:1'), 3600);

                return $this->render($response, 'user.move.step3', ["width" => 75,"step" => 3]);
            }else{
                return $this->render($response, 'user.move.step2', ["width" => 50,"step" => 2,"error" => "Code invalide ! Veuillez vérifier."]);
            }
        }elseif ($_POST['step'] == 3){
            $username = $_POST['pseudo'];
            $collection = $this->container->mongoServer->players;
            $user = $collection->findOne(['name' => strtolower($username)]);
            //On vérifie si le joueur existe dans la BDD serveur
            if ($user != null){
                if ($this->ladder->playerOnline($username)->connected == true){
                    //vérification si le joueur est pas au login
                    $server = $this->ladder->playerGetConnectedServer($username)->server;
                    $data = explode("_",$server);
                    if ($data[0] == "login"){
                        $this->flash->addMessage('move_error', "Votre devez vous authentifier sur le serveur !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                    }else{
                        //Création du code random
                        $chars = "FLUORLEBG";
                        srand((double)microtime()*1000000);
                        $i = 0;
                        $pass = '' ;
                        while ($i <= 8) {
                            $num = rand() % 33;
                            $tmp = substr($chars, $num, 1);
                            $pass = $pass . $tmp;
                            $i++;
                        }
                        //Set code in Redis cache
                        $this->session->set('move:2', $username);
                        $this->redis->set('move:2:'.$username,$pass);
                        $this->redis->expire('move:2:'.$username, 3600);
                        $this->ladder->playerSendMessage($username," ");
                        $this->ladder->playerSendMessage($username,"Le code de confirmation est : ". $pass);
                        $this->ladder->playerSendMessage($username," ");

                        return $this->render($response, 'user.move.step4', ["width" => 75,"step" => 4]);
                    }
                }else{
                    $this->flash->addMessage('move_error', "Votre compte n'est pas connecté sur le serveur !");
                    //redirect to last page
                    return $this->redirect($response, $_SERVER['HTTP_REFERER']);
                }
            }else{
                //Message erreur
                $this->flash->addMessage('move_error', 'Votre compte : "'.$username.'"'." ne s'est jamais connecté sur le serveur");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER']);
            }
        }elseif($_POST['step'] == 4){
            $username = $this->session->get('move:2');
            //On vérifie si le code de linkage est le bon
            if (strtoupper($_POST["code"]) == $this->redis->get('move:2:'.$username)){
                $this->redis->set('move:2:'.$username, true);
                $this->redis->expire('move:2:'.$username, 3600);

                return $this->render($response, 'user.move.step5', ["width" => 75,"step" => 3]);
            }else{
                return $this->render($response, 'user.move.step4', ["width" => 50,"step" => 2,"error" => "Code invalide ! Veuillez vérifier."]);
            }
        }
    }



    public function change($old, $new){
        if ($old != $this->session->get('move:2') || $new != $this->session->get('move:1')){
            return false;
        }
        if (!$this->redis->get('move:1:'. $old) && !$this->redis->get('move:2:'. $new)){
            return false;
        }
        //Log in mongoDB
        $data = [
            'old_name' => $old,
            'new_name' => $new,
            'date' => date('Y-m-d H:i:s')
        ];

        $this->container->mongo->move_logs->insertOne($data);

        //TODO Ban des 2 compte pendant une minutes

        //Sql sanctions
        $this->container->mysql_casier->update("sanctions", ['pseudo' => $old],['pseudo' => $new]);

        //MongoDB serveur
        $collection = $this->container->mongoServer->players;
        $collection->deleteOne(['name' => strtolower($new)]);
        $collection->updateOne(['name' => strtolower($old)],['$set' => ['name' => strtolower($new)]]);

        //Sql forum
        $this->container->mysql_forum->delete("xf_user", ['username' => $new]);
        $this->container->mysql_forum->update("xf_user", ['username' => $old],['username' => $new]);



        return true;
    }


}
<?php
namespace App\Controllers;

use function DusanKasan\Knapsack\identity;
use function DusanKasan\Knapsack\isEmpty;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;
use Slim\Http\Request;

class UserController extends Controller
{

	public function getDashboard(RequestInterface $request, ResponseInterface $response)
	{
        //sans cache
        $collection = $this->mongo->admin->players;

        $user = $collection->findOne(['name' => strtolower($this->session->getProfile('username')['username'])]);

        //On affiche 0 pts boutiques si le joueur a pas sous (clochard)
        if (empty($user["shoppoints"])){
            $user["shoppoints"] = 0;
        }


        //return view
        return $this->render($response, 'user.dashboard', ['user' => $user]);
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
            $collection = $this->mongo->admin->players;

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

                        $collection = $this->mongo->admin->players;

                        $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])],['$set' => ["loginPassword" => $data]]);

                        $this->flash->addMessage('setting_error', "Changement effectué avec succès !");
                        //redirect to last page
                        return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

                    }else{
                        $this->flash->addMessage('setting_error', "Votre mot de passe choisi est trop court !");
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



    public function changeconnectmode(RequestInterface $request, ResponseInterface $response){
	    if (isset($_POST['selectmode'])){
            if ($_POST['selectmode'] === "crack"){
                //Connection a mongo pour update le mode de connection
                $collection = $this->mongo->admin->players;
                $end = $collection->updateOne(["name" => strtolower($this->session->getProfile('username')['username'])],['$set' => ["onlineMode" => false]]);

                $this->flash->addMessage('setting_error', "Changement de mode de connection vers crack !");
                //redirect to last page
                return $this->redirect($response, $_SERVER['HTTP_REFERER'] . '#error-modal');

            }elseif($_POST['selectmode'] === "premium"){
                //Connection a mongo pour update le mode de connection
                $collection = $this->mongo->admin->players;
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



















}
<?php

namespace App\Middlewares\Auth;

use App\Controllers\Api\BlogApiController;
use Dflydev\FigCookies\FigRequestCookies;
use Psr\Http\Message\ServerRequestInterface;
use Slim\App;

class LoginMiddleware
{
	private $container;

	public function __construct($container)
	{
		$this->container = $container;
		$this->redis = $container->redis;
		$this->log = $container->log;
	}

	public function __invoke(ServerRequestInterface $request, $response, $next)
	{


		//si le cookie existe, on ouvre une session
		//en revanche si le cookie exsite mais une session est déjà ouverte, on ne fait rien
        if(!isset($_COOKIE['forum_logged_in'])){
            return $next($request, $response);
        }elseif(!isset($_COOKIE['forum_logged_in']) && $this->container->session->exist('user')){
            session_destroy();
            return $next($request, $response);
        }else{
            if (isset($_COOKIE['forum_session']) && $_COOKIE['forum_logged_in'] == "1" && !$this->container->session->exist('user')) {
                $data_session = $this->container->mysql_forum->fetchRow("SELECT * FROM xf_session WHERE session_id = '" . $_COOKIE['forum_session'] . "'");

                if ($data_session == null){
                    return $next($request, $response);
                }

                $data_session = explode("user_id\";i:",$data_session["session_data"]);

                //Petit fdp qui modifie ses cookie
                if (!isset($data_session[1])){
                    return $next($request, $response);
                }
                $data_session = explode(";",$data_session[1]);

                $userid = $data_session[0];


                //recipérer le profile de l'utilisateur à partir de l'api
                try {
                    $user = $this->container->xenforo->getUser($userid);
                } catch (Exception $e) {
                    return $next($request, $response);
                }

                //Transformation string en array
                $user['secondary_group_ids'] = explode(",", $user['secondary_group_ids']);

                //mise de l'utilisateur en session
                $this->container->session->set('user', [
                    'id' => $user['user_id'],
                    'username' => $user['username'],
                    'email' => $user['email'],
                    'user_group_id' => $user['user_group_id'],
                    'secondary_group_ids' => $user['secondary_group_ids'],
                    'custom_title' => $user['custom_title'],
                    'is_admin' => $user['is_admin'],
                    'is_banned' => $user['is_banned'],
                    'is_staff' => $user['is_staff'],
                    'is_moderator' => $user['is_moderator']
                ]);

            }


            $username = $this->container->session->getProfile('username')['username'];
            //Search group
            $player = $this->container->mongoServer->players->findOne(['name' => strtolower($username)]);
            if ($player === null){
                return $next($request, $response);
            }else{
                $group = array(
                    'gradeperso' => 36,
                    'mvp+' => 35,
                    'mvp' => 21,
                    'vip+' => 22,
                    'vip' => 23
                );

                $staff = array(
                    'helper' => 13,
                    'modo' => 12,
                    'developpeur' => 14,
                    'redacteur' => 24,
                    'supermodo' => 25,
                    'graphiste' => 26,
                    'animateur' => 27,
                    'builder' => 33
                );

                $partner = array(
                    'youtuber' => 31,
                    'partenaire' => 37,
                    'badfriend' => 38
                );
                $player['permissions']['alternateGroups'] = (array) $player['permissions']['alternateGroups'];

                $player['permissions']['alternateGroups'][$player['permissions']['group']] = $player['permissions']['end'];

                //Search
                foreach ($player['permissions']['alternateGroups'] as $k => $row){
                    //Regular group
                    foreach ($group as $g => $gr){
                        if ($k === $g){
                            //Tout est réussi on update le forum
                            $this->container->xenforo->addGroup($username,$gr);
                            $old = $this->container->session->getProfile('user');
                            array_push($old['secondary_group_ids'], $gr);
                            $this->container->session->set('user', $old);
                        }
                    }

                    //staff groupgit pu
                    foreach ($staff as $g => $gr){
                        if ($k === $g){
                            //Tout est réussi on update le forum
                            $this->container->xenforo->addGroup($username,$gr);
                            $old = $this->container->session->getProfile('user');
                            array_push($old['secondary_group_ids'], $gr);
                            $this->container->session->set('user', $old);

                            //Update database directly
                            $this->container->mysql_forum->update("xf_user", ['username' => $username],['is_staff' => true]);
                        }
                    }
                    //partner group
                    foreach ($partner as $g => $gr){
                        if ($k === $g){
                            //Tout est réussi on update le forum
                            $this->container->xenforo->addGroup($username,$gr);
                            $old = $this->container->session->getProfile('user');
                            array_push($old['secondary_group_ids'], $gr);
                            $this->container->session->set('user', $old);
                        }
                    }
                }

            }


            return $next($request, $response);

        }


	}
}
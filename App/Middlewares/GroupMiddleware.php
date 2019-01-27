<?php

namespace App\Middlewares;

use App\Controllers\Api\BlogApiController;
use Dflydev\FigCookies\FigRequestCookies;
use Psr\Http\Message\ServerRequestInterface;
use Slim\App;

class GroupMiddleware
{
	private $container;

	public function __construct($container)
	{
		$this->container = $container;
	}

	public function __invoke(ServerRequestInterface $request, $response, $next)
	{


		if ($this->container->session->exist('user')){
            if ($this->container->session->exist('grade')){
                return $next($request, $response);
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

                //Search
                foreach ((array) $player['permissions']->groups->bungee as $k => $row){
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

                    //staff group
                    foreach ($staff as $g => $gr){
                        if ($k === $g){
                            //Tout est réussi on update le forum
                            $this->container->xenforo->addGroup($username,$gr);
                            $old = $this->container->session->getProfile('user');
                            array_push($old['secondary_group_ids'], $gr);
                            $this->container->session->set('user', $old);

                            //Update database directly
                            try {
                            	$this->container->mysql_forum->update("xf_user", ['username' => $username],['is_staff' => true]);
                            } catch (\Exception $e) {
															
                            }

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

            $this->container->session->set('grade', true);

            return $next($request, $response);
        }else{
            return $next($request, $response);
        }
	}
}

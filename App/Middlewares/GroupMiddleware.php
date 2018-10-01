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

                    //staff group
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


        }else{
            return $next($request, $response);
        }
	}
}
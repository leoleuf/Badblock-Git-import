<?php

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class PagesController extends Controller {

    public function getHome(RequestInterface $request, ResponseInterface $response){
    	var_dump($this->session->get('user'));

//    	var_dump($this->container->minecraft->getStatus());
//    	var_dump($this->container->minecraft->getPlayers());

		$firstRow = $this->redis->getJson('website:first_row_posts');
		$secondRow = $this->redis->getJson('website:second_row_posts');
		$postsCount = $this->redis->get('website:posts_count');

		$this->render($response, 'pages.home', [
        	'first_row' => $firstRow,
			'second_row' => $secondRow,
			'posts_count' => $postsCount
		]);
    }



    public function pagestaff(RequestInterface $request, ResponseInterface $response){
        //récupération du cache
        $admin = $this->redis->getJson('staff.admin');
        $admin = json_decode($admin);
        $resp = $this->redis->getJson('staff.responsables');
        $resp = json_decode($resp);
        $dev = $this->redis->getJson('staff.dev');
        $dev = json_decode($dev);
        $sup = $this->redis->getJson('staff.sup');
        $sup = json_decode($sup);
        $modo = $this->redis->getJson('staff.modo');
        $modo = json_decode($modo);
        $help = $this->redis->getJson('staff.helper');
        $help = json_decode($help);
        $modof = $this->redis->getJson('staff.modof');
        $modof = json_decode($modof);
        $anim = $this->redis->getJson('staff.anim');
        $anim = json_decode($anim);
        $staff = $this->redis->getJson('staff.staff');
        $staff = json_decode($staff);




        $this->render($response, 'pages.staff', [
            'admin' => $admin,
            'resp' => $resp,
            'dev' => $dev,
            'sup' => $sup,
            'modo' => $modo,
            'help' => $help,
            'modof' => $modof,
            'anim' => $anim,
            'staff' => $staff,
        ]);


    }

}
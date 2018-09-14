<?php
/**
 * Created by PhpStorm.
 * User: POINTURIER
 * Date: 04/08/2018
 * Time: 20:35
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class RedirectController extends Controller
{

    public function link(RequestInterface $request, ResponseInterface $response, $uuid)
    {
        $uuid = $uuid['uuid'];

        if ($this->redis->exists('redirect:' . $uuid)){
            $link = $this->redis->get('redirect:' . $uuid);

            return $this->redirect($response, $link);
        }else{
            $link = $this->container->mongo->redirect->findOne(['uuid' => $uuid]);
            $this->redis->set('redirect:' . $uuid, $link['link']);

            return $this->redirect($response, $link['link']);
        }
    }
    
}
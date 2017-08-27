<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 21/08/2017
 * Time: 22:12
 */

namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;


class ProfileController extends Controller
{

    public function getprofile(RequestInterface $request, ResponseInterface $response,$pseudo){

        //sans cache
        $collection = $this->mongo->test->test;

        $user = $collection->findOne(['realName' => $pseudo['pseudo']]);

        if (empty($user)){
            return $this->container['notFoundHandler']($request, $response);
        }



        $user->game->stats->tower['cpoints'] = $this->tower($user->game->stats->tower);
        $user->game->stats->rush['cpoints'] = $this->rushs($user->game->stats->rush);
        $user->game->stats->survival['cpoints'] = $this->survival($user->game->stats->survival);
        $user->game->stats->uhcspeed['cpoints'] = $this->uhcspeed($user->game->stats->uhcspeed);
        $user->game->stats->pearlswar['cpoints'] = $this->pearlswar($user->game->stats->pearlswar);
        $user->game->stats->spaceballs['cpoints'] = $this->spaceball($user->game->stats->spaceballs);
        $user->game->stats->cts['cpoints'] = $this->cts($user->game->stats->cts);



        //return view
        return $this->render($response, 'user.profile', [
            'user' => $user]);

    }




    public function tower($var){

        //Algo celon Xmalware

        $c1 = $var['marks']+$var['wins'];
        //pas de division par zéro
        if ($var['deaths'] > 0){
            $c2 = $var['kills']/$var['deaths'];
        }else{
            $c2 = $var['kills'];
        }
        $c3 = $c1*$c2;
        //pas de division par zéro
        $var['looses'] = $var['looses']*4;
        if ($var['looses'] > 0){
            $c4 = $c3 /$var['looses'];
        }else{
            $c4 = $c3;
        }

        //Arrondis

        $c4 = round($c4,0,PHP_ROUND_HALF_UP);

        return $c4;

    }

    public function spaceball($var){

        //Algo celon Xmalware

        $c1 = $var['diamonds']+$var['wins'];
        //pas de division par zéro
        if ($var['deaths'] > 0){
            $c2 = $var['kills']/$var['deaths'];
        }else{
            $c2 = $var['kills'];
        }
        $c3 = $c1*$c2;
        //pas de division par zéro
        if ($var['looses'] > 0){
            $c4 = $c3 /$var['looses'];
        }else{
            $c4 = $c3;
        }

        //Arrondis

        $c4 = round($c4,0,PHP_ROUND_HALF_UP);

        return $c4;

    }

    public function rushs($var){

        //Algo celon Xmalware

        $c1 = $var['brokenbeds']+$var['wins'];
        //pas de division par zéro
        if ($var['deaths'] > 0){
            $c2 = $var['kills']/$var['deaths'];
        }else{
            $c2 = $var['kills'];
        }
        $c3 = $c1*$c2;
        //pas de division par zéro
        if ($var['looses'] > 0){
            $c4 = $c3 /$var['looses'];
        }else{
            $c4 = $c3;
        }

        //Arrondis

        $c4 = round($c4,0,PHP_ROUND_HALF_UP);

        return $c4;

    }


    public function pearlswar($var){

        //Algo celon Xmalware

        $c1 = $var['wins']*1.5;
        //pas de division par zéro
        if ($var['deaths'] > 0){
            $c2 = $var['kills']/$var['deaths'];
        }else{
            $c2 = $var['kills'];
        }
        $c3 = $c1*$c2;
        //pas de division par zéro
        if ($var['looses'] > 0){
            $c4 = $c3 /$var['looses'];
        }else{
            $c4 = $c3;
        }

        //Arrondis

        $c4 = round($c4,0,PHP_ROUND_HALF_UP);

        return $c4;

    }


    public function uhcspeed($var){

    //Algo celon Xmalware

    $c1 = $var['wins']*1.5;
    //pas de division par zéro
    if ($var['deaths'] > 0){
        $c2 = $var['kills']/$var['deaths'];
    }else{
        $c2 = $var['kills'];
    }
    $c3 = $c1*$c2;
    //pas de division par zéro
    if ($var['looses'] > 0){
        $c4 = $c3 /$var['looses'];
    }else{
        $c4 = $c3;
    }

    //Arrondis

    $c4 = round($c4,0,PHP_ROUND_HALF_UP);

    return $c4;

}

    public function cts($var){

        //Algo celon Xmalware

        $c1 = $var['capturedflags']+$var['wins'];
        //pas de division par zéro
        if ($var['deaths'] > 0){
            $c2 = $var['kills']/$var['deaths'];
        }else{
            $c2 = $var['kills'];
        }
        $c3 = $c1*$c2;
        //pas de division par zéro
        if ($var['looses'] > 0){
            $c4 = $c3 /$var['looses'];
        }else{
            $c4 = $c3;
        }

        //Arrondis

        $c4 = round($c4,0,PHP_ROUND_HALF_UP);

        return $c4;

    }


    public function survival($var){

        //Algo celon Xmalware

        $c1 = $var['wins']*1.5;
        //pas de division par zéro
        if ($var['deaths'] > 0){
            $c2 = $var['kills']/$var['deaths'];
        }else{
            $c2 = $var['kills'];
        }
        $c3 = $c1*$c2;
        //pas de division par zéro
        if ($var['looses'] > 0){
            $c4 = $c3 /$var['looses'];
        }else{
            $c4 = $c3;
        }

        //Arrondis

        $c4 = round($c4,0,PHP_ROUND_HALF_UP);

        return $c4;

    }



}
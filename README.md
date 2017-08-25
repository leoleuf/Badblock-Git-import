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


{% extends "default.twig" %}
{% set show_title = false %}
{% set title = user.name %}
{% set subtitle = 'Ses statistiques' %}
{% block cover %}
    <!------Cover------>
    <div class="cover-back">
        <div class="cover cover-profile">
            <div class="container">
                <div class="one-half column animated bounceInLeft">
                    <div class="profile-title">
                        <div class="profile-mc-head"><img src="https://api.badblock.fr/public/badblock-heads/?name={{ user.name }}" title="Tête du skin"></div>
                        <div>
                            <h2 class="cover-heading">{{ user.name }}</h2>
                            <span class="badge badge-{{ user.permissions.group }}">{{ ucfirst(user.permissions.group) }}</span>
                            {% for key, permission in user.permissions.alternateGroups %}
                                <span class="badge badge-{{ key }}">{{ ucfirst(key) }}</span>
                            {% endfor %}
                        </div>
                    </div>
                </div>
            </div>
            <div class="profile-info">
                <div class="container">
                    <ul>
                        <li><i class="icon icon-bookmarkalt"></i> {{ user.game.level }} <span>Levels</span></li>
                        <li><i class="icon icon-value-coins"></i> {{ user.game.xp }} <span>Xp</span></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!------Cover fin------>
{% endblock %}
{% block content %}
    {% if user.punish.mute == true %}
        <div class="alert alert-warning" role="alert">
            Cette personne est baillonné pour {{ user.punish.muteReason }} jusqu'au {{ timestampToTime(user.punish.muteEnd) }}
        </div>
    {% endif %}

    {% if user.punish.ban == true %}
        <div class="alert alert-warning" role="alert">
            Cette personne est baillonné pour {{ user.punish.banReason }} jusqu'au {{ timestampToTime(user.punish.banEnd) }}
        </div>
    {% endif %}
    <div class="profile-stats">
        <h2>Statistiques</h2>
        <h3>Tower</h3> <span class="badge badge-admin">{{ user.game.stats.tower.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.tower.kills }}</td>
            </tr>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.tower.deaths }}</td>
            </tr>
            <tr>
                <td>Points</td>
                <td>{{ user.game.stats.tower.marks }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.tower.wins }}</td>
            </tr>
            <tr>
                <td>Parties perdus</td>
                <td>{{ user.game.stats.tower.looses }}</td>
            </tr>
            </tbody>
        </table>

        <h3>Rush</h3> <span class="badge badge-admin">{{ user.game.stats.rush.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.rush.wins }}</td>
            </tr>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.rush.deaths }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.rush.wins }}</td>
            </tr>
            <tr>
                <td>Lit détruits</td>
                <td>{{ user.game.stats.rush.brokenbeds }}</td>
            </tr>
            <tr>
                <td>Parties perdus</td>
                <td>{{ user.game.stats.rush.looses }}</td>
            </tr>
            </tbody>
        </table>

        <h3>Survival</h3>   <span class="badge badge-admin">{{ user.game.stats.survival.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.survival.kills }}</td>
            </tr>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.survival.deaths }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.survival.wins }}</td>
            </tr>
            <tr>
                <td>Parties perdus</td>
                <td>{{ user.game.stats.survival.looses }}</td>
            </tr>
            </tbody>
        </table>

        <h3>Uhc speed</h3>  <span class="badge badge-admin">{{ user.game.stats.uhcspeed.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.uhcspeed.kills }}</td>
            </tr>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.uhcspeed.deaths }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.uhcspeed.wins }}</td>
            </tr>
            <tr>
                <td>Parties perdus</td>
                <td>{{ user.game.stats.uhcspeed.looses }}</td>
            </tr>
            </tbody>
        </table>

        <h3>Pearls War</h3>    <span class="badge badge-admin">{{ user.game.stats.pearlswar.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.pearlswar.kills }}</td>
            </tr>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.pearlswar.deaths }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.pearlswar.wins }}</td>
            </tr>
            <tr>
                <td>Parties perdus</td>
                <td>{{ user.game.stats.pearlswar.looses }}</td>
            </tr>
            </tbody>
        </table>

        <h3>Spaceballs</h3> <span class="badge badge-admin">{{ user.game.stats.spaceballs.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Diamants</td>
                <td>{{ user.game.stats.spaceballs.diamonds }}</td>
            </tr>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.spaceballs.kills }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.spaceballs.wins }}</td>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.spaceballs.deaths }}</td>
            </tr>
            </tbody>
        </table>

        <h3>Capture The Sheep</h3> <span class="badge badge-admin">{{ user.game.stats.cts.cpoints }} Points</span>
        <table class="u-full-width">
            <tbody>
            <tr>
                <td>Exécutions</td>
                <td>{{ user.game.stats.cts.kills }}</td>
            </tr>
            <tr>
                <td>Morts</td>
                <td>{{ user.game.stats.cts.deaths }}</td>
            </tr>
            <tr>
                <td>Parties gagnés</td>
                <td>{{ user.game.stats.cts.wins }}</td>
            </tr>
            <tr>
                <td>Mouton Capturés</td>
                <td>{{ user.game.stats.cts.capturedflags }}</td>
            </tr>
            <tr>
                <td>Parties perdues</td>
                <td>{{ user.game.stats.cts.looses }}</td>
            </tr>
            </tbody>
        </table>
    </div>
{% endblock %}

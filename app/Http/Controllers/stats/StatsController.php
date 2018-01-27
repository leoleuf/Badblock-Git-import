<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 14/01/2018
 * Time: 09:38
 */

namespace App\Http\Controllers\stats;
use Illuminate\Support\Facades\Redis;


class StatsController
{

    public function  playersStats()
    {

        Redis::exists('key');

        return view('stats.statsPlayers');
    }




}
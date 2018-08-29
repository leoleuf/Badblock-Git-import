<?php

namespace App;


use Jenssegers\Mongodb\Eloquent\Model as Eloquent;


class Players extends Eloquent {

    protected $connection = 'mongodb_server';
    protected $collection = "players";

    public static $rules = array(
        'name'=>'required',
        'nickName'=>'required',
        'lastip'=>'required',
    );


}
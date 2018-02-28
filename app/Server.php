<?php

namespace App;


use Jenssegers\Mongodb\Eloquent\Model as Eloquent;


class Server extends Eloquent {

    protected $connection = 'mongodb';
    protected $collection = "server";

    public static $rules = array(
        'name'=>'required',

        'realname'=>'required',

        'icon'=>'required',

        'visibility' => 'required',
    );


}
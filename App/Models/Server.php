<?php

namespace App;


use Jenssegers\Mongodb\Eloquent\Model as Eloquent;


class Server extends Eloquent {

    protected $connection = 'mongodb';
    protected $collection = "server_list";

    public static $rules = array(
        'name'=>'required',

        'realname'=>'required',

        'icon'=>'required',

        'visibility' => 'required',
    );


}
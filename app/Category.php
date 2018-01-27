<?php

namespace App;


use Jenssegers\Mongodb\Eloquent\Model as Eloquent;


class Category extends Eloquent {

    protected $connection = 'mongodb';
    protected $collection = "category";


    public static $rules = array(
        'name'=>'required',

        'realname'=>'required',

        'server'=>'required',

        'visibility' => 'required',
    );



}
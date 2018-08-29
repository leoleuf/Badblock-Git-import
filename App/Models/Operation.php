<?php

namespace App;


use Jenssegers\Mongodb\Eloquent\Model as Eloquent;


class Operation extends Eloquent {

    protected $connection = 'mongodb';
    protected $collection = "operations";

    public static $rules = array(
        'name'=>'required',

        'price'=>'required',

        'unique-id' => 'required',
        'date' => 'required',
    );


}
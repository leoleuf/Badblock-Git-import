<?php

namespace App;


use Jenssegers\Mongodb\Eloquent\Model as Eloquent;


class Category extends Eloquent {

    protected $connection = 'mongodb';
    protected $collection = "category_list";


    public static $rules = array(
        'name'=>'required',

        'subname'=>'required',


        'visibility' => 'required',
    );



}
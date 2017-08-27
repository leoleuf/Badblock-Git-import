<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 27/08/2017
 * Time: 22:14
 */

namespace App\Controllers;


class CacheController extends Controller
{

    public function setcache(){


        $collection = $this->mongo->test->test;

        $cursor = $collection->find();

        $owner = array();
        $admin = array();
        $staff = array();



        foreach ($cursor as $document) {
            if ($document->permissions["group"] == "owner") {
                array_push($owner, $document["name"]);
            } elseif ($document->permissions["group"] == "admin") {
                array_push($admin, $document["name"]);
            } elseif ($document->permissions["group"] == "staff") {
                array_push($staff, $document["name"]);
            }


        }

        $admin = json_encode($admin);

        $this->redis->setJson('staff.admin', $admin);

        $array = $this->redis->getJson('staff.admin');

        var_dump($array);

    }





}
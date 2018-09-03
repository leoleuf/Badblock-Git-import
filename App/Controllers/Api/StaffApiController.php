<?php

namespace App\Controllers\Api;


use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class StaffApiController extends \App\Controllers\Controller
{
    public function getCreateCacheAllStaff(RequestInterface $request, ResponseInterface $response)
    {

        $data = $this->container->mysql_forum->fetchRowMany('SELECT username,custom_title,user_group_id FROM xf_user WHERE is_staff = 1 ORDER by user_group_id');

        $role = [];

        foreach ($data as $document)
        {
            if (isset($role[$document["user_group_id"]])){
                array_push($role[$document["user_group_id"]]["data"], array($document["username"],$document["custom_title"]));
            }else{
                $dat = $this->container->mysql_forum->fetchRow('SELECT user_group_id,banner_text,display_style_priority FROM xf_user_group WHERE user_group_id = ' . $document["user_group_id"] .' ORDER by user_group_id');
                $role[$dat['user_group_id']] = ['name' => $dat['banner_text'],'display_style_priority' => $dat['display_style_priority'],'data' => []];
                array_push($role[$document["user_group_id"]]["data"], array($document["username"],$document["custom_title"]));

            }

        }

        foreach ($role as $key => $row){
            if (count( $row['data']) > 1){
                $data_r = preg_split("/[\s,]+/", $row['name']);
                if (count($data_r) > 1){
                    $role[$key]['name'] = preg_replace("/[\s,]+/", "s ", $row['name']);
                }else{
                    $role[$key]['name'] = $row['name']."s";
                }
            }

        }

        usort($role, function($a, $b) {
            return $a['display_style_priority'] <= $b['display_style_priority'];
        });


        $this->redis->setJson('staff.list', $role);


        $this->redis->setJson('staff.number', count($data));

        $this->log->success('"StaffApiController\getCreateCacheAllStaff"',' Success writing staff cache');


        return $response->write('Success writing staff cache')->withStatus(200);


    }

}
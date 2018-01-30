<?php

namespace App\Controllers\Api;


use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

class StaffApiController extends \App\Controllers\Controller
{
    public function getCreateCacheAllStaff(RequestInterface $request, ResponseInterface $response)
    {

        $data = $this->mysql->fetchRowMany('SELECT username,custom_title,user_group_id FROM xf_user WHERE is_staff = 1 ORDER by user_group_id');

        $role = [];

        foreach ($data as $document) {
            if (isset($role[$document["user_group_id"]])){
                array_push($role[$document["user_group_id"]]["data"], array($document["username"],$document["custom_title"]));
            }else{
                $dat = $this->mysql->fetchRow('SELECT user_group_id,banner_text,display_style_priority FROM xf_user_group WHERE user_group_id = ' . $document["user_group_id"] .' ORDER by user_group_id');
                $role[$dat['user_group_id']] = ['name' => $dat['banner_text'],'display_style_priority' => $dat['display_style_priority'],'data' => []];
                array_push($role[$document["user_group_id"]]["data"], array($document["username"],$document["custom_title"]));

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

	public function getCreateCacheAllStsssaff(RequestInterface $request, ResponseInterface $response)
	{

        $data = $this->mysql->fetchRowMany('SELECT username,custom_title,user_group_id FROM xf_user WHERE is_staff = 1 ORDER by username');

		$admin = [];
		$dev = [];
		$resp = [];
		$sup = [];
		$modo = [];
		$modof = [];
		$helper = [];
		$staff = [];


		foreach ($data as $document) {

			if ($document["user_group_id"] == "3" || $document["user_group_id"] == "7") {
				array_push($admin, array($document["username"],$document["custom_title"]));
			}
			elseif ($document["user_group_id"] == "14") {
				array_push($dev, array($document["username"],$document["custom_title"]));
			}
            elseif ($document["user_group_id"] == "6") {
                array_push($resp, array($document["username"],$document["custom_title"]));
            }
            elseif ($document["user_group_id"] == "4" || $document["user_group_id"] == "10") {
                array_push($sup, array($document["username"],$document["custom_title"]));
            }
            elseif ($document["user_group_id"] == "12") {
                array_push($modo, array($document["username"],$document["custom_title"]));
            }
            elseif ($document["user_group_id"] == "13") {
                array_push($helper, array($document["username"],$document["custom_title"]));
            }
            elseif ($document["user_group_id"] == "8") {
                array_push($modof, array($document["username"],$document["custom_title"]));
            }
            else{
                array_push($staff, array($document["username"],$document["custom_title"]));
            }

		}

        $this->redis->setJson('staff.admin', $admin);
		$this->redis->setJson('staff.dev', $dev);
		$this->redis->setJson('staff.resp', $resp);
		$this->redis->setJson('staff.sup', $sup);
		$this->redis->setJson('staff.modo', $modo);
		$this->redis->setJson('staff.helper', $helper);
		$this->redis->setJson('staff.modof', $modof);
		$this->redis->setJson('staff.staff', $staff);
		$this->redis->setJson('staff.number', count($data));

        $this->log->success('"StaffApiController\getCreateCacheAllStaff"',' Success writing staff cache');


        return $response->write('Success writing staff cache')->withStatus(200);


    }
}
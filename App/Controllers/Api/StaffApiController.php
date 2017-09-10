<?php

namespace App\Controllers;

class StaffApiController extends \App\Controllers\Controller
{

	public function getCreateCacheAllStaff()
	{

        $data = $this->mysql->fetchRowMany('SELECT username,custom_title,user_group_id FROM xf_user WHERE is_staff = 1 ORDER by user_group_id');

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

        var_dump($resp);


        $this->redis->setJson('staff.admin', $admin);
		$this->redis->setJson('staff.dev', $dev);
		$this->redis->setJson('staff.resp', $resp);
		$this->redis->setJson('staff.sup', $sup);
		$this->redis->setJson('staff.modo', $modo);
		$this->redis->setJson('staff.helper', $helper);
		$this->redis->setJson('staff.modof', $modof);
		$this->redis->setJson('staff.staff', $staff);
	}
}
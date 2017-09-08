<?php

namespace App\Controllers;

class StaffApiController extends Controller
{

	public function getCreateCacheAllStaff()
	{
		$collection = $this->mongo->test->test;

		$cursor = $collection->find();

		$owner = [];
		$admin = [];
		$dev = [];
		$sup = [];
		$modo = [];
		$helper = [];
		$anim = [];
		$modof = [];
		$staff = [];


		foreach ($cursor as $document) {

			if ($document->permissions["group"] == "owner") {
				array_push($owner, $document["name"]);
			} elseif ($document->permissions["group"] == "admin") {
				array_push($admin, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "developpeur") {
				array_push($dev, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "superviseur" or $document->permissions["group"] == "superviseuse") {
				array_push($sup, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "modo") {
				array_push($modo, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "helper") {
				array_push($helper, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "animateur") {
				array_push($anim, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "modof") {
				array_push($modof, $document["name"], $document->permissions["group"]);
			} elseif ($document->permissions["group"] == "staff") {
				array_push($staff, $document["name"], $document->permissions["group"]);
			}


		}

		$this->redis->setJson('staff.owner', $owner);
		$this->redis->setJson('staff.admin', $admin);
		$this->redis->setJson('staff.dev', $dev);
		$this->redis->setJson('staff.sup', $sup);
		$this->redis->setJson('staff.modo', $modo);
		$this->redis->setJson('staff.helper', $helper);
		$this->redis->setJson('staff.anim', $anim);
		$this->redis->setJson('staff.modof', $modof);
		$this->redis->setJson('staff.staff', $staff);
	}
}
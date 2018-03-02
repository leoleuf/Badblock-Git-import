<?php
namespace App;

//Le futuriste
//TO DO: A commenter
use App\Controllers\Controller;

class MinecraftServerQuery {

	private $endpoint = "https://mcapi.us/server/";

	public function __construct($container, $config)
	{
			$this->guzzle = $container->guzzle;
			$this->redis = $container->redis;
			$this->config = $config;
	}

	private function extractData($resp){
		return json_decode($resp->getBody(), 1);
	}

	private function getData(){
		return $this->extractData($this->guzzle->request('GET', $this->endpoint . 'status?ip=' . $this->config['host']));
	}

	public function getStatus(){
		$data = $this->getData();
		if ($data['status'] == 'success'){
			return true;
		}else{
			return false;
		}
	}

	public function getInfos(){
		if ($this->getStatus()){
			$data = $this->getData();
			return [
				'motd' => $data['motd'],
				'motd_extra' => $data['motd_extra'],
				'motd_formatted' => $data['motd_formatted'],
				'server' => $data['server']
			];
		}
	}

	public function getPlayers(){
	    if ($this->redis->exists('api.players')){
            return $this->redis->get('api.players');
        }else{
            if ($this->getStatus()){
                $data = $this->getData();
                $this->redis->setJson('api.players', $data['players']);
                $this->redis->expire('api.players', 5);
                return $data['players'];
            }else{
                return $this->getStatus();
            }
        }
	}


    /**
     * @return mixed
     */

    public function registredCount(){
        
    }

    public function getRegistred()
    {
        if ($this->redis->exists('api.registred')){
            return $this->redis->get('api.registred');
        }else{
            if ($this->getStatus()){
                $data = $this->getData();
                $this->redis->setJson('api.registred', $data['players']);
                $this->redis->expire('api.registred', 5);
                return $data['players'];
            }else{
                return $this->getStatus();
            }
        }
    }

}
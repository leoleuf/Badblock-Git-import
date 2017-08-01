<?php
namespace App;

class Environment {
    public function __construct($configFile = '../environment.json')
    {
        $this->configFile = $configFile;
        $this->config = $this->getConfig();
    }

    private function getConfig(){
        $json = file_get_contents($this->configFile);
        $config = json_decode($json, 1);
        return $config['environments'];
    }

    public function getEnvironment(){
        foreach ($this->config AS $item){
        	if (isset($item['root_path'])){
				if ($item['root_path'] == $_SERVER['DOCUMENT_ROOT'] && $item['access_way'] = $_SERVER['SERVER_NAME'] .
						$_SERVER['SERVER_PORT']){
					//success verification
					$environment = $item['name'];
					break;
				}
			}else{
				if ($item['access_way'] = $_SERVER['SERVER_NAME'] .
						$_SERVER['SERVER_PORT']){
					//success verification
					$environment = $item['name'];
					break;
				}
			}
        }
        return $environment;
    }
}
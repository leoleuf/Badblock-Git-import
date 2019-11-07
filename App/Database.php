<?php

namespace App;

use PDO;
use PDOException;

class Database extends PDO
{

    protected $host;
    protected $db;
    protected $user;
    protected $password;
    protected $port;
    protected $defaultDB;
    protected $defaultPort;

    public function __construct($name = NULL)
    {

        $this->defaultDB = strtoupper("MYSQL"); //Here is setup the default Database
        $this->defaultPort = 3306;

        $dbEnv = Helper::getEnv(($name) ? $name : $this->defaultDB ."_DB");

        $this->host = $dbEnv['HOST'];
        $this->db = $dbEnv['DB'];
        $this->user = $dbEnv['USER'];
        $this->password = $dbEnv['PASSWORD'];
        $this->port = (!empty($dbEnv['PORT'])) ? $dbEnv['PORT'] : $this->defaultPort;

        try {

            return parent::__construct("mysql:host=" . $this->host . ";port=".$this->port.";dbname=" . $this->db, $this->user, $this->password, [
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_OBJ
            ]);

        } catch (PDOException $e){


            var_dump($e);
            die();

        }

    }

}
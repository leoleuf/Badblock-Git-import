<?php

//Autoloading namespaces
spl_autoload_register(function($className) {
    include_once "../".$className . '.php';
});

$routes = [
    "/" => "home",
    "/api" => "api",
    "/404" => "errors/404"
];

//uri = explode("/", strip_tags($_SERVER['REQUEST_URI'])); Future update
$uri = strtok(strtok(strip_tags($_SERVER['REQUEST_URI']), "?"), "&");

array_key_exists($uri, $routes) ? include_once "./pages/".$routes[$uri].".php" : include_once "./pages/errors/404.php";
<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 24/03/2018
 * Time: 15:00
 */

namespace App\Middlewares;


class ErrorMiddleware
{
    /**
     * Slim container object
     */
    private $container;

    public $protec_routes =  ["/","/shop",""];

    public function __construct($container)
    {
        $this->container = $container;
    }

    public function __invoke($request, $response, $exception) {
        $collection = $this->container->mongo->logs;

        $requestCode = uniqid();

        $ip = $_SERVER['REMOTE_ADDR'];
        $method = $request->getMethod();
        $uri = $request->getUri();
        $head = json_encode($request->getHeaders(), true);

        $data = [
            "error_id" => $requestCode,
            "type" => "error",
            "date" => date("Y-m-d H:i:s"),
            "ip" => $ip,
            "method" => $method,
            "uri" => $uri,
            "header" => $head,
            "last_error" => error_get_last(),
            "server" => $_SERVER
        ];

        $collection->insertOne($data);


        return $response
            ->withStatus(200)
            ->withHeader('Content-Type', 'text/html')
            ->write("Oups une erreur s'est produite ! CODE : " . $requestCode);
    }
}
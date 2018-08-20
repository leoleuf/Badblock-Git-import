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
            "error" => $exception->getMessage(),
            "last_error" => error_get_last(),
            "server" => $_SERVER
        ];

        $collection->insertOne($data);



        $html = '<!doctype html>
<title>BadBlock</title>
<body style="background-color:#dcdde1">
<link rel="icon" type="image/png" href="https://cdn.badblock.fr/images/logo_small.png" title="Logo BadBlock" title="Logo BadBlock"/>
<style>
  body { text-align: center; padding: 150px; }
  h1 { font-size: 25px; }
  body { font: 20px Helvetica, sans-serif; color: #333; }
  article { display: block; text-align: left; width: 900px; margin: 0 auto; }
  a { color: #dc8100; text-decoration: none; }
  a:hover { color: #333; text-decoration: none; }
</style>
<style type="text/css">.cfmessage{visibility: hidden;}</style>

<article>
        <div class="cfmessage"></div>
        <center><img src="https://cdn.badblock.fr/images/logo_small.png" alt="Logo BadBlock" title="Logo BadBlock"></center>
        <center><h1>Code d\'erreur : '. $requestCode .'</h1></center>
        <center><h1>Merci de conserver ce code pour une meilleure asistance !</h1></center>
        <center>                        <p>Discord de BadBlock : <a href="https://badblock.fr/discord">Rejoindre !</a></p></center>

        <div>
                <center>
                <br><br><a href="mailto:sysadmin@badblock.fr" target="_top" title="Abus BadBlock">ABUS</a> | <a href="https://status.badblock.fr" target="_top" title="État BadBlock">ÉTAT DE SERVICE</a>
                </center>
    </div>
</article>';




        return $response
            ->withStatus(200)
            ->withHeader('Content-Type', 'text/html')
            ->write($html);
    }
}
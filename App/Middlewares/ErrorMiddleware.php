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
        $head = json_encode($request->getHeaders(), true);

        $stack = [];
        $i = 1;
        $trace = debug_backtrace();
        unset($trace[0]); //Remove call to this function from stack trace
        foreach($trace as $node) {
            $s = "";
            $s .= "#$i ".$node['file'] ."(" .$node['line']."): ";
            if(isset($node['class'])) {
                $s .= $node['class'] . "->";
            }
            $s .= $node['function'] . "()" . PHP_EOL;
            array_push($stack,$s);
            $i++;
        }

        $data = [
            "error_id" => $requestCode,
            "type" => "error",
            "date" => date("Y-m-d H:i:s"),
            "ip" => $ip,
            "method" => $method,
            "uri" => $_SERVER['REQUEST_URI'],
            "header" => $head,
            "error" => $exception->getMessage(),
            "last_error" => error_get_last(),
            "stack" => $stack
        ];

        $collection->insertOne($data);



        $html = '<!doctype html>
<title>BadBlock</title>
<body style="background-color:#dcdde1">
<link rel="icon" type="image/png" href="https://cdn.badblock.fr/images/serveur-minecraft.png" title="Serveur Minecraft BadBlock" title="Serveur Minecraft BadBlock"/>
<style>
  body { text-align: center; padding: 150px; }
  h1 { font-size: 25px; }
  body { font: 20px Helvetica, sans-serif; color: #333; }
  article { display: block; text-align: left; width: 900px; margin: 0 auto; }
  a { color: #dc8100; text-decoration: none; }
  a:hover { color: #333; text-decoration: none; }
</style>
<style type="text/css">.cfmessage{visibility: hidden;}</style>
<meta name="robots" content="noindex, nofollow">

<article>
        <div class="cfmessage"></div>
        <center><img src="https://cdn.badblock.fr/images/serveur-minecraft.png" alt="Serveur Minecraft BadBlock" title="Serveur Minecraft BadBlock"></center>
        <center><h1>Code d\'erreur : '. $requestCode .'</h1></center>
        <center><h1>Merci de conserver ce code pour une meilleure asistance !</h1></center>
        <center>                        <p>Discord de BadBlock : <a href="https://badblock.fr/discord">Rejoindre !</a></p></center>

        <div>
                <center>
                <br><br><a rel="nofollow" href="mailto:sysadmin@badblock.fr" target="_top" title="Abus BadBlock">ABUS</a> | <a href="https://status.badblock.fr" target="_top" title="État BadBlock">ÉTAT DE SERVICE</a>
                </center>
    </div>
</article>';




        return $response
            ->withStatus(200)
            ->withHeader('Content-Type', 'text/html')
            ->write($html);
    }
}
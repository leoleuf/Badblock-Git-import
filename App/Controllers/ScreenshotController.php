<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/06/2018
 * Time: 19:43
 */

namespace App\Controllers;
namespace App\Controllers;

use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;



class ScreenshotController extends Controller
{

    public function getPage(RequestInterface $request, ResponseInterface $response){
        $this->render($response, 'pages.screen');
    }



    public function getPost(RequestInterface $request, ResponseInterface $response){
        //Search username
        if ($this->container->session->exist('user')){
            $player = strtolower($this->session->getProfile('username')['username']);
        }else{
            $player = false;
        }



        //Search extension of file
        $filename = $_POST["name"];
        $filename = explode('.', $filename);
        //Check if they are two points
        if (count($filename) == 2){
            $ext = $filename[1];
        }else{
            $ext = end($filename);
        }

        //Generate file name
        $hash = uniqId($filename[1] . date('s'));
        $uuid = substr(
            $hash,
            strlen($hash) - 8
        );


        //Save dans MongoDB
        $data = [
            'ip' => $_SERVER['REMOTE_ADDR'],
            'user' => $player,
            'date' => date("Y-m-d h:i"),
            'file_name' => $uuid . '.' .$ext
        ];
        $this->container->mongo->log_upload->insertOne($data);


        $handle = fopen($_POST['data'], 'r');

        //Upload to FTP
        // Mise en place d'une connexion basique
        $conn_id = ftp_connect(getenv('FTP_IP'));

        // Identification avec un nom d'utilisateur et un mot de passe
        $login_result = ftp_login($conn_id, getenv('FTP_USER'), getenv('FTP_PASSWORD')) or die("Erreur serveur !");;
        ftp_pasv($conn_id, true) or die("Erreur serveur !");

        ftp_fput($conn_id,  $uuid . "." . $ext, $handle,FTP_BINARY) or die("Erreur serveur !");;
        fclose($handle);

        return $response->write($uuid . '.' .$ext)->withStatus(200);

    }


}
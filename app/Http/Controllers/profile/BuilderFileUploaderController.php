<?php
/**
 * Created by PhpStorm.
 * User: matth
 * Date: 27/02/2019
 * Time: 12:29
 */

namespace App\Http\Controllers\profile;


class BuilderFileUploaderController
{

    public function index(){

        return view('profil.builderFileUploader');
    }

    public function upload()
    {

        $file_dest = "/";

        $file = $_FILES['uploadedFile'];
        $remote_file = $file_dest.$file['name'];

        $extensions = array("application/octet-stream");

        if(in_array($file['type'],$extensions) == false){
            return response()->json(['error' => "Extension non permise, merci de ne téléverser qu'un .schematic"]);
        }

        $conn_id = ftp_connect(env("FTP_IP"));

        if($conn_id == false){
            response()->json(['error' => "Erreur lors de la connection FTP, merci de contacter un administrateur"], 409);
        }

        // Identification avec un nom d'utilisateur et un mot de passe
        if (ftp_login($conn_id, env("FTP_USER"), env("FTP_PASSWORD"))){

            if (ftp_put($conn_id, $remote_file, $file, FTP_ASCII)) {

                ftp_close($conn_id);
                response()->json(["success" => "Le fichier " . $file['name'] . " a été chargé avec succès\n"], 200);
            } else {
                ftp_close($conn_id);
                response()->json(["error" => "Il y a eu un problème lors du chargement du fichier " . $file['name']], 409);
            }
        }

        else {
            ftp_close($conn_id);
            response()->json(['error' => "Erreur lors de la connection FTP, merci de contacter un administrateur"], 409);
        }


    }
}
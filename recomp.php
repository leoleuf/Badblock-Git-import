<?php

    if (isset($_COOKIE['forum_user'])) {

        //recupérer l'id utilisateur à partir du cookie
        $xf_user = $_COOKIE['forum_user'];
        $pos = strpos($xf_user, ',');
        $userid = substr($xf_user, 0, $pos);


        date_default_timezone_set('Europe/London');
        $time = date('Y-m-d h:i');
        $time = hash("gost", $time);
        $key = md5($time);


        $dbh = new PDO('mysql:host=node02-dev.cluster.badblock-network.fr;dbname=forum', "node02-dev", "nbx41losj6npowdvzy7iymujwvuq33ufbtpjzu2udc7x3fzx13x4gvuyl22dqoc8");

        $df = $dbh->query("SELECT recop,username from xf_user where user_id = ". $userid);
        $data = $df->fetch(PDO::FETCH_ASSOC);


        //on vérifie si il a pas déjà ça récompense

        if ($data["recop"] == '0'){
            $dbh->exec("UPDATE xf_user set recop = 1 where user_id = ". $userid);

            sendrecomp($data['username']);
        }



    }







?>
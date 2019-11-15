<?php

    if($_GET) {

        $params = array_map('strip_tags', $_GET);

        /**
         *
         * HERE IS THE API
         *
         */
        if(isset($params['player'])){

            $sql = "SELECT votes_nb FROM votes WHERE user_uuid = ?";

            if(isset($params['month']) && isset($params['year']))
                $sql .= " AND month = ? AND year = ";
            elseif(isset($params['year']))
                $sql .= " AND year = ?";
            elseif (isset($params['month']))
                $sql .= " AND month = ?";

            $values = [];
            foreach($params as $key => $value) {
                unset($params[$key]);
                array_push($values, $value);
            }

            $credentials = [
                "host" => "127.0.0.1",
                "port" => "3306",
                "dbname" => "badblock_stats",
                "user" => "root",
                "password" => ""
            ];

            $pdo = new PDO("mysql:host=" . $credentials['host'] . ";port=".$credentials['port'].";dbname=" . $credentials['db'], $credentials['user'], $credentials['password'], [
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_OBJ
            ]);

            $result = $pdo->prepare($sql);
            $result->execute($values);
            $result = $result->fetch();

            if($result)
                echo json_encode($result);
            else
                echo json_encode("[]");


        }

        else
            return http_response_code(404);
    }

    /**
     *
     * HERE IS THE VOTE CONFIRMATION
     *
     */

    ?>
<!DOCTYPE html>
<html id="XF" lang="fr-FR" dir="LTR" data-app="public" data-template="thread_home_blog_Xenbros" data-container-key="" data-content-key="" data-logged-in="false" data-cookie-prefix="badblock_" class="has-no-js template-thread_home_blog_Xenbros" data-run-jobs=""> <head> <meta charset="utf-8" /> <meta http-equiv="X-UA-Compatible" content="IE=Edge" /> <meta name="viewport" content="width=device-width, initial-scale=1"> <meta name="author" content="BadBlock developers, BuyCraft Team" /> <meta name="description" content="Jouez au meilleur Serveur Minecraft accessible à tout le monde gratuitement. Découvre nos jeux SkyBlock Mini-Jeux FreeBuild Faction et bien plus encore ! Télécharge le jeu gratuitement. Rejoins nous sur notre serveur Minecraft. IP : play.badblock.fr"> <meta name="keywords" content="badblock, boutique, serveur mini-jeux, serveur skyblock, skyblock minecraft, skyblock, spaceballs, minecraft mini-jeux, serveur faction, serveur freebuild, freecube, minecraft serveur, serveur minecraft, launcher minecraft, télécharger, download, launcher lifecraft, lifecraft launcher, minecraft lifecraft, launcher premium, launcher gratuit, minecraft, launcher minecraft, telecharger, installer, 1.8, 1.9, 1.10, 1.11, 1.7 launcher, minecraft, cracké, premium, non officiel, launcher gratuit, officiel, serveur, badblock, français, serveur français, launchers, badblock, loncher, executable, rush, tower, speeduhc, uhcspeed, uhc run, uhcrun, cts, capture the sheep, capture the flag, ctf, survivalgames, survival, hungergames, hg, survivalgame, spaceballs, cube of steels, cube of steel, spaceball, space ball, pearlswar, pearlswar, pvpbox, box, 1v1, dayz, infecté, faction, serveur minecraft, minecraft, skyblock, freebuild, launcher minecraft, minecraft, jouer, minecraft, gratuit, minecraft gratuit, minecraft crack, crack, meilleur launcher de france" /> <meta name="twitter:card" content="summary" /> <meta name="twitter:image" content="https://cdn.badblock.fr/images/logo-réseaux-sociaux.jpg" /> <meta name="twitter:url" content="https://store.badblock.fr/" /> <meta name="twitter:site" content="@BadBlockGame"> <meta name="twitter:creator" content="@BadBlockGame" /> <meta name="twitter:description" content="Jouez au meilleur Serveur Minecraft accessible à tout le monde gratuitement. Découvre nos jeux SkyBlock Mini-Jeux FreeBuild Faction et bien plus encore ! Télécharge le jeu gratuitement. Rejoins nous sur notre serveur Minecraft. IP : play.badblock.fr" /> <meta property="og:image:width" content="177" /> <meta property="og:image:height" content="177" /> <meta property="og:locale" content="fr" /> <meta property="og:image" content="https://cdn.badblock.fr/images/logo-réseaux-sociaux.jpg" /> <meta property="og:type" content="website" /> <script data-ad-client="ca-pub-9979868023188321" async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script> <meta name="theme-color" content="#2d94cc" /> <title>BadBlock – Serveur Minecraft Français</title> <link href="https://fonts.googleapis.com/css?family=Karla:400,400i,700,700i" rel="stylesheet"> <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.0/css/all.css" integrity="sha384-Mmxa0mLqhmOeaE8vgOSbKacftZcsNYDjQzuCOm6D02luYSzBG8vpaOykv9lFQ51Y" crossorigin="anonymous"> <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,400i,500,700,900"> <link rel="stylesheet" href="/inc/css/animate.css"> <meta property="og:site_name" content="BadBlock – Serveur Minecraft Français" /> <meta property="og:type" content="website" /> <meta property="og:title" content="BadBlock – Serveur Minecraft Français" /> <meta property="twitter:title" content="BadBlock – Serveur Minecraft Français" /> <meta property="og:url" content="https://badblock.best/" /> <meta property="og:image" content="https://cdn.badblock.fr/images/logo-réseaux-sociaux.jpg" /> <meta property="twitter:image" content="https://cdn.badblock.fr/images/logo-réseaux-sociaux.jpg" /> <meta property="twitter:card" content="summary" /> <meta name="theme-color" content="rgb(24, 88, 134)" /> <link rel="preload" href="/styles/fonts/fa/fa-regular-400.woff2" as="font" type="font/woff2" crossorigin="anonymous" /> <link rel="preload" href="/styles/fonts/fa/fa-solid-900.woff2" as="font" type="font/woff2" crossorigin="anonymous" /> <link rel="preload" href="/styles/fonts/fa/fa-brands-400.woff2" as="font" type="font/woff2" crossorigin="anonymous" /> <link rel="stylesheet" href="/css.php?css=public%3Anormalize.css%2Cpublic%3Acore.less%2Cpublic%3Aapp.less&amp;s=15&amp;l=2&amp;d=1572813727&amp;k=1e4d3fd7fae89f282a1c5b2b1b0c95816bd73df3" /> <link rel="stylesheet" href="/css.php?css=public%3Afetured_thread_css.less%2Cpublic%3Ahome_blog_xenbros_list_css.less%2Cpublic%3Ath_covers.less%2Cpublic%3Axb_hb_main.less%2Cpublic%3Aextra.less&amp;s=15&amp;l=2&amp;d=1572813727&amp;k=810e22d8bf7b6287de0f8a9feea16d2214f6f71b" />
    <head>

    <style>

	.pageNav{
		display:none;
		}
	
    </style>

    </head>

    <body>



    </body>
</html>

<?php

/**
 *
 * SOME USEFUL TOOLS
 *
 */

function getConfig(){
    return [
        "urls" => [
            'home' => "https://badblock.fr",
            'store' => "https://store.badblock.fr"
        ]
    ];
}

/**
 * Don't add a / at the beginning when calling this function
 * @param $path
 * @return string
 */
function assets($path)
{
    return '../assets/' . $path;
}

function layouts($name)
{
    return include_once '../layouts/' . $name . '.php';
}

if ($_GET) {

    $params = array_map('strip_tags', $_GET);

    /**
     *
     * HERE IS THE API
     *
     */
    if (isset($params['player'])) {

        $sql = "SELECT votes_nb FROM votes WHERE user_uuid = ?";

        if (isset($params['month']) && isset($params['year']))
            $sql .= " AND month = ? AND year = ";
        elseif (isset($params['year']))
            $sql .= " AND year = ?";
        elseif (isset($params['month']))
            $sql .= " AND month = ?";

        $values = [];
        foreach ($params as $key => $value) {
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

        $pdo = new PDO("mysql:host=" . $credentials['host'] . ";port=" . $credentials['port'] . ";dbname=" . $credentials['db'], $credentials['user'], $credentials['password'], [
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_OBJ
        ]);

        $result = $pdo->prepare($sql);
        $result->execute($values);
        $result = $result->fetch();

        if ($result)
            echo json_encode($result);
        else
            echo json_encode("[]");


    } else
        return http_response_code(404);
}

/**
 *
 * HERE IS THE VOTE CONFIRMATION
 *
 */

?>


<!DOCTYPE html>
<html lang="fr">
<?php layouts('header'); ?>
<body>

<?php layouts('nav-top') ?>

<div class="container-fluid">
    <div class="panel">
        <div class="panel-body">
            <div class="p-body-inner">
                <div class="content">
                    <h1 class="title-center text-uppercase">Votez pour notre serveur !</h1>
                    <hr>
                    <div class="row">
                        <div class="col-lg-6 col-xs-12 vote-input center">
                            <input type="text" placeholder="Votre nom d'utilisateur"
                                   id="username" name="username">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-4">
                            <div class="rank-header">
                                <?php
                                /**
                                * TODO
                                 * Add month + year next to "Classement" string
                                 * Add a "See more" button (modal ?)
                                 **/
                                ?>
                                <h3 class="text-uppercase">Classement</h3>
                            </div>
                            <div class="rank-first">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <h4 class="rank-number">#1</h4>
                                    </div>
                                    <div class="col-xs-9">
                                        <div class="media">
                                            <div class="media-left">
                                                <a href="#">
                                                <img class="media-object" src="<?= assets('img/crown.png') ?>" alt="Crown" style="max-width: 64px;">
                                                </a>
                                            </div>
                                            <div class="media-body">
                                                <h4>Vanilor</h4>
                                            </div>
                                        </div>
                                        <hr>
                                        <h5 class="rank-vote">257 VOTES</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-6 col-xs-12 no-padding-mobile-right">
                                    <div class="rank-second">
                                        <div class="row">
                                            <div class="col-xs-3">
                                                <h4 class="rank-number">#2</h4>
                                            </div>
                                            <div class="col-xs-9">
                                                <h4 class="rank-username">Hookifza</h4>
                                                <h5 class="rank-vote">195 VOTES</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-xs-12 no-padding-mobile-left">
                                    <div class="rank-third">
                                        <div class="row">
                                            <div class="col-xs-3">
                                                <h4 class="rank-number">#3</h4>
                                            </div>
                                            <div class="col-xs-9">
                                                <h4 class="rank-username">_Skew_</h4>
                                                <h5 class="rank-vote">78 VOTES</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="ranking-pair">
                                <div class="row">
                                    <div class="col-xs-2">
                                        <h4 class="rank-number">#4</h4>
                                    </div>
                                    <div class="col-xs-10">
                                        <h4 class="rank-username">Hookwood</h4>
                                        <h5 class="rank-vote">72 VOTES</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="ranking-impair ranking-last">
                                <div class="row">
                                    <div class="col-xs-2">
                                        <h4 class="rank-number">#5</h4>
                                    </div>
                                    <div class="col-xs-10">
                                        <h4 class="rank-username">XxA1ExX</h4>
                                        <h5 class="rank-vote">64 VOTES</h5>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4">
                            <div class="vote-button">
                                <a href="https://minecraft.top-serveurs.net/vote/badblock-5d49e769e7cf1"
                                   class="btn btn-block btn-lg btn-default site-btn disabled"
                                   target="_blank"
                                >
                                    <p>Top serveurs (2h)</p>
                                </a>
                                <a href="https://topg.org/fr/Minecraft/in-518057"
                                   class="btn btn-block btn-lg btn-default site-btn disabled"
                                   target="_blank"
                                >
                                    TOPG
                                </a>
                                <a href="https://www.serveurminecraft.org/serveur/3448"
                                   class="btn btn-block btn-lg btn-default site-btn disabled"
                                   target="_blank"
                                >
                                    Serveurs Minecraft (24h)
                                </a>
                            </div>
                        </div>
                        <div class="col-lg-4">
                            <div class="server-list">
                                <h4>Choisi ta récompense !</h4>
                                <hr>
                                <div class="servers">
                                    <button class="btn disabled" id="skyblock">
                                        <img alt="BadBlock Skyblock" class="img-responsive"
                                             src="<?= assets('img/BB_key_2.png'); ?>">
                                    </button>
                                    <button class="btn disabled" id="mini-jeux">
                                        <img alt="BadBlock Mini-games" class="img-responsive"
                                             src="<?= assets('img/BB_key_3.png'); ?>">
                                    </button>
                                    <button class="btn disabled" id="survie">
                                        <img alt="BadBlock Survival" class="img-responsive"
                                             src="<?= assets('img/BB_key_1.png'); ?>">
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <?php
        /**
         * TODO
         * Add Skew contact
         **/
        ?>
        <div class="panel-footer" style="margin-top: 20px">
            <p class="text-center">Copyright &copy; BadBlock 2019<?php if(date("Y") != 2019) echo "-".date("Y"); ?> | Site réalisé par <a href="#" target="_blank">Skew</a> & <a href="https://vanilor.info" target="_blank">Vanilor</a></p>
        </div>
    </div>
</div>

        <?php layouts('footer'); ?>

</body>
</html>
<?php

include_once '../App/Helper.php';

function getRanking()
{

    $request = db()->prepare("SELECT votes.user_uuid, votes.vote_nb, users.name
                                        FROM votes 
                                        INNER JOIN users ON votes.user_uuid=users.uuid
                                        WHERE votes.month = ? AND votes.year = ?
                                        ORDER BY votes.vote_nb DESC");
    $request->execute([date("m"), date("Y")]);
    $results = $request->fetchAll();

    if ($results === false)
        return array();
    else
        return $results;

}

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
                        <div class="col-lg-6 col-xs-12 vote-input center" id="usernameDiv">
                            <p class="alert-danger text-center hide" id="error"></p>
                            <input type="text" placeholder="Votre nom d'utilisateur"
                                   id="username" name="username">
                        </div>
                    </div>
                    <div class="row col-lg-4">
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

                        <?php
                        $players = getRanking();
                        foreach ($players as $key => $player): ?>
                            <?php $key++ ?>
                            <?php if ($key <= 3): ?>
                                <div class="rank-<?= $key ?>">
                            <?php endif; ?>
                            <div class="col-xs-3">
                                <h4 class="rank-number">#<?= $key; ?></h4>
                            </div>
                            <div class="col-xs-9">
                                <?php if ($key == 1): ?>
                                    <div class="media">
                                        <div class="media-left">
                                            <a href="#">
                                                <img class="media-object"
                                                     src="<?= assets('img/crown.png') ?>" alt="Crown"
                                                     style="max-width: 64px;">
                                            </a>
                                        </div>
                                        <div class="media-body">
                                            <h4 class="rank-username"><?= $player['name']; ?></h4>
                                        </div>
                                    </div>
                                <?php else: ?>
                                    <h4 class="rank-username"><?= $player['name']; ?></h4>
                                <?php endif; ?>
                                <hr>
                                <h5 class="rank-vote text-uppercase"><?= $player['vote_nb'] ?> votes</h5>
                            </div>

                            <?php if ($key <= 3): ?>
                                </div>
                            <?php endif;
                        endforeach; ?>
                    </div>
                    <div class="row col-lg-4">
                        <div class="vote-button">
                            <a href="https://minecraft.top-serveurs.net/vote/badblock-5d49e769e7cf1"
                               class="btn btn-block btn-lg btn-default site-btn disabled btn-default"
                               target="_blank"
                            >
                                <p>Top serveurs (2h)</p>
                            </a>
                            <a href="https://www.serveurminecraft.org/serveur/3448"
                               class="btn btn-block btn-lg btn-default site-btn disabled btn-default"
                               target="_blank"
                            >
                                Serveurs Minecraft (24h)
                            </a>
                        </div>
                    </div>
                    <div class="row col-lg-4">
                        <div class="server-list">
                            <h4>Choisi ta récompense !</h4>
                            <hr>
                            <div class="servers">
                                <button class="btn disabled get-award-btn" id="skyblock">
                                    <img alt="BadBlock Skyblock" class="img-responsive"
                                         src="<?= assets('img/BB_key_2.png'); ?>">
                                </button>
                                <button class="btn disabled get-award-btn" id="mini-jeux">
                                    <img alt="BadBlock Mini-games" class="img-responsive"
                                         src="<?= assets('img/BB_key_3.png'); ?>">
                                </button>
                                <button class="btn disabled get-award-btn" id="survie">
                                    <img alt="BadBlock Survival" class="img-responsive"
                                         src="<?= assets('img/BB_key_1.png'); ?>">
                                </button>
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
            <p class="text-center">Copyright &copy; BadBlock 2019<?php if (date("Y") != 2019) echo "-" . date("Y"); ?> |
                Site réalisé par <a href="https://skewram.tk/" target="_blank">Skew</a> & <a href="https://vanilor.info"
                                                                                             target="_blank">Vanilor</a>
            </p>
        </div>
    </div>
</div>

<?php layouts('footer'); ?>

</body>
</html>
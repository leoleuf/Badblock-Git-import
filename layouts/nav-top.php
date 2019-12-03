<div class="header-block ">
    <div class="navigation">
        <nav class="navbar navbar-default navbar-dark">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#nav-top">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>

                <div class="collapse navbar-collapse" id="nav-top">
                    <ul class="nav navbar-nav">
                        <li><a href="<?= getConfig()['urls']['home']; ?>"><i class="fa fa-home fa-large fa-fixed-width" style="padding-right:5px;"></i>Accueil</a></li>
                        <li><a href="<?= getConfig()['urls']['home']."/play"; ?>"><i class="fa fa-play-circle fa-large fa-fixed-width" style="padding-right:5px; color:#EA2027;"></i>Jouer</a></li>
                        <li><a href="<?= getConfig()['urls']['home']."/forums"; ?>"><i class="fa fa-comments fa-large fa-fixed-width" style="padding-right:5px; color:#1B1464;"></i>Forums</a></li>
                        <li class="active"><a href="<?= getConfig()['urls']['home']."/vote"; ?>"><i class="fa fa-gift fa-large fa-fixed-width" style="padding-right:5px; color:#009432;"></i>Vote</a></li>
                        <li><a href="<?= getConfig()['urls']['home']."/support"; ?>"><i class="fa fa-question fa-large fa-fixed-width" style="padding-right:5px; color:#D980FA;"></i>Assistance</a></li>
                        <li><a href="<?= getConfig()['urls']['store']; ?>"><i class="fa fa-shopping-cart fa-large fa-fixed-width" style="padding-right:5px; color:#FFC312;"></i>Boutique</a></li>
                    </ul>
                    <?php
                    /**
                     * TODO
                     * Fix navbar-right class not working
                     */
                    ?>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="https://badblock.fr/connexion" target="_blank">S'authentifier sur le site</a></li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <div class="container flex">
        <li class="left" id="copy" data-clipboard-text="play.badblock.fr">
            <div class="ip-copied">IP copié! À bientôt :)</div>
            <small>Cliquez pour copier l'adresse IP</small>

            <?php
            /**
            *   TODO
             * Dynamically update Minecraft servers and Discord connected clients amount
             **/
            ?>

            <div class="players">Chargement...</div>
            <a href="<?= $config['urls']['home']."/play"; ?>" class="btn btn-sm"
               style="background-color: #487eb0; margin-top: 15px;">
                <i class="fas fa-download"></i> Télécharger notre launcher Minecraft
            </a>
        </li>
        <li class="logo">
            <a href="https://badblock.fr"><img src="https://cdn.badblock.fr/images/logo_badblock_text.png"></a>
        </li>
        <li class="right">
            <a href="https://bblock.pw/discord" target="_blank">
                <small>Rejoignez notre Discord</small>
                <div class="discord players">Chargement...</div>
            </a>
            <a href="https://invite.teamspeak.com/ts.badblock.fr" class="btn btn-sm"
               style="background-color: #487eb0; margin-top: 15px;">
                <i class="fab fa-teamspeak"></i> Rejoindre notre TeamSpeak
            </a>
        </li>
    </div>
</div>
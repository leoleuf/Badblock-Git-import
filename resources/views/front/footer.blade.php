<?php

$creationDate = "2018";
$date = date("Y");
if ($date != $creationDate)
{
    $date = $creationDate."-".$date;
}

?>

<!-- start footer Area -->
<footer class="footer-area section-gap">
    <div class="container">
        <div class="row">
            <div class="col-lg-3 col-md-12 single-footer-widget">
                    <span class="ft">Serveur MultiGames</span><br /><br />
                    Serveur MultiGames est un site Internet de liste et de classement des serveurs de jeux. Ajoutez gratuitement un serveur ou trouvez votre serveur préféré.
                    @if (session()->has('online_count'))
                        {{ session()->get('online_count') }} utilisateurs en ligne
                    @endif
            </div>
            <div class="col-lg-3 col-md-12 single-footer-widget">
                    <span class="ft">Créateurs</span>
                    <ul class="footer-nav">
                        <li><a title="Ajouter mon serveur sur Serveur-MultiGames" href="/add-server">Ajouter votre serveur</a></li>
                        <li><a title="Mettre en place l'API sur son site" href="/api">Mise en place API</a></li>
                        @if(!Auth::user())
                            <li><a title="Se connecter sur Serveur-MultiGames" href="/login">Se connecter</a></li>
                            <li><a title="S'inscrire sur Serveur-MultiGames" href="/register">S'inscrire</a></li>
                        @else
                            <li><a title="Accéder au Tableau de Bord" href="/dashboard">Tableau de Bord</a></li>
                            <li><a title="Se déconnecter" href="/logout">Se déconnecter</a></li>
                        @endif
                    </ul>
            </div>
            <div class="col-lg-3 col-md-12 single-footer-widget">
                <span class="ft">Nos différents jeux</span>
                    <ul class="footer-nav">
                        @foreach(config('tag.cat') as $k)
                            <li><img alt="Serveur {{ $k }}" src="/img/{{ encname($k) }}.png" width="24" height="24" class="ialign" /> &nbsp; <a title="Liste des serveurs {{ $k }}" href="/{{ encname($k) }}">{{ ucfirst($k) }}</a></li>
                        @endforeach
                    </ul>
            </div>
            <div class="col-lg-3 col-md-12 single-footer-widget">
                <span class="ft">Informations légales</span>
                    <ul class="footer-nav">
                        <li><a title="Conditions d'utilisation" href="/tos">Conditions d'Utilisation</a></li>
                        <li><a title="Foire aux questions" href="/faq">Foire aux Questions</a></li>
                        <li><a title="Règlement" href="/reglement">Règlement</a></li>
                        <li><a title="Nous contacter" href="/contact">Contact</a></li>
                        <li><a title="VPN detect" href="https://ipdetector.info">Detect vpn</a></li>
                        <li></li>
                    </ul>
            </div>
            <!--<div class="col-lg-6  col-md-12">
            </div>
            <div class="col-lg-3  col-md-12">
                <div class="single-footer-widget mail-chimp">
                </div>
            </div>
            !-->
        </div>

        <div class="row footer-bottom d-flex justify-content-between">
            <p class="col-lg-8 col-sm-12 footer-text m-0 text-white">
                Copyright &copy; <?php echo $date; ?> Serveur MultiGames. Liste des meilleurs serveurs de jeu. Tous droits réservés
            </p>
        </div>
    </div>
</footer>
<!-- End footer Area -->
@if (!isset($classement))
<link rel="stylesheet" href="/css/magnific-popup.css">
<link rel="stylesheet" href="/css/nice-select.css">
<link rel="stylesheet" href="/css/animate.min.css">
<link rel="stylesheet" href="/css/owl.carousel.css">
<link rel="stylesheet" href="/css/linearicons.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css?family=Poppins:100,200,400,300,500,600,700" rel="stylesheet" />
@endif
<script @if(!trim($__env->yieldContent('gjs-normal')))async defer @endif src="/js/gjs.js"></script>
<script async defer src="/gtag.js"></script>
<!--<script async defer>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-122426050-1');

    var giftofspeed = document.createElement('link');
    giftofspeed.rel = 'stylesheet';
    giftofspeed.href = '/css/df.css';
    giftofspeed.type = 'text/css';
    var godefer = document.getElementsByTagName('link')[0];
    godefer.parentNode.insertBefore(giftofspeed, godefer);
</script>!-->

@if (!isMobile())
    <script async defer type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-5bd996025e31aea0"></script>
@endif

@if (!isset($classement))
 <script>   (adsbygoogle=window.adsbygoogle||[]).push({google_ad_client:"ca-pub-1905923613312160",enable_page_level_ads:!0});</script>
@endif